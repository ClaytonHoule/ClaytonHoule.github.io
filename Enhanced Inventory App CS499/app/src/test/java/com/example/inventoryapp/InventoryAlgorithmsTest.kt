package com.example.inventoryapp

import org.junit.Test
import org.junit.Assert.assertEquals

class InventoryAlgorithmsTest {

    // Verifies quantities below low stock threshold are returned in their original order
    @Test
    fun filterLowStock_returnsOnlyLowStockItems() {

        val items = listOf(
            InventoryItem(1, "TV Set", 10),
            InventoryItem(2, "Laptop", 5),
            InventoryItem(3, "Laptop Charger", 1),
            InventoryItem(4, "USB Wireless Mouse", 4),
            InventoryItem(5, "Wireless Noise Cancelling Headphones", 5)
        )

        val result = InventoryAlgorithms.filterLowStock(items, 5)

        assertEquals(4, result.size)
        assertEquals("Laptop", result[0].itemName)
        assertEquals("Laptop Charger", result[1].itemName)
        assertEquals("USB Wireless Mouse", result[2].itemName)
    }

    // Boundary test verifying 5 qualifies as low stock while 6 is not
    @Test
    fun filterLowStock_includesThresholdButExcludesAboveThreshold() {

        val items = listOf(
            InventoryItem(1, "Monitor", 6),
            InventoryItem(2, "HDMI Cable", 5),
            InventoryItem(3, "HDMI Adapter", 0)
        )

        val result = InventoryAlgorithms.filterLowStock(items, 5)

        assertEquals(2, result.size)
        assertEquals("HDMI Cable", result[0].itemName)
        assertEquals("HDMI Adapter", result[1].itemName)
    }

    // Verifies empty list does not fail and is returned empty
    @Test
    fun filterLowStock_emptyListReturnsEmptyList() {
        val result = InventoryAlgorithms.filterLowStock(emptyList(), 5)

        assertEquals(0, result.size)
    }

    // Verifies filterLowStock adheres to user set threshold
    @Test
    fun filterLowStock_usesProvidedThreshold() {
        val items = listOf(
            InventoryItem(1, "Monitor", 3),
            InventoryItem(2, "HDMI Cable", 5),
            InventoryItem(3, "HDMI Adapter", 10)
        )

        val result = InventoryAlgorithms.filterLowStock(items, 7)

        assertEquals(2, result.size)
        assertEquals("Monitor", result[0].itemName)
        assertEquals("HDMI Cable", result[1].itemName)
    }

    // Verifies that list is sorted by quantity lowest to highest without fail
    @Test
    fun mergeSortByQuantity_sortsLowestToHighest() {


        val items = listOf(
            InventoryItem(1, "TV Set", 10),
            InventoryItem(2, "Laptop", 5),
            InventoryItem(3, "Laptop Charger", 1),
            InventoryItem(4, "USB Wireless Mouse", 4)
        )

        val result = InventoryAlgorithms.mergeSortByQuantity(items)

        assertEquals(1, result[0].quantity)
        assertEquals(4, result[1].quantity)
        assertEquals(5, result[2].quantity)
        assertEquals(10, result[3].quantity)
    }

    // Verifies item order when called
    @Test
    fun mergeSortByQuantity_preservesEqualQuantityOrder() {

        val items = listOf(
            InventoryItem(1, "TV Set", 10),
            InventoryItem(2, "Laptop", 5),
            InventoryItem(3, "Laptop Charger", 1),
            InventoryItem(4, "USB Wireless Mouse", 4)
        )

        val result = InventoryAlgorithms.mergeSortByQuantity(items)

        assertEquals("Laptop Charger",result[0].itemName)
        assertEquals("USB Wireless Mouse",result[1].itemName)
        assertEquals("Laptop",result[2].itemName)
        assertEquals("TV Set",result[3].itemName)
    }
}