package com.example.inventoryapp

class InventoryRepository(
    private val inventoryDao: InventoryDao) {

    // Retrieves all inventory items through Room Dao
    suspend fun getAllItems(): List<InventoryItem> {
        return inventoryDao.getAllItems()
    }

    // Creates an InventoryItem and inserts through Room Dao
    suspend fun addItem(itemName: String, quantity: Int): Boolean {
        val item = InventoryItem(itemName = itemName, quantity = quantity)
        return inventoryDao.insertItem(item) > 0
    }

    // Updates inventory item through Room Dao
    suspend fun updateItem(item: InventoryItem): Boolean {
        return inventoryDao.updateItem(item) > 0
    }

    // Deletes inventory item through Room Dao
    suspend fun deleteItem(item: InventoryItem): Boolean {
        return inventoryDao.deleteItem(item) > 0
    }
}