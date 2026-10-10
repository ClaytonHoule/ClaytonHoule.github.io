package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class ActivityMain extends AppCompatActivity {

    EditText editItemName;
    EditText editQuantity;
    Button buttonAddItem;
    Button buttonSmsSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSmsSettings = findViewById(R.id.buttonSmsSettings);
        editItemName = findViewById(R.id.editItemName);
        editQuantity = findViewById(R.id.editQuantity);
        buttonAddItem = findViewById(R.id.buttonAddItem);

        buttonSmsSettings.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityMain.this, ActivitySMS.class);
            startActivity(intent);
        });

    }
}