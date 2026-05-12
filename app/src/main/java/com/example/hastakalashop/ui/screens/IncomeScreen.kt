package com.example.hastakalashop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakalashop.data.db.entities.Sale
import com.example.hastakalashop.viewmodel.HastaKalaViewModel
import com.example.hastakalashop.viewmodel.IncomePeriod
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IncomeScreen(viewModel: HastaKalaViewModel) {
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val periodSales by viewModel.periodSales.collectAsState()
    val periodIncome by viewModel.periodIncome.collectAsState()

    var saleToEdit by remember { mutableStateOf<Sale?>(null) }
    var saleToDelete by remember { mutableStateOf<Sale?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0))
            .padding(16.dp)
    ) {
        Text("Income Log", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A))
        Text("Track your earnings", fontSize = 13.sp, color = Color(0xFF9E7060))
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IncomePeriod.entries.forEach { period ->
                val isSelected = selectedPeriod == period
                Button(
                    onClick = { viewModel.setPeriod(period) },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFFE07B39) else Color(0xFFEDE0D4)
                    )
                ) {
                    Text(
                        if (period == IncomePeriod.WEEK) "This Week" else "This Month",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else Color(0xFF5D3A1A),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE07B39)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "₹${"%.2f".format(periodIncome)}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Total ${if (selectedPeriod == IncomePeriod.WEEK) "this week" else "this month"}",
                    color = Color(0xFFFFE0CC),
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(4.dp))
                Text("${periodSales.size} sales recorded", color = Color(0xFFFFE0CC), fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Sales History", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D3A1A), fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        if (periodSales.isEmpty()) {
            Box(
                Modifier.fillMaxWidth().padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💰", fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("No sales in this period.", color = Color(0xFF9E7060))
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(periodSales, key = { it.id }) { sale ->
                    SaleRow(
                        sale = sale,
                        onEdit = { saleToEdit = sale },
                        onDelete = { saleToDelete = sale }
                    )
                }
            }
        }
    }

    // Edit sale dialog
    saleToEdit?.let { sale ->
        EditSaleDialog(
            sale = sale,
            onConfirm = { updatedSale ->
                viewModel.updateSale(updatedSale)
                saleToEdit = null
            },
            onDismiss = { saleToEdit = null }
        )
    }

    // Delete sale confirmation
    saleToDelete?.let { sale ->
        AlertDialog(
            onDismissRequest = { saleToDelete = null },
            title = { Text("Delete Sale?", fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A)) },
            text = {
                Text(
                    "Delete sale of ${if (sale.color.isBlank()) sale.productName else "${sale.productName} — ${sale.color}"} (₹${"%.0f".format(sale.totalAmount)})?",
                    color = Color(0xFF5D3A1A)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSale(sale)
                        saleToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBF360C))
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { saleToDelete = null }) {
                    Text("Cancel", color = Color(0xFF9E7060))
                }
            },
            containerColor = Color(0xFFFFF8F0)
        )
    }
}

@Composable
private fun EditSaleDialog(
    sale: Sale,
    onConfirm: (Sale) -> Unit,
    onDismiss: () -> Unit
) {
    var quantity by remember { mutableStateOf(sale.quantity.toString()) }
    var price by remember { mutableStateOf(
        if (sale.pricePerItem == sale.pricePerItem.toLong().toDouble())
            sale.pricePerItem.toLong().toString()
        else "%.2f".format(sale.pricePerItem)
    ) }
    var inputError by remember { mutableStateOf("") }

    val newTotal = (quantity.toIntOrNull() ?: 0) * (price.toDoubleOrNull() ?: 0.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Sale", fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "${sale.productName} — ${sale.color}",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF9E7060),
                    fontSize = 13.sp
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { v ->
                        if (v.all(Char::isDigit)) { quantity = v; inputError = "" }
                    },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { v ->
                        if (v.isEmpty() || v.toDoubleOrNull() != null) { price = v; inputError = "" }
                    },
                    label = { Text("Price per item (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                if (newTotal > 0) {
                    Text(
                        "New Total: ₹${"%.2f".format(newTotal)}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF43A047),
                        fontSize = 14.sp
                    )
                }
                if (inputError.isNotEmpty()) {
                    Text(inputError, color = Color(0xFFBF360C), fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 0
                    val p = price.toDoubleOrNull() ?: 0.0
                    when {
                        qty <= 0 -> inputError = "Quantity must be at least 1"
                        p <= 0 -> inputError = "Price must be greater than 0"
                        else -> onConfirm(sale.copy(quantity = qty, pricePerItem = p, totalAmount = qty * p))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE07B39))
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = Color(0xFF9E7060)) }
        },
        containerColor = Color(0xFFFFF8F0)
    )
}

@Composable
fun SaleRow(sale: Sale, onEdit: () -> Unit = {}, onDelete: () -> Unit = {}) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    if (sale.color.isBlank()) sale.productName else "${sale.productName} — ${sale.color}",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D3A1A)
                )
                Text("${sale.quantity} pcs × ₹${"%.0f".format(sale.pricePerItem)}", fontSize = 13.sp, color = Color(0xFF9E7060))
                Text(dateFormat.format(Date(sale.timestamp)), fontSize = 11.sp, color = Color(0xFFBCAAA4))
            }
            Text(
                "₹${"%.0f".format(sale.totalAmount)}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF43A047),
                fontSize = 16.sp
            )
            Spacer(Modifier.width(4.dp))
            IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                Text("✏️", fontSize = 16.sp)
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Text("🗑️", fontSize = 16.sp)
            }
        }
    }
}