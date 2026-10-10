package com.example.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityLogin : AppCompatActivity() {
    private var editUsername: EditText? = null
    private var editPassword: EditText? = null
    private var buttonLogin: Button? = null
    private var buttonCreateAccount: Button? = null

    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount)

        databaseHelper = DatabaseHelper(this)

        buttonLogin!!.setOnClickListener { _: View? -> loginUser() }

        buttonCreateAccount!!.setOnClickListener { _: View? -> createAccount() }
    }

    private fun loginUser() {
        val username = editUsername!!.getText().toString().trim { it <= ' ' }
        val password = editPassword!!.getText().toString().trim { it <= ' ' }

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            return
        }

        val validUser = databaseHelper!!.checkUser(username, password)

        if (validUser) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            openMainScreen()
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccount() {
        val username = editUsername!!.getText().toString().trim { it <= ' ' }
        val password = editPassword!!.getText().toString().trim { it <= ' ' }

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show()
            return
        }

        val created = databaseHelper!!.addUser(username, password)

        if (created) {
            Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
            openMainScreen()
        } else {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMainScreen() {
        val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
        startActivity(intent)
        finish()
    }
}