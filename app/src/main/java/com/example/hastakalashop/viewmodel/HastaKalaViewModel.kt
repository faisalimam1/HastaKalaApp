package com.example.hastakalashop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hastakalashop.data.db.dao.BestSellerResult
import com.example.hastakalashop.data.db.entities.Product
import com.example.hastakalashop.data.db.entities.Sale
import com.example.hastakalashop.data.db.entities.Stock
import com.example.hastakalashop.data.repository.SaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class IncomePeriod { WEEK, MONTH }

class HastaKalaViewModel(private val repository: SaleRepository) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStock: StateFlow<List<Stock>> = repository.allStock
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStock: StateFlow<List<Stock>> = repository.lowStock
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bestSellers: StateFlow<List<BestSellerResult>> = repository.bestSellers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedPeriod = MutableStateFlow(IncomePeriod.WEEK)
    val selectedPeriod: StateFlow<IncomePeriod> = _selectedPeriod

    private val _periodSales = MutableStateFlow<List<Sale>>(emptyList())
    val periodSales: StateFlow<List<Sale>> = _periodSales

    private val _periodIncome = MutableStateFlow(0.0)
    val periodIncome: StateFlow<Double> = _periodIncome

    private val _saleSuccess = MutableStateFlow(false)
    val saleSuccess: StateFlow<Boolean> = _saleSuccess

    init {
        loadPeriodData(IncomePeriod.WEEK)
    }

    fun setPeriod(period: IncomePeriod) {
        _selectedPeriod.value = period
        loadPeriodData(period)
    }

    private fun loadPeriodData(period: IncomePeriod) {
        val cal = Calendar.getInstance()
        when (period) {
            IncomePeriod.WEEK -> cal.add(Calendar.DAY_OF_YEAR, -7)
            IncomePeriod.MONTH -> cal.add(Calendar.MONTH, -1)
        }
        val from = cal.timeInMillis
        viewModelScope.launch {
            repository.getSalesSince(from).collect { _periodSales.value = it }
        }
        viewModelScope.launch {
            repository.getTotalIncomeSince(from).collect { _periodIncome.value = it ?: 0.0 }
        }
    }

    fun recordSale(product: Product, color: String, quantity: Int, pricePerItem: Double) {
        viewModelScope.launch {
            val sale = Sale(
                productId = product.id,
                productName = product.name,
                color = color,
                quantity = quantity,
                pricePerItem = pricePerItem,
                totalAmount = quantity * pricePerItem
            )
            repository.recordSale(sale)
            _saleSuccess.value = true
        }
    }

    fun resetSaleSuccess() { _saleSuccess.value = false }

    fun addStock(product: Product, color: String, quantity: Int) {
        viewModelScope.launch {
            repository.addStock(
                Stock(productId = product.id, productName = product.name, color = color, quantity = quantity)
            )
        }
    }

    fun addCustomProduct(name: String, emoji: String, defaultPrice: Double?) {
        viewModelScope.launch {
            val nextId = (repository.getMaxProductId() ?: 100) + 1
            repository.insertProduct(
                Product(id = nextId, name = name, emoji = emoji, defaultPrice = defaultPrice, isCustom = true)
            )
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch { repository.updateProduct(product) }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch { repository.deleteProduct(product) }
    }

    fun updateSale(sale: Sale) {
        viewModelScope.launch { repository.updateSale(sale) }
    }

    fun deleteSale(sale: Sale) {
        viewModelScope.launch { repository.deleteSale(sale) }
    }
}

class HastaKalaViewModelFactory(private val repository: SaleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HastaKalaViewModel(repository) as T
    }
}
