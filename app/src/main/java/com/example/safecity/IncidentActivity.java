package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IncidentActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private ImageView backIncidentBtn;
    private CheckBox checkBox;
    private Button startBtn;
    LocationManager locationManager;
    private ConstraintLayout f1;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private ConstraintLayout detailsCL;
    GoogleMap mMap;
    private TextView latlng;
    private FirebaseFirestore firebaseFirestore;
    private double finalLat, finalLong;

    private EditText describeIncident;
    private EditText shareSafetyED;
    private EditText incidentDate;
    private Button submitIncident;
    private AppCompatCheckBox confirmBtn;
    private RadioButton yesRB;
    private RadioButton noRB;
    private CheckBox c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12;
    private long no_of_incidents;
    String regex = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLocation);
        mapFragment.getMapAsync(this);

        f1 = findViewById(R.id.f1);
        detailsCL = findViewById(R.id.detailsCL);
        latlng = findViewById(R.id.LatLng);
        firebaseFirestore = FirebaseFirestore.getInstance();

        describeIncident = findViewById(R.id.describeIncident);
        shareSafetyED = findViewById(R.id.shareTipsED);
        incidentDate = findViewById(R.id.incidentDate);
        submitIncident = findViewById(R.id.submitIncident);
        confirmBtn = findViewById(R.id.confirmBtn);
        yesRB = findViewById(R.id.yesRB);
        noRB = findViewById(R.id.noRB);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);
        c9 = findViewById(R.id.c9);
        c10 = findViewById(R.id.c10);
        c11 = findViewById(R.id.c11);
        c12 = findViewById(R.id.c12);

        firebaseFirestore.collection("INCIDENTS").document("USER_INCIDENTS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    no_of_incidents = (long) task.getResult().get("no_of_incidents");
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox = findViewById(R.id.checkBox);
        startBtn = findViewById(R.id.startButton);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    f1.setVisibility(View.GONE);
                    detailsCL.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Tick the Checkbox!", Toast.LENGTH_SHORT).show();
                    checkBox.requestFocus();
                }
            }
        });

        backIncidentBtn = findViewById(R.id.backIncidentBtn);
        backIncidentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IncidentActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        submitIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitIncident.setEnabled(false);
                if (!TextUtils.isEmpty(describeIncident.getText().toString())) {
                    if (!TextUtils.isEmpty(shareSafetyED.getText().toString())) {
                        if (!TextUtils.isEmpty(incidentDate.getText().toString()) && incidentDate.getText().toString().matches(regex)) {
                            if (confirmBtn.isChecked() && (c1.isChecked() || c2.isChecked() || c3.isChecked() || c4.isChecked() || c5.isChecked() || c6.isChecked() || c7.isChecked() || c8.isChecked() || c9.isChecked() || c10.isChecked() || c11.isChecked() || c12.isChecked()) && (yesRB.isChecked() || noRB.isChecked())) {
                                String typesOfIncident = "";
                                if (c1.isChecked()) {
                                    typesOfIncident += c1.getText();
                                }
                                if (c2.isChecked()) {
                                    typesOfIncident += " " + c2.getText();
                                }
                                if (c3.isChecked()) {
                                    typesOfIncident += " " + c3.getText();
                                }
                                if (c4.isChecked()) {
                                    typesOfIncident += " " + c4.getText();
                                }
                                if (c5.isChecked()) {
                                    typesOfIncident += " " + c5.getText();
                                }
                                if (c6.isChecked()) {
                                    typesOfIncident += " " + c6.getText();
                                }
                                if (c7.isChecked()) {
                                    typesOfIncident += " " + c7.getText();
                                }
                                if (c8.isChecked()) {
                                    typesOfIncident += " " + c8.getText();
                                }
                                if (c9.isChecked()) {
                                    typesOfIncident += " " + c9.getText();
                                }
                                if (c10.isChecked()) {
                                    typesOfIncident += " " + c10.getText();
                                }
                                if (c11.isChecked()) {
                                    typesOfIncident += " " + c11.getText();
                                }
                                if (c12.isChecked()) {
                                    typesOfIncident += " " + c12.getText();
                                }

                                Map<String, Object> incidentData = new HashMap<>();
                                incidentData.put("no_of_incidents", no_of_incidents + 1);
                                incidentData.put("INCIDENT_DESC_" + (no_of_incidents + 1), describeIncident.getText().toString());
                                incidentData.put("SAFETY_TIP_" + (no_of_incidents + 1), shareSafetyED.getText().toString());
                                incidentData.put("INCIDENT_DATE_" + (no_of_incidents + 1), incidentDate.getText().toString());
                                incidentData.put("INCIDENT_TYPES_" + (no_of_incidents + 1), typesOfIncident);
                                @SuppressLint("DefaultLocale") double final_Lat = Math.round(finalLat * 100.0) / 100.0;
                                @SuppressLint("DefaultLocale") double final_Long = Math.round(finalLong * 100) / 100.0;
                                incidentData.put("LOCATION_LAT_" + (no_of_incidents + 1), final_Lat);
                                incidentData.put("LOCATION_LONG_" + (no_of_incidents + 1), final_Long);

                                firebaseFirestore.collection("INCIDENTS").document("USER_INCIDENTS").update(incidentData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(IncidentActivity.this, "Incident Reported!", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(IncidentActivity.this, MainActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                submitIncident.setEnabled(true);
                                Toast.makeText(IncidentActivity.this, "Please, select all the required fields!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            submitIncident.setEnabled(true);
                            incidentDate.setError("Enter valid Incident Date!");
                            incidentDate.requestFocus();
                        }
                    } else {
                        submitIncident.setEnabled(true);
                        shareSafetyED.setError("Enter Safety Tips!");
                        shareSafetyED.requestFocus();
                    }
                } else {
                    submitIncident.setEnabled(true);
                    describeIncident.setError("Please, Describe your Incident!");
                    describeIncident.requestFocus();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getLastLocation();

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latLng = marker.getPosition();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                    finalLat = latLng.latitude;
                    finalLong = latLng.longitude;
                    latlng.setText(latLng.latitude + ", " + latLng.longitude);
                    Toast.makeText(IncidentActivity.this, address.toString(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
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
                            double lat = location.getLatitude();
                            double longi = location.getLongitude();
                            finalLat = lat;
                            finalLong = longi;
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 14.0f));
                            mMap.addMarker(new MarkerOptions().position(current).title("Incident Location").draggable(true));
                            latlng.setText(lat + ", " + longi);
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
            double lat = mLastLocation.getLatitude();
            double longi = mLastLocation.getLongitude();
            finalLat = lat;
            finalLong = longi;
            LatLng current = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 14.0f));
            mMap.addMarker(new MarkerOptions().position(current).title("Incident Location").draggable(true));
            latlng.setText(lat + ", " + longi);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}