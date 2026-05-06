package com.example.hastakalashop.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock", indices = [androidx.room.Index(value = ["productId", "color"], unique = true)])
data class Stock(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val color: String,
    val quantity: Int
)
