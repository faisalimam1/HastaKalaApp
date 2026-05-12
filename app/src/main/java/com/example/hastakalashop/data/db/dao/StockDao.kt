package com.example.hastakalashop.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hastakalashop.data.db.entities.Stock
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStock(stock: Stock)

    @Update
    suspend fun updateStock(stock: Stock)

    @Query("SELECT * FROM stock ORDER BY quantity ASC")
    fun getAllStock(): Flow<List<Stock>>

    @Query("SELECT * FROM stock WHERE quantity < 3 ORDER BY quantity ASC")
    fun getLowStock(): Flow<List<Stock>>

    @Query("SELECT * FROM stock WHERE productId = :productId AND color = :color LIMIT 1")
    suspend fun getStockByProductAndColor(productId: Int, color: String): Stock?

    @Query("UPDATE stock SET quantity = quantity - :sold WHERE productId = :productId AND color = :color")
    suspend fun decrementStock(productId: Int, color: String, sold: Int)

    @Query("DELETE FROM stock WHERE productId = :productId")
    suspend fun deleteStockByProduct(productId: Int)
}
