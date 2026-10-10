package com.example.inventoryapp

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.InventoryAdapter.OnItemActionListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActivityMain : AppCompatActivity() {

    private lateinit var editItemName: EditText
    private lateinit var editQuantity: EditText
    private lateinit var inventoryAdapter: InventoryAdapter
    private lateinit var inventoryViewModel: InventoryViewModel

    private val inventoryList = ArrayList<InventoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editItemName = findViewById(R.id.editItemName)
        editQuantity = findViewById(R.id.editQuantity)

        val buttonAddItem = findViewById<Button>(R.id.buttonAddItem)
        val buttonSmsSettings = findViewById<Button>(R.id.buttonSmsSettings)
        val recyclerInventory = findViewById<RecyclerView>(R.id.recyclerInventory)
        val buttonAllItems = findViewById<Button>(R.id.buttonAllItems)
        val buttonSortQuantity = findViewById<Button>(R.id.buttonSortQuantity)
        val buttonLowStock = findViewById<Button>(R.id.buttonLowStock)
        val buttonThreshold = findViewById<Button>(R.id.buttonThreshold)

        // 1. Initialize Database
        val database = InventoryDatabase.getDatabase(this)
        val inventoryDao = database.inventoryDao()

        // 2. Create repository with Dao
        val repository = InventoryRepository(inventoryDao)

        // 3. Give repository to ViewModel
        inventoryViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return InventoryViewModel(repository) as T
            }

        }
        )[InventoryViewModel::class.java]

        inventoryAdapter = InventoryAdapter(inventoryList, object : OnItemActionListener {
            override fun onDeleteClick(item: InventoryItem) {
                showDeleteConfirmationDialog(item)
            }

            override fun onItemClick(item: InventoryItem) {
                showUpdateDialog(item)
            }

            override fun onPlusClick(item: InventoryItem) {
                updateInventoryItem(item, item.quantity + 1)
            }

            override fun onMinusClick(item: InventoryItem) {
                if (item.quantity > 0) {
                    updateInventoryItem(item, item.quantity - 1)
                }
            }
        })

        recyclerInventory.layoutManager = LinearLayoutManager(this)
        recyclerInventory.adapter = inventoryAdapter

        inventoryViewModel.inventoryItems.observe(this) {
            items -> inventoryList.clear()
            inventoryList.addAll(items)
            inventoryAdapter.notifyDataSetChanged()
        }
        inventoryViewModel.operationResult.observe(this) { result ->
            when (result.operation) {

                InventoryOperation.ADD -> {
                    if (result.success) {
                        Toast.makeText(
                            this, "Item added", Toast.LENGTH_SHORT).show()

                        editItemName.setText("")
                        editQuantity.setText("")
                    } else {
                        Toast.makeText(this, "Item was not added", Toast.LENGTH_SHORT).show()
                    }
                }

                InventoryOperation.UPDATE -> {
                    if (result.success) {
                        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Item was not updated", Toast.LENGTH_SHORT).show()
                    }
                }

                InventoryOperation.DELETE -> {
                    if (result.success) {
                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Item was not deleted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        buttonAddItem.setOnClickListener {
            addInventoryItem()
        }

        buttonAllItems.setOnClickListener {
            inventoryViewModel.loadInventory()
        }

        buttonSortQuantity.setOnClickListener {
            inventoryViewModel.sortInventoryByQuantity()
        }

        buttonLowStock.setOnClickListener {
            inventoryViewModel.showLowStock()
        }

        buttonSmsSettings.setOnClickListener{
            val intent = Intent(this, ActivitySMS::class.java)
            startActivity(intent)
        }

        buttonThreshold.setOnClickListener {
            showThresholdDialog()
        }

        inventoryViewModel.loadInventory()
    }

    private fun addInventoryItem() {
        val itemName = editItemName.text.toString().trim()
        val quantityText = editQuantity.text.toString().trim()

        if (itemName.isBlank() || quantityText.isBlank()) {
            Toast.makeText(this, "Please enter an item name and quantity", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val quantity = quantityText.toIntOrNull()

        if (quantity == null || quantity < 0) {
            Toast.makeText(this, "Enter a valid quantity", Toast.LENGTH_SHORT).show()
            return
        }

        inventoryViewModel.addItem(itemName, quantity)
    }


    private fun deleteInventoryItem(item: InventoryItem) {
        inventoryViewModel.deleteItem(item)
    }

    private fun showDeleteConfirmationDialog(item: InventoryItem) {
        AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete ${item.itemName}?")
            .setPositiveButton("Delete") { _, _ ->
                deleteInventoryItem(item)
            }
                .setNegativeButton("Cancel", null)
                .show()
    }

    private fun showUpdateDialog(item: InventoryItem) {
        val inputQuantity = EditText(this)
        inputQuantity.setInputType(InputType.TYPE_CLASS_NUMBER)
        inputQuantity.setHint("New quantity")
        inputQuantity.setText(item.quantity.toString())

        AlertDialog.Builder(this).setTitle("Update Quantity")
            .setMessage("Update quantity for ${item.itemName}").setView(inputQuantity)
            .setPositiveButton(
                "Update") { _, _ ->
                    val quantityText = inputQuantity.text.toString().trim()
                    val newQuantity = quantityText.toIntOrNull()

                    if (newQuantity == null || newQuantity < 0) {
                            Toast.makeText(this, "Enter a valid quantity", Toast.LENGTH_SHORT).show()
                        } else {
                            updateInventoryItem(item, newQuantity)
                    }
                }.setNegativeButton("Cancel", null).show()
    }

    private fun showThresholdDialog() {
        val inputThreshold = EditText(this)

        inputThreshold.inputType = InputType.TYPE_CLASS_NUMBER
        inputThreshold.hint = "Low stock threshold"

        inputThreshold.setText(
            inventoryViewModel.getLowStockThreshold().toString()
        )

        AlertDialog.Builder(this)
            .setTitle("Low Stock Threshold")
            .setMessage("Enter your threshold for low stock items.")
            .setView(inputThreshold)
            .setPositiveButton("Save") { _, _ ->
                val threshold = inputThreshold.text.toString().trim().toIntOrNull()

                if (threshold == null || threshold < 0) {
                    Toast.makeText(
                        this,
                        "Enter a valid threshold",
                        Toast.LENGTH_SHORT).show()
                } else {
                    inventoryViewModel.setLowStockThreshold(threshold)

                    Toast.makeText(
                        this,
                        "Low stock threshold set to $threshold",
                        Toast.LENGTH_SHORT).show()
                }
            }.setNegativeButton("Cancel", null).show()
    }

    private fun updateInventoryItem(
        item: InventoryItem,
        newQuantity: Int
    ) {
        inventoryViewModel.updateQuantity(item, newQuantity)
    }

    private fun sendLowInventorySms(itemName: String, quantity: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val smsManager = SmsManager.getDefault()

            val phoneNumber = ""
            val message =
                "Inventory Alert: $itemName is running low. Current quantity: $quantity"
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "Low inventory SMS alert sent", Toast.LENGTH_SHORT).show()
        }
    }
}