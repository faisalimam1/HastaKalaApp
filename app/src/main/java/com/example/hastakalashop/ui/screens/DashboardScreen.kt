package com.example.hastakalashop.ui.screens

import android.graphics.Typeface
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hastakalashop.viewmodel.HastaKalaViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

val CHART_COLORS = listOf(
    0xFFE53935.toInt(), 0xFF1E88E5.toInt(), 0xFF43A047.toInt(),
    0xFFFDD835.toInt(), 0xFFE07B39.toInt(), 0xFF6D4C41.toInt(),
    0xFF8E24AA.toInt(), 0xFFE91E8C.toInt(), 0xFF00ACC1.toInt(), 0xFF757575.toInt()
)

@Composable
fun DashboardScreen(viewModel: HastaKalaViewModel) {
    val bestSellers by viewModel.bestSellers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0))
            .padding(16.dp)
    ) {
        Text("Best Sellers", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A))
        Text("Sales breakdown by product & color", fontSize = 13.sp, color = Color(0xFF9E7060))
        Spacer(Modifier.height(16.dp))

        if (bestSellers.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📊", fontSize = 64.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("No sales yet.", fontWeight = FontWeight.Medium, color = Color(0xFF9E7060))
                    Text("Record your first sale to see analytics.", fontSize = 13.sp, color = Color(0xFFBCAAA4))
                }
            }
        } else {
            val entries = bestSellers.take(8).map { PieEntry(it.totalQty.toFloat(), "${it.productName}\n${it.color}") }
            val total = bestSellers.sumOf { it.totalQty }

            AndroidView(
                factory = { ctx ->
                    PieChart(ctx).apply {
                        description.isEnabled = false
                        isDrawHoleEnabled = true
                        holeRadius = 40f
                        setHoleColor(android.graphics.Color.parseColor("#FFF8F0"))
                        setUsePercentValues(true)
                        setEntryLabelTextSize(11f)
                        setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
                        legend.isEnabled = false
                        setDrawCenterText(true)
                        centerText = "Total\n$total sold"
                        setCenterTextSize(14f)
                        setCenterTextTypeface(Typeface.DEFAULT_BOLD)
                        animateY(800)
                    }
                },
                update = { chart ->
                    val dataSet = PieDataSet(entries, "Sales").apply {
                        colors = CHART_COLORS.take(entries.size)
                        sliceSpace = 3f
                        selectionShift = 8f
                        valueTextSize = 11f
                        valueTextColor = android.graphics.Color.WHITE
                        valueTypeface = Typeface.DEFAULT_BOLD
                    }
                    chart.data = PieData(dataSet).apply { setValueFormatter(PercentFormatter(chart)) }
                    chart.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )

            Spacer(Modifier.height(16.dp))
            Text("Breakdown", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D3A1A), fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bestSellers) { item ->
                    val pct = if (total > 0) item.totalQty * 100 / total else 0
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("${item.productName} — ${item.color}", fontWeight = FontWeight.Medium, color = Color(0xFF5D3A1A))
                                Text("${item.totalQty} units sold", fontSize = 13.sp, color = Color(0xFF9E7060))
                            }
                            Text("$pct%", fontWeight = FontWeight.Bold, color = Color(0xFFE07B39), fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}
