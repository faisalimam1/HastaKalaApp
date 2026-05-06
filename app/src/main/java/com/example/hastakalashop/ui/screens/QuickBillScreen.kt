package com.example.hastakalashop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakalashop.data.db.entities.Product
import com.example.hastakalashop.viewmodel.HastaKalaViewModel

val CRAFT_COLORS = listOf("Red", "Blue", "Green", "Yellow", "Orange", "Brown", "Black", "White", "Pink", "Purple")

val COLOR_MAP = mapOf(
    "Red" to Color(0xFFE53935),
    "Blue" to Color(0xFF1E88E5),
    "Green" to Color(0xFF43A047),
    "Yellow" to Color(0xFFFDD835),
    "Orange" to Color(0xFFE07B39),
    "Brown" to Color(0xFF6D4C41),
    "Black" to Color(0xFF212121),
    "White" to Color(0xFFF5F5F5),
    "Pink" to Color(0xFFE91E8C),
    "Purple" to Color(0xFF8E24AA)
)

@Composable
fun QuickBillScreen(viewModel: HastaKalaViewModel) {
    val products by viewModel.products.collectAsState()
    val saleSuccess by viewModel.saleSuccess.collectAsState()

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf("1") }
    var price by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(saleSuccess) {
        if (saleSuccess) {
            showSuccess = true
            selectedProduct = null
            selectedColor = null
            quantity = "1"
            price = ""
            viewModel.resetSaleSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0))
            .padding(16.dp)
    ) {
        Text(
            "Quick Bill",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D3A1A)
        )
        Spacer(Modifier.height(4.dp))
        Text("Tap a product to record a sale", fontSize = 13.sp, color = Color(0xFF9E7060))

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(showSuccess) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Sale saved successfully!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                showSuccess = false
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Select Product", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D3A1A))
        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.height(220.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    isSelected = selectedProduct?.id == product.id,
                    onClick = { selectedProduct = product; selectedColor = null }
                )
            }
        }

        if (selectedProduct != null) {
            Spacer(Modifier.height(16.dp))
            Text("Select Color", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D3A1A))
            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(90.dp)
            ) {
                items(CRAFT_COLORS) { color ->
                    ColorDot(
                        color = color,
                        isSelected = selectedColor == color,
                        onClick = { selectedColor = color }
                    )
                }
            }
        }

        if (selectedProduct != null && selectedColor != null) {
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { if (it.all(Char::isDigit)) quantity = it },
                    label = { Text("Qty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(Modifier.height(12.dp))

            val total = (quantity.toIntOrNull() ?: 0) * (price.toDoubleOrNull() ?: 0.0)
            if (total > 0) {
                Text("Total: ₹${"%.2f".format(total)}", fontWeight = FontWeight.Bold, color = Color(0xFF43A047), fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 0
                    val p = price.toDoubleOrNull() ?: 0.0
                    if (qty > 0 && p > 0) {
                        viewModel.recordSale(selectedProduct!!, selectedColor!!, qty, p)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE07B39))
            ) {
                Text("Save Sale", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .then(if (isSelected) Modifier.border(3.dp, Color(0xFFE07B39), RoundedCornerShape(16.dp)) else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFE0CC) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(product.emoji, fontSize = 36.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(4.dp))
            Text(
                product.name,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5D3A1A),
                maxLines = 2
            )
        }
    }
}

@Composable
fun ColorDot(color: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = COLOR_MAP[color] ?: Color.Gray
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(if (isSelected) Modifier.border(3.dp, Color(0xFF5D3A1A), CircleShape) else Modifier)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) Text("✓", color = if (color == "White" || color == "Yellow") Color.Black else Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
