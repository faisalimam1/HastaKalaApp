package com.example.hastakalashop

import android.app.Application
import com.example.hastakalashop.data.db.AppDatabase
import com.example.hastakalashop.data.repository.SaleRepository

class HastaKalaApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy {
        SaleRepository(database.productDao(), database.saleDao(), database.stockDao())
    }
}
