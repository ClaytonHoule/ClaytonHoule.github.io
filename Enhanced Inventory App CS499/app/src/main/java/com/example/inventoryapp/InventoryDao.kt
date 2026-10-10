package com.example.inventoryapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface InventoryDao {

    // Gets all rows from inventory table and converts them into InventoryItem objects
    @Query("SELECT * FROM inventory")
    suspend fun getAllItems(): List<InventoryItem>

    // Inserts object and returns row ID
    @Insert
    suspend fun insertItem(item: InventoryItem): Long

    // Updates an inventory item and returns number of rows updated
    @Update
    suspend fun updateItem(item: InventoryItem): Int

    // Deletes an inventory item and return
    @Delete
    suspend fun deleteItem(item: InventoryItem): Int
}