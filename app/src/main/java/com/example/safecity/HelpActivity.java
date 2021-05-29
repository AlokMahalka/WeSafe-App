package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    private ImageView helpBackBtn;
    private TextView fireBtn;
    private TextView policeBtn;
    private TextView ambulanceBtn;
    private TextView womenHelpline1;
    private TextView womenHelpline2;
    private TextView panBtn;
    private TextView disasterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        helpBackBtn = findViewById(R.id.helpBackBtn);
        fireBtn = findViewById(R.id.fireBtn);
        policeBtn = findViewById(R.id.policeBtn);
        ambulanceBtn = findViewById(R.id.ambulanceBtn);
        womenHelpline1 = findViewById(R.id.womenHelpline1);
        womenHelpline2 = findViewById(R.id.womenHelpline2);
        panBtn = findViewById(R.id.panBtn);
        disasterBtn = findViewById(R.id.disasterBtn);

        helpBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HelpActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });

        fireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:101"));
                startActivity(i);
            }
        });

        policeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:100"));
                startActivity(i);
            }
        });

        ambulanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:102"));
                startActivity(i);
            }
        });

        womenHelpline1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1091"));
                startActivity(i);
            }
        });

        womenHelpline2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:181"));
                startActivity(i);
            }
        });

        panBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:112"));
                startActivity(i);
            }
        });

        disasterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:108"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.CALL_PHONE},123);

        }
    }
}