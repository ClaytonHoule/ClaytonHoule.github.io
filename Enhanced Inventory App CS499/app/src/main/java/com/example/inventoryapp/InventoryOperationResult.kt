package com.example.inventoryapp

enum class InventoryOperation {
    ADD, UPDATE, DELETE
}

data class InventoryOperationResult(
    val operation: InventoryOperation,
    val success: Boolean
)
