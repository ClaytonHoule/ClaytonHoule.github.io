package com.example.inventoryapp

object InventoryAlgorithms {

    // Merges two previously sorted lists into one list ordered
    private fun merge(left: List<InventoryItem>, right: List<InventoryItem>): List<InventoryItem> {
        val mergedList = mutableListOf<InventoryItem>()
        var leftIndex = 0
        var rightIndex = 0

        while (leftIndex < left.size && rightIndex < right.size) {

            if (left[leftIndex].quantity <= right[rightIndex].quantity) {
                mergedList.add(left[leftIndex])
                leftIndex += 1
            }
            else {
                mergedList.add(right[rightIndex])
                rightIndex += 1
            }
        }

        while (leftIndex < left.size) {
            mergedList.add(left[leftIndex])
            leftIndex += 1
        }

        while (rightIndex < right.size) {
            mergedList.add(right[rightIndex])
            rightIndex += 1
        }

        return mergedList
    }

    // Returns inventory item's quantity below LOW_STOCK_THRESHOLD
    // Time complexity: O(n)
    fun filterLowStock(items: List<InventoryItem>, threshold: Int): List<InventoryItem> {
        val lowStockItems = mutableListOf<InventoryItem>()

        for (item in items) {
            if (item.quantity <= threshold) {
                lowStockItems.add(item)
            }
        }

        return lowStockItems
    }

    // Sorts inventory items from lowest to highest quantity using merge sort
    // Time complexity: O(n log n)
    fun mergeSortByQuantity(items: List<InventoryItem>): List<InventoryItem> {

        if (items.size <= 1) {
            return items
        }

        val middle = items.size / 2
        val left = mergeSortByQuantity(items.subList(0, middle))
        val right = mergeSortByQuantity(items.subList(middle, items.size))

        return merge(left, right)
    }


}
