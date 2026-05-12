package com.example.hastakalashop.ui.screens

import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hastakalashop.data.db.dao.BestSellerResult
import com.example.hastakalashop.viewmodel.HastaKalaViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

val CHART_COLORS = listOf(
    0xFFE53935.toInt(), 0xFF1E88E5.toInt(), 0xFF43A047.toInt(),
    0xFFFDD835.toInt(), 0xFFE07B39.toInt(), 0xFF6D4C41.toInt(),
    0xFF8E24AA.toInt(), 0xFFE91E8C.toInt(), 0xFF00ACC1.toInt(), 0xFF757575.toInt()
)

@Composable
fun DashboardScreen(viewModel: HastaKalaViewModel) {
    val bestSellers by viewModel.bestSellers.collectAsState()
    val selectedSlice = remember { mutableStateOf<Pair<Int, BestSellerResult>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0))
            .padding(16.dp)
    ) {
        Text("Best Sellers", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A))
        Text("Tap a chart slice for full details", fontSize = 13.sp, color = Color(0xFF9E7060))
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
            val topSellers = bestSellers.take(8)
            val entries = topSellers.map { PieEntry(it.totalQty.toFloat(), it.productName) }
            val total = bestSellers.sumOf { it.totalQty }

            AndroidView(
                factory = { ctx ->
                    PieChart(ctx).apply {
                        description.isEnabled = false
                        isDrawHoleEnabled = true
                        holeRadius = 40f
                        setHoleColor(android.graphics.Color.parseColor("#FFF8F0"))
                        setUsePercentValues(true)
                        setEntryLabelTextSize(10f)
                        setEntryLabelTypeface(Typeface.DEFAULT_BOLD)
                        legend.isEnabled = false
                        setDrawCenterText(true)
                        setCenterTextSize(13f)
                        setCenterTextTypeface(Typeface.DEFAULT_BOLD)
                        animateY(800)
                    }
                },
                update = { chart ->
                    chart.centerText = "Total\n$total sold"

                    val dataSet = PieDataSet(entries, "Sales").apply {
                        colors = CHART_COLORS.take(entries.size)
                        sliceSpace = 3f
                        selectionShift = 12f
                        valueTextSize = 11f
                        valueTextColor = android.graphics.Color.WHITE
                        valueTypeface = Typeface.DEFAULT_BOLD
                    }
                    chart.data = PieData(dataSet).apply { setValueFormatter(PercentFormatter(chart)) }
                    chart.invalidate()

                    chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            val index = h?.x?.toInt() ?: return
                            val item = topSellers.getOrNull(index) ?: return
                            selectedSlice.value = Pair(index, item)
                        }
                        override fun onNothingSelected() {
                            selectedSlice.value = null
                        }
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )

            AnimatedVisibility(
                visible = selectedSlice.value != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                selectedSlice.value?.let { (idx, item) ->
                    val sliceColor = Color(CHART_COLORS[idx % CHART_COLORS.size])
                    val pct = if (total > 0) item.totalQty * 100f / total else 0f
                    val rank = topSellers.indexOfFirst {
                        it.productName == item.productName && it.color == item.color
                    } + 1

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = sliceColor.copy(alpha = 0.08f)),
                        border = BorderStroke(2.dp, sliceColor),
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp)
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(sliceColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("#$rank", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.productName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF5D3A1A))
                                if (item.color.isNotBlank()) {
                                    Text("Color: ${item.color}", fontSize = 13.sp, color = Color(0xFF6B4C2A))
                                }
                                Text("${item.totalQty} units  •  ${"%.1f".format(pct)}% of all sales", fontSize = 13.sp, color = Color(0xFF9E7060))
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${"%.1f".format(pct)}%", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = sliceColor)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Breakdown", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D3A1A), fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bestSellers) { item ->
                    val pct = if (total > 0) item.totalQty * 100 / total else 0
                    val colorLabel = if (item.color.isBlank()) item.productName else "${item.productName} — ${item.color}"
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
                                Text(colorLabel, fontWeight = FontWeight.Medium, color = Color(0xFF5D3A1A))
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