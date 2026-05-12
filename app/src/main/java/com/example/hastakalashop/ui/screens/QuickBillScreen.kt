package com.example.hastakalashop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickBillScreen(viewModel: HastaKalaViewModel) {
    val products by viewModel.products.collectAsState()
    val saleSuccess by viewModel.saleSuccess.collectAsState()

    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf("1") }
    var price by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var inputError by remember { mutableStateOf("") }

    // Product management state
    var productForOptions by remember { mutableStateOf<Product?>(null) }
    var productToEdit by remember { mutableStateOf<Product?>(null) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }
    var showAddProductDialog by remember { mutableStateOf(false) }

    // Large order confirmation
    var pendingSaleData by remember { mutableStateOf<Triple<Int, Double, Double>?>(null) }

    LaunchedEffect(saleSuccess) {
        if (saleSuccess) {
            showSuccess = true
            selectedProduct = null
            selectedColor = null
            quantity = "1"
            price = ""
            inputError = ""
            viewModel.resetSaleSuccess()
        }
    }

    // Pre-fill price from defaultPrice when product is selected
    LaunchedEffect(selectedProduct) {
        selectedColor = null
        quantity = "1"
        val dp = selectedProduct?.defaultPrice
        price = if (dp != null && dp > 0) {
            if (dp == dp.toLong().toDouble()) dp.toLong().toString() else "%.2f".format(dp)
        } else ""
        inputError = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Quick Bill", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A))
                Text("Tap a product to record a sale", fontSize = 13.sp, color = Color(0xFF9E7060))
            }
            OutlinedButton(
                onClick = { showAddProductDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE07B39)),
                border = BorderStroke(1.5.dp, Color(0xFFE07B39))
            ) {
                Text("+ Add Item", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

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
        Text(
            "Select Product  •  Long-press to edit",
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF5D3A1A),
            fontSize = 13.sp
        )
        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    isSelected = selectedProduct?.id == product.id,
                    onClick = { selectedProduct = product },
                    onLongClick = { productForOptions = product }
                )
            }
        }

        if (selectedProduct != null) {
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Select Color", fontWeight = FontWeight.SemiBold, color = Color(0xFF5D3A1A))
                TextButton(
                    onClick = { selectedColor = "" },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("No Color →", fontSize = 12.sp, color = Color(0xFF9E7060))
                }
            }
            Spacer(Modifier.height(4.dp))

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

            if (selectedColor != null) {
                Spacer(Modifier.height(6.dp))
                if (selectedColor!!.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(COLOR_MAP[selectedColor] ?: Color.Gray)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            selectedColor!!,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5D3A1A)
                        )
                    }
                } else {
                    Text("No color — any variant", fontSize = 13.sp, color = Color(0xFF9E7060))
                }
            }
        }

        if (selectedProduct != null && selectedColor != null) {
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { v ->
                        if (v.all(Char::isDigit)) {
                            quantity = v
                            inputError = ""
                        }
                    },
                    label = { Text("Qty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { v ->
                        if (v.isEmpty() || v.toDoubleOrNull() != null) {
                            price = v
                            inputError = ""
                        }
                    },
                    label = { Text("Price (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (inputError.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(inputError, color = Color(0xFFBF360C), fontSize = 12.sp)
            }

            Spacer(Modifier.height(8.dp))

            val total = (quantity.toIntOrNull() ?: 0) * (price.toDoubleOrNull() ?: 0.0)
            if (total > 0) {
                Text(
                    "Total: ₹${"%.2f".format(total)}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF43A047),
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 0
                    val p = price.toDoubleOrNull() ?: 0.0
                    when {
                        qty <= 0 -> inputError = "Quantity must be at least 1"
                        p <= 0 -> inputError = "Price must be greater than 0"
                        total > 10000 -> pendingSaleData = Triple(qty, p, total)
                        else -> viewModel.recordSale(selectedProduct!!, selectedColor!!, qty, p)
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

    // Large order confirmation dialog
    pendingSaleData?.let { (qty, p, total) ->
        AlertDialog(
            onDismissRequest = { pendingSaleData = null },
            title = { Text("Large Sale — Are you sure?", fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A)) },
            text = {
                Text(
                    "This sale totals ₹${"%.2f".format(total)}, which exceeds ₹10,000. Do you want to proceed?",
                    color = Color(0xFF5D3A1A)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.recordSale(selectedProduct!!, selectedColor!!, qty, p)
                        pendingSaleData = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE07B39))
                ) { Text("Yes, Save") }
            },
            dismissButton = {
                TextButton(onClick = { pendingSaleData = null }) {
                    Text("Cancel", color = Color(0xFF9E7060))
                }
            },
            containerColor = Color(0xFFFFF8F0)
        )
    }

    // Product options dialog (long-press)
    productForOptions?.let { product ->
        AlertDialog(
            onDismissRequest = { productForOptions = null },
            title = {
                Text(
                    "${product.emoji} ${product.name}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D3A1A)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { productToEdit = product; productForOptions = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("✏️  Edit Product", color = Color(0xFF5D3A1A), fontWeight = FontWeight.Medium)
                    }
                    TextButton(
                        onClick = { productToDelete = product; productForOptions = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🗑️  Delete Product", color = Color(0xFFBF360C), fontWeight = FontWeight.Medium)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { productForOptions = null }) {
                    Text("Cancel", color = Color(0xFF9E7060))
                }
            },
            containerColor = Color(0xFFFFF8F0)
        )
    }

    // Edit product dialog
    productToEdit?.let { product ->
        ProductFormDialog(
            title = "Edit Product",
            initialName = product.name,
            initialEmoji = product.emoji,
            initialDefaultPrice = product.defaultPrice?.let {
                if (it == it.toLong().toDouble()) it.toLong().toString() else "%.2f".format(it)
            } ?: "",
            onConfirm = { name, emoji, defaultPrice ->
                viewModel.updateProduct(product.copy(name = name, emoji = emoji, defaultPrice = defaultPrice))
                productToEdit = null
            },
            onDismiss = { productToEdit = null }
        )
    }

    // Delete product confirmation
    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Delete Product?", fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A)) },
            text = {
                Text(
                    "Are you sure you want to delete \"${product.name}\"? Related stock entries will also be removed.",
                    color = Color(0xFF5D3A1A)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedProduct?.id == product.id) selectedProduct = null
                        viewModel.deleteProduct(product)
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBF360C))
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancel", color = Color(0xFF9E7060))
                }
            },
            containerColor = Color(0xFFFFF8F0)
        )
    }

    // Add product dialog
    if (showAddProductDialog) {
        ProductFormDialog(
            title = "Add New Item",
            initialName = "",
            initialEmoji = "🛍️",
            initialDefaultPrice = "",
            onConfirm = { name, emoji, defaultPrice ->
                viewModel.addCustomProduct(name, emoji, defaultPrice)
                showAddProductDialog = false
            },
            onDismiss = { showAddProductDialog = false }
        )
    }
}

