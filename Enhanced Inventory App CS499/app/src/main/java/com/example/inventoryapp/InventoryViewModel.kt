package com.example.inventoryapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class InventoryViewModel (
    private val repository: InventoryRepository
) : ViewModel() {
    private val _inventoryItems = MutableLiveData<List<InventoryItem>>()
    private val _operationResult = MutableLiveData<InventoryOperationResult>()
    // Default threshold
    private var lowStockThreshold = 5
    val inventoryItems: LiveData<List<InventoryItem>>
        get() = _inventoryItems

    val operationResult: LiveData<InventoryOperationResult>
        get() = _operationResult

    // Get and load items from the repository, updates UI state
    fun loadInventory() {
        viewModelScope.launch {
            _inventoryItems.value = repository.getAllItems()
        }
    }

    // Sorts all items in repository by quantity and updates inventory state
    fun sortInventoryByQuantity() {
        viewModelScope.launch {
            val items = repository.getAllItems()

            val sortedItems = InventoryAlgorithms.mergeSortByQuantity(items)

            _inventoryItems.value = sortedItems
        }

    }

    // Setter and getter for threshold
    fun setLowStockThreshold(threshold: Int): Boolean {
        if (threshold < 0) {
            return false
        }

        lowStockThreshold = threshold
        return true
    }
    fun getLowStockThreshold(): Int {
        return lowStockThreshold
    }

    // Filters inventory using current low stock threshold
    // Sorts results by quantity and updates inventory state
    fun showLowStock() {
        viewModelScope.launch {
            val items = repository.getAllItems()

            val lowStockItems = InventoryAlgorithms.filterLowStock(items, lowStockThreshold)

            _inventoryItems.value = InventoryAlgorithms.mergeSortByQuantity(lowStockItems)
        }
    }

    // Determines if quantity meets current low stock threshold
    fun isLowStock(quantity: Int): Boolean {
        return quantity <= lowStockThreshold
    }

    // Validates and adds an inventory item, refreshes inventory
    fun addItem(itemName: String, quantity: Int) {
        if (itemName.isBlank() || quantity < 0) {
            _operationResult.value = InventoryOperationResult(
                InventoryOperation.ADD, false
            )
            return
        }

        viewModelScope.launch {
            val inserted = repository.addItem(itemName, quantity)

            _operationResult.value = InventoryOperationResult(
                InventoryOperation.ADD, inserted
            )

            if (inserted) {
                loadInventory()
            }
        }
    }

    // Deletes an inventory item, refreshes inventory
    fun deleteItem(item: InventoryItem) {
        viewModelScope.launch {
            val deleted = repository.deleteItem(item)

            _operationResult.value = InventoryOperationResult(
                InventoryOperation.DELETE, deleted
            )

            if (deleted) {
                loadInventory()
            }
        }
    }

    // Validates and updates an item's quantity, refreshes inventory
    fun updateQuantity(item: InventoryItem, newQuantity: Int) {
        if (newQuantity < 0) {
            _operationResult.value = InventoryOperationResult(
                InventoryOperation.UPDATE, false
            )
            return
        }

        viewModelScope.launch {
            val updatedItem = item.copy(quantity = newQuantity)
            val updated = repository.updateItem(updatedItem)

            _operationResult.value = InventoryOperationResult(
                InventoryOperation.UPDATE, updated
            )

            if (updated) {
                loadInventory()
            }
        }
    }
}