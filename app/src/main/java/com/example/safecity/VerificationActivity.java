package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class VerificationActivity extends AppCompatActivity {
    private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        fl = findViewById(R.id.main_fl);
        setDefaultFragment(new SignInFragment());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setFragment(new SignInFragment());
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setDefaultFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fl.getId(), fragment);
        ft.commit();
    }

    private void setFragment(Fragment fg) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        ft.replace(fl.getId(), fg);
        ft.commit();
    }

}