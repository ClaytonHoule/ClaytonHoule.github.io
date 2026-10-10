package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class ActivityLogin extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;
    private Button buttonLogin;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
            startActivity(intent);
        });

        buttonCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
            startActivity(intent);
        });
    }
}
