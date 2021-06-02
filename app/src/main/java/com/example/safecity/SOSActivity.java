package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SOSActivity extends AppCompatActivity {
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<String> numbers = new ArrayList<>();
    private EditText sosEmer1;
    private EditText sosEmer2;
    private EditText sosEmer3;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String currentLocation = "";
    private Button btnSOS;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private TextView playSiren;
    private TextView stopSiren;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_o_s);

        sosEmer1 = findViewById(R.id.sosEmer1);
        sosEmer2 = findViewById(R.id.sosEmer2);
        sosEmer3 = findViewById(R.id.sosEmer3);
        btnSOS = findViewById(R.id.btnSOS);
        playSiren = findViewById(R.id.playSiren);
        stopSiren = findViewById(R.id.stopSiren);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.siren);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (int x = 1; x <= 3; x++) {
                        numbers.add(task.getResult().get("Emer" + x).toString());
                    }
                    sosEmer1.setText(numbers.get(0));
                    sosEmer2.setText(numbers.get(1));
                    sosEmer3.setText(numbers.get(2));
                } else {
                    Toast.makeText(getApplicationContext(), "Please, Enter Emergency Contact!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                sendSMS();
            }
        });

        playSiren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });

        stopSiren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });
    }



    public void sendSMS() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(sosEmer1.getText().toString(), null, "SOS: I need your help! My current location is: " + currentLocation, null, null);
                smsManager.sendTextMessage(sosEmer2.getText().toString(), null, "SOS: I need your help! My current location is: " + currentLocation, null, null);
                smsManager.sendTextMessage(sosEmer3.getText().toString(), null, "SOS: I need your help! My current location is: " + currentLocation, null, null);
                smsManager.sendTextMessage("+918861640061", null, "SOS: I need your help! My current location is: " + currentLocation, null, null);
                Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
            }
        }
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
                            double lat = location.getLatitude();
                            double longi = location.getLongitude();
                            currentLocation = "http://maps.google.com/maps?q=" + lat + "," + longi;
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
            double lat = mLastLocation.getLatitude();
            double longi = mLastLocation.getLongitude();
            currentLocation = "http://maps.google.com/maps?q=" + lat + "," + longi;
        }
    };

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
}