package com.example.hastakalashop.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val color: String,
    val quantity: Int,
    val pricePerItem: Double,
    val totalAmount: Double,
    val timestamp: Long = System.currentTimeMillis()
)
