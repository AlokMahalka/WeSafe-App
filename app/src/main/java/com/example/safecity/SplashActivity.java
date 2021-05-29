package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fa = FirebaseAuth.getInstance();
        SystemClock.sleep(3000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = fa.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(SplashActivity.this, IntroActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent in = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }
    }
}