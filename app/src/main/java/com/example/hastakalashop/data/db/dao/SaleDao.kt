package com.example.hastakalashop.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hastakalashop.data.db.entities.Sale
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {
    @Insert
    suspend fun insertSale(sale: Sale)

    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE timestamp >= :fromTimestamp ORDER BY timestamp DESC")
    fun getSalesSince(fromTimestamp: Long): Flow<List<Sale>>

    @Query("SELECT productName, color, SUM(quantity) as totalQty FROM sales GROUP BY productName, color ORDER BY totalQty DESC")
    fun getBestSellers(): Flow<List<BestSellerResult>>

    @Query("SELECT SUM(totalAmount) FROM sales WHERE timestamp >= :fromTimestamp")
    fun getTotalIncomeSince(fromTimestamp: Long): Flow<Double?>
}

data class BestSellerResult(
    val productName: String,
    val color: String,
    val totalQty: Int
)
