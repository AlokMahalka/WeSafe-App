package com.example.safecity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private FirebaseFirestore firebaseFirestore;
    private ImageView menuBtn;
    private Button sosBtn;
    private Button shareIncidentBtn;
    private double currentLat;
    private double currentLong;
    public String channelID = "channel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuBtn = findViewById(R.id.menuBtn);
        sosBtn = findViewById(R.id.sosBtn);
        shareIncidentBtn = findViewById(R.id.shareIncidentBtn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        createNotificationChannel();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SOSActivity.class);
                startActivity(i);
            }
        });

        shareIncidentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, IncidentActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isLocationEnabled();
        } else {
            getLastLocation();
            getIncidentDetails(mMap);
        }
    }

    private void getIncidentDetails(final GoogleMap googleMap) {

        firebaseFirestore.collection("INCIDENTS").document("USER_INCIDENTS").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (long x = 1; x <= (long) task.getResult().get("no_of_incidents"); x++) {
                                googleMap.addCircle(new CircleOptions().center(new LatLng((double) task.getResult().get("LOCATION_LAT_" + x), (double) task.getResult().get("LOCATION_LONG_" + x))).radius(400).fillColor(0x70ff0000));
                                googleMap.addMarker(new MarkerOptions().position(new LatLng((double) task.getResult().get("LOCATION_LAT_" + x), (double) task.getResult().get("LOCATION_LONG_" + x))).title("Type: " + task.getResult().get("INCIDENT_TYPES_" + x)).snippet("Safety Tip: " + task.getResult().get("SAFETY_TIP_" + x)).icon(null));
                                if (((Math.round(currentLat * 100.0) / 100.0) == (double) task.getResult().get("LOCATION_LAT_" + x)) && ((Math.round(currentLong * 100.0) / 100.0) == (double) task.getResult().get("LOCATION_LONG_" + x))) {
                                    Intent intent = new Intent(MainActivity.this, SOSActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                                            .setChannelId(channelID)
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setContentTitle("Warning!")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setAutoCancel(true)
                                            .setSmallIcon(R.drawable.ic_baseline_warning_24)
                                            .setStyle(new NotificationCompat.BigTextStyle().bigText("You entered a Danger Zone!"))
                                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                            .setContentIntent(pendingIntent);

                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    mNotificationManager.notify(1, builder.build());
                                }
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            currentLat = location.getLatitude();
                            currentLong = location.getLongitude();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 14.0f));
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            LatLng current = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            currentLat = mLastLocation.getLatitude();
            currentLong = mLastLocation.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 14.0f));
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, "Channel", importance);
            channel.setDescription("This is channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}