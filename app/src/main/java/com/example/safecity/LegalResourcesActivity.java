package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LegalResourcesActivity extends AppCompatActivity {
    private ImageView legalResourcesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_resources);
        legalResourcesBtn = findViewById(R.id.contactBackBtn);

        AlertDialog.Builder builder = new AlertDialog.Builder(LegalResourcesActivity.this);
        builder.setMessage("The content provided here is in no way a substitute for professional legal advice.")
                .setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(LegalResourcesActivity.this, MenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        builder.create();
        builder.show();

        legalResourcesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LegalResourcesActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}