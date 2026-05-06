package com.example.hastakalashop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                items(periodSales) { sale -> SaleRow(sale) }
            }
        }
    }
}

@Composable
fun SaleRow(sale: Sale) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("${sale.productName} — ${sale.color}", fontWeight = FontWeight.Medium, color = Color(0xFF5D3A1A))
                Text("${sale.quantity} pcs × ₹${"%.0f".format(sale.pricePerItem)}", fontSize = 13.sp, color = Color(0xFF9E7060))
                Text(dateFormat.format(Date(sale.timestamp)), fontSize = 11.sp, color = Color(0xFFBCAAA4))
            }
            Text("₹${"%.0f".format(sale.totalAmount)}", fontWeight = FontWeight.Bold, color = Color(0xFF43A047), fontSize = 16.sp)
        }
    }
}
