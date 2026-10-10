package com.example.inventoryapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [InventoryItem::class],
    version = 1,
    exportSchema = false
)

abstract class InventoryDatabase : RoomDatabase() {

    // Provides access to database operations
    abstract fun inventoryDao(): InventoryDao

    companion object {

        // Holds database instance so only one instance is used
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        // Returns an existing database instance or creates one
        fun getDatabase(context: Context): InventoryDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "inventory_database").build()

                INSTANCE = instance
                instance
            }
        }
    }
}