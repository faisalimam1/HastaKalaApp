package com.example.hastakalashop.data.repository

import com.example.hastakalashop.data.db.dao.BestSellerResult
import com.example.hastakalashop.data.db.dao.ProductDao
import com.example.hastakalashop.data.db.dao.SaleDao
import com.example.hastakalashop.data.db.dao.StockDao
import com.example.hastakalashop.data.db.entities.Product
import com.example.hastakalashop.data.db.entities.Sale
import com.example.hastakalashop.data.db.entities.Stock
import kotlinx.coroutines.flow.Flow

class SaleRepository(
    private val productDao: ProductDao,
    private val saleDao: SaleDao,
    private val stockDao: StockDao
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    val allStock: Flow<List<Stock>> = stockDao.getAllStock()
    val lowStock: Flow<List<Stock>> = stockDao.getLowStock()
    val bestSellers: Flow<List<BestSellerResult>> = saleDao.getBestSellers()

    fun getSalesSince(fromTimestamp: Long): Flow<List<Sale>> = saleDao.getSalesSince(fromTimestamp)
    fun getTotalIncomeSince(fromTimestamp: Long): Flow<Double?> = saleDao.getTotalIncomeSince(fromTimestamp)

    suspend fun recordSale(sale: Sale) {
        saleDao.insertSale(sale)
        val existing = stockDao.getStockByProductAndColor(sale.productId, sale.color)
        if (existing != null) {
            stockDao.decrementStock(sale.productId, sale.color, sale.quantity)
        }
    }

    suspend fun addStock(stock: Stock) {
        val existing = stockDao.getStockByProductAndColor(stock.productId, stock.color)
        if (existing != null) {
            stockDao.updateStock(existing.copy(quantity = existing.quantity + stock.quantity))
        } else {
            stockDao.insertStock(stock)
        }
    }
}
