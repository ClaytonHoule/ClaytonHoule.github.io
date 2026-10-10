package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ActivitySMS extends AppCompatActivity {

    TextView textSmsStatus;
    Button buttonEnableSms;
    Button buttonDisableSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        textSmsStatus = findViewById(R.id.textSmsStatus);
        buttonEnableSms = findViewById(R.id.buttonEnableSms);
        buttonDisableSms = findViewById(R.id.buttonDisableSms);

        buttonEnableSms.setOnClickListener(v ->
                textSmsStatus.setText("SMS notifications enabled"));

        buttonDisableSms.setOnClickListener(v ->
                textSmsStatus.setText("SMS notifications disabled"));
    }
}