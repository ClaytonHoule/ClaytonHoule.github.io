package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySMS extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;

    private TextView textSmsStatus;
    private Button buttonEnableSms;
    private Button buttonDisableSms;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        textSmsStatus = findViewById(R.id.textSmsStatus);
        buttonEnableSms = findViewById(R.id.buttonEnableSms);
        buttonDisableSms = findViewById(R.id.buttonDisableSms);
        buttonBack = findViewById(R.id.buttonBack);

        buttonEnableSms.setOnClickListener(v -> requestSmsPermission());

        buttonDisableSms.setOnClickListener(v -> {
            textSmsStatus.setText("Status: SMS notifications disabled");
            Toast.makeText(this, "SMS notifications disabled", Toast.LENGTH_SHORT).show();
        });

        buttonBack.setOnClickListener(v -> finish());
    }

    // Checks whether SMS permission is already granted or requests it from the user
    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            textSmsStatus.setText("Status: SMS notifications enabled");
            Toast.makeText(this, "SMS notifications already enabled", Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE
            );
        }
    }

    // Handles the user's response to the SMS permission prompt
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                textSmsStatus.setText("Status: SMS notifications enabled");
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();

            } else {
                textSmsStatus.setText("Status: SMS notifications disabled");
                Toast.makeText(this, "SMS permission denied. App will still work without SMS.", Toast.LENGTH_LONG).show();
            }
        }
    }
}