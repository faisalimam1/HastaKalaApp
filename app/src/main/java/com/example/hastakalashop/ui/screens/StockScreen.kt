package com.example.hastakalashop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hastakalashop.data.db.entities.Product
import com.example.hastakalashop.data.db.entities.Stock
import com.example.hastakalashop.viewmodel.HastaKalaViewModel

@Composable
fun StockScreen(viewModel: HastaKalaViewModel) {
    val allStock by viewModel.allStock.collectAsState()
    val lowStock by viewModel.lowStock.collectAsState()
    val products by viewModel.products.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Stock", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A))
                Text("Inventory overview", fontSize = 13.sp, color = Color(0xFF9E7060))
            }
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE07B39)),
                shape = RoundedCornerShape(12.dp)
            ) { Text("+ Add Stock") }
        }

        Spacer(Modifier.height(16.dp))

        if (lowStock.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF8F00), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Low Stock Alerts", fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    }
                    Spacer(Modifier.height(8.dp))
                    lowStock.forEach { stock ->
                        Text(
                            "• ${stock.productName} (${stock.color}): Only ${stock.quantity} left — Time to make more!",
                            fontSize = 13.sp,
                            color = Color(0xFFBF360C),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }

        if (allStock.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📦", fontSize = 64.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("No stock tracked yet.", fontWeight = FontWeight.Medium, color = Color(0xFF9E7060))
                    Text("Tap '+ Add Stock' to get started.", fontSize = 13.sp, color = Color(0xFFBCAAA4))
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(allStock) { stock -> StockItem(stock) }
            }
        }
    }

    if (showAddDialog) {
        AddStockDialog(
            products = products,
            onDismiss = { showAddDialog = false },
            onConfirm = { product, color, qty ->
                viewModel.addStock(product, color, qty)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun StockItem(stock: Stock) {
    val isLow = stock.quantity < 3
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isLow) Color(0xFFFFF3E0) else Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(stock.productName, fontWeight = FontWeight.Medium, color = Color(0xFF5D3A1A))
                Text(stock.color, fontSize = 13.sp, color = Color(0xFF9E7060))
            }
            if (isLow) Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF8F00), modifier = Modifier.size(18.dp).padding(end = 4.dp))
            Text(
                "${stock.quantity} pcs",
                fontWeight = FontWeight.Bold,
                color = if (isLow) Color(0xFFE65100) else Color(0xFF43A047),
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStockDialog(products: List<Product>, onDismiss: () -> Unit, onConfirm: (Product, String, Int) -> Unit) {
    var selectedProduct by remember { mutableStateOf(products.firstOrNull()) }
    var selectedColor by remember { mutableStateOf(CRAFT_COLORS.first()) }
    var quantity by remember { mutableStateOf("10") }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(20.dp)) {
                Text("Add Stock", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF5D3A1A))
                Spacer(Modifier.height(16.dp))

                Text("Product", fontWeight = FontWeight.Medium, color = Color(0xFF5D3A1A))
                Spacer(Modifier.height(4.dp))
                var expandedProduct by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expandedProduct, onExpandedChange = { expandedProduct = it }) {
                    OutlinedTextField(
                        value = selectedProduct?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedProduct) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(expanded = expandedProduct, onDismissRequest = { expandedProduct = false }) {
                        products.forEach { DropdownMenuItem(text = { Text("${it.emoji} ${it.name}") }, onClick = { selectedProduct = it; expandedProduct = false }) }
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text("Color", fontWeight = FontWeight.Medium, color = Color(0xFF5D3A1A))
                Spacer(Modifier.height(4.dp))
                var expandedColor by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expandedColor, onExpandedChange = { expandedColor = it }) {
                    OutlinedTextField(
                        value = selectedColor,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedColor) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(expanded = expandedColor, onDismissRequest = { expandedColor = false }) {
                        CRAFT_COLORS.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { selectedColor = it; expandedColor = false }) }
                    }
                }

                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { if (it.all(Char::isDigit)) quantity = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)) { Text("Cancel") }
                    Button(
                        onClick = {
                            val qty = quantity.toIntOrNull() ?: 0
                            if (qty > 0 && selectedProduct != null) onConfirm(selectedProduct!!, selectedColor, qty)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE07B39))
                    ) { Text("Add") }
                }
            }
        }
    }
}
