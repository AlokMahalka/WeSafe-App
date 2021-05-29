package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class IntroActivity extends AppCompatActivity {
    private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        fl = findViewById(R.id.introFL);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fl.getId(), new F1());
        ft.commit();
    }
}