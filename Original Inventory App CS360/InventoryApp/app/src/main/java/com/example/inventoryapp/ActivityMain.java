package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {

    private EditText editItemName;
    private EditText editQuantity;

    private DatabaseHelper databaseHelper;
    private ArrayList<InventoryItem> inventoryList;
    private InventoryAdapter inventoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editItemName = findViewById(R.id.editItemName);
        editQuantity = findViewById(R.id.editQuantity);
        Button buttonAddItem = findViewById(R.id.buttonAddItem);
        Button buttonSmsSettings = findViewById(R.id.buttonSmsSettings);
        RecyclerView recyclerInventory = findViewById(R.id.recyclerInventory);

        databaseHelper = new DatabaseHelper(this);
        inventoryList = new ArrayList<>();

        inventoryAdapter = new InventoryAdapter(inventoryList, new InventoryAdapter.OnItemActionListener() {

            @Override
            public void onDeleteClick(InventoryItem item) {
                showDeleteConfirmationDialog(item);
            }

            @Override
            public void onItemClick(InventoryItem item) {
                showUpdateDialog(item);
            }

            @Override
            public void onPlusClick(InventoryItem item) {
                updateInventoryItem(item, item.getQuantity() + 1);
            }

            @Override
            public void onMinusClick(InventoryItem item) {
                if (item.getQuantity() > 0) {
                    updateInventoryItem(item, item.getQuantity() - 1);
                }
            }
        });

        recyclerInventory.setLayoutManager(new LinearLayoutManager(this));
        recyclerInventory.setAdapter(inventoryAdapter);

        buttonAddItem.setOnClickListener(v -> addInventoryItem());

        buttonSmsSettings.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityMain.this, ActivitySMS.class);
            startActivity(intent);
        });

        loadInventoryItems();
    }

    private void addInventoryItem() {
        String itemName = editItemName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        if (itemName.isEmpty() || quantityText.isEmpty()) {
            Toast.makeText(this, "Please enter an item name and quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityText);

        boolean inserted = databaseHelper.addInventoryItem(itemName, quantity);

        if (inserted) {

            if (quantity <= 5) {
                sendLowInventorySms(itemName, quantity);
            }
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
            editItemName.setText("");
            editQuantity.setText("");
            loadInventoryItems();
        } else {
            Toast.makeText(this, "Item was not added", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInventoryItems() {
        inventoryList.clear();

        Cursor cursor = databaseHelper.getAllInventoryItems();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String itemName = cursor.getString(1);
                    int quantity = cursor.getInt(2);

                    inventoryList.add(new InventoryItem(id, itemName, quantity));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        inventoryAdapter.notifyDataSetChanged();
    }

    private void deleteInventoryItem(InventoryItem item) {
        boolean deleted = databaseHelper.deleteInventoryItem(item.getId());

        if (deleted) {
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            loadInventoryItems();
        } else {
            Toast.makeText(this, "Item was not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog(InventoryItem item) {
        new AlertDialog.Builder(this).setTitle("Delete Item").setMessage("Are you sure you want to delete " + item.getItemName() + "?").setPositiveButton("Delete", (dialog, which) -> deleteInventoryItem(item)).setNegativeButton("Cancel", null).show();
    }

    private void showUpdateDialog(InventoryItem item) {
        EditText inputQuantity = new EditText(this);
        inputQuantity.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        inputQuantity.setHint("New quantity");
        inputQuantity.setText(String.valueOf(item.getQuantity()));

        new AlertDialog.Builder(this).setTitle("Update Quantity").setMessage("Update quantity for " + item.getItemName()).setView(inputQuantity).setPositiveButton("Update", (dialog, which) -> {
                    String quantityText = inputQuantity.getText().toString().trim();

                    if (!quantityText.isEmpty()) {
                        int newQuantity = Integer.parseInt(quantityText);
                        updateInventoryItem(item, newQuantity);
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    private void updateInventoryItem(InventoryItem item, int newQuantity) {
        boolean updated = databaseHelper.updateInventoryItem(item.getId(), newQuantity);

        if (updated) {

            if (newQuantity <= 5) {sendLowInventorySms(item.getItemName(), newQuantity);
            }
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
            loadInventoryItems();
        } else {
            Toast.makeText(this, "Item was not updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLowInventorySms(String itemName, int quantity) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();

            String phoneNumber = "";
            String message = "Inventory Alert: " + itemName + " is running low. Current quantity: " + quantity;
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Low inventory SMS alert sent", Toast.LENGTH_SHORT).show();
        }
    }
}