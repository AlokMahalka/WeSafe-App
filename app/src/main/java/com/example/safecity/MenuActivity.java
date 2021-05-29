package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    private ImageView backBtn;
    private TextView logoutBtn;
    private TextView legalResBtn;
    private TextView helpBtn;
    private TextView contactUsBtn;
    private TextView flashlightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent registerIntent = new Intent(MenuActivity.this, VerificationActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        legalResBtn = findViewById(R.id.legalResourcesBtn);
        legalResBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, LegalResourcesActivity.class);
                startActivity(i);
            }
        });

        helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });

        contactUsBtn = findViewById(R.id.contactUsBtn);
        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ContactUsActivity.class);
                startActivity(i);
            }
        });

        flashlightBtn = findViewById(R.id.flashlighBtn);
        flashlightBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this,FlashLight.class);
                startActivity(i);
            }
        }));
    }
}