@Composable
private fun ProductFormDialog(
    title: String,
    initialName: String,
    initialEmoji: String,
    initialDefaultPrice: String,
    onConfirm: (name: String, emoji: String, defaultPrice: Double?) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var emoji by remember { mutableStateOf(initialEmoji) }
    var defaultPrice by remember { mutableStateOf(initialDefaultPrice) }
    var nameError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = "" },
                    label = { Text("Product Name") },
                    isError = nameError.isNotEmpty(),
                    supportingText = if (nameError.isNotEmpty()) {
                        { Text(nameError, color = Color(0xFFBF360C)) }
                    } else null,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = defaultPrice,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) defaultPrice = it },
                    label = { Text("Default Price ₹ (optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Default price pre-fills when billing. You can still edit it per sale.",
                    fontSize = 11.sp,
                    color = Color(0xFF9E7060)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = "Name is required"
                    } else {
                        val dp = defaultPrice.toDoubleOrNull()
                        onConfirm(name.trim(), emoji.ifBlank { "🛍️" }, if (dp != null && dp > 0) dp else null)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductCard(
    product: Product,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
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
            if (product.defaultPrice != null && product.defaultPrice > 0) {
                Text(
                    "₹${if (product.defaultPrice == product.defaultPrice.toLong().toDouble()) product.defaultPrice.toLong().toString() else "%.0f".format(product.defaultPrice)}",
                    fontSize = 10.sp,
                    color = Color(0xFF9E7060),
                    textAlign = TextAlign.Center
                )
            }
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