package com.example.inventoryapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InventoryDaoTest {

    private lateinit var database: InventoryDatabase
    private lateinit var inventoryDao: InventoryDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            InventoryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        inventoryDao = database.inventoryDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertItem_andRetrieveItem() = runBlocking {
        val item = InventoryItem(
            itemName = "Laptop",
            quantity = 5
        )

        inventoryDao.insertItem(item)

        val items = inventoryDao.getAllItems()

        assertEquals(1, items.size)
        assertEquals("Laptop", items[0].itemName)
        assertEquals(5, items[0].quantity)
    }

    @Test
    fun updateItem_changesQuantity() = runBlocking {
        val item = InventoryItem(
            itemName = "Monitor",
            quantity = 3
        )

        val id = inventoryDao.insertItem(item).toInt()

        val updatedItem = InventoryItem(
            id = id,
            itemName = "Monitor",
            quantity = 8
        )

        val rowsUpdated = inventoryDao.updateItem(updatedItem)
        val items = inventoryDao.getAllItems()

        assertEquals(1, rowsUpdated)
        assertEquals(8, items[0].quantity)
    }

    @Test
    fun deleteItem_removesItem() = runBlocking {
        val item = InventoryItem(
            itemName = "Mouse",
            quantity = 4
        )

        val id = inventoryDao.insertItem(item).toInt()

        val storedItem = InventoryItem(
            id = id,
            itemName = "Mouse",
            quantity = 4
        )

        val rowsDeleted = inventoryDao.deleteItem(storedItem)
        val items = inventoryDao.getAllItems()

        assertEquals(1, rowsDeleted)
        assertTrue(items.isEmpty())
    }
}