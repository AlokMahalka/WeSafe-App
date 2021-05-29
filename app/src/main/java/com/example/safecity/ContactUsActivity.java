package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContactUsActivity extends AppCompatActivity {
    private ImageView contactUsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        contactUsBtn = findViewById(R.id.contactBackBtn);
        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactUsActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}