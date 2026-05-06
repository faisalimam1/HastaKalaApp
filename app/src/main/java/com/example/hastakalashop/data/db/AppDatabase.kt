package com.example.hastakalashop.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hastakalashop.data.db.dao.ProductDao
import com.example.hastakalashop.data.db.dao.SaleDao
import com.example.hastakalashop.data.db.dao.StockDao
import com.example.hastakalashop.data.db.entities.Product
import com.example.hastakalashop.data.db.entities.Sale
import com.example.hastakalashop.data.db.entities.Stock

@Database(entities = [Product::class, Sale::class, Stock::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun stockDao(): StockDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "hastakala_db")
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            defaultProducts().forEach { p ->
                                db.execSQL(
                                    "INSERT OR IGNORE INTO products (id, name, emoji) VALUES (?, ?, ?)",
                                    arrayOf<Any>(p.id, p.name, p.emoji)
                                )
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private fun defaultProducts() = listOf(
            Product(1,  "Banana Fiber Bag",     "🛍️"),
            Product(2,  "Keychain",              "🔑"),
            Product(3,  "Pouch",                 "👜"),
            Product(4,  "Basket",                "🧺"),
            Product(5,  "Coaster",               "☕"),
            Product(6,  "Wall Hanging",          "🖼️"),
            Product(7,  "Clay Pot",              "🏺"),
            Product(8,  "Jute Rope",             "🪢"),
            Product(9,  "Woven Mat",             "🧵"),
            Product(10, "Dried Flower Wreath",   "🌸"),
            Product(11, "Herbal Soap",           "🧼"),
            Product(12, "Beeswax Candle",        "🕯️"),
            Product(13, "Handmade Pickle",       "🫙"),
            Product(14, "Wooden Spoon",          "🥄"),
            Product(15, "Terracotta Bowl",       "🍵"),
            Product(16, "Natural Broom",         "🧹"),
            Product(17, "Handwoven Shawl",       "🧣"),
            Product(18, "Bamboo Lamp",           "🪔")
        )
    }
}
