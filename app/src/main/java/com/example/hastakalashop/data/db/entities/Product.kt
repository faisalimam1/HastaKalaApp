package com.example.hastakalashop.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val defaultPrice: Double? = null,
    val isCustom: Boolean = false
)
