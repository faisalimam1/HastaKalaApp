package com.example.hastakalashop.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8F0))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🌾", fontSize = 64.sp)
        Spacer(Modifier.height(8.dp))
        Text("HastaKala", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D3A1A))
        Text("Farmer's Craft Shop", fontSize = 14.sp, color = Color(0xFF8D5A2A))
        Spacer(Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFE0CC)
        ) {
            Text(
                "Version 1.5",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 12.sp,
                color = Color(0xFFE07B39),
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(24.dp))

        AboutCard(
            emoji = "👨‍💻",
            title = "Made By",
            content = "Faisal Imam — built with love for the farmer community to help rural artisans manage their handmade craft business simply and offline."
        )
        Spacer(Modifier.height(12.dp))

        AboutCard(
            emoji = "🎯",
            title = "What is HastaKala?",
            content = "HastaKala is an offline-first shop management app designed for farmers and rural artisans who sell handmade crafts. No internet needed — everything works on your device."
        )
        Spacer(Modifier.height(12.dp))

        AboutCard(
            emoji = "📱",
            title = "How to Use",
            content = "Quick Bill  —  Select a product, pick a color (the color name is shown when selected), enter quantity & price, then tap Save Sale. A confirmation popup appears if a sale exceeds ₹10,000. Long-press any product to edit its name, emoji, or default price — or delete it. Tap '+ Add Item' to add your own custom products.\n\nBest Seller  —  See which products and colors sell the most, displayed as a visual pie chart.\n\nStock  —  Track your inventory. Tap the + button to add new stock. A warning icon appears when any item is running low (less than 3 units).\n\nIncome  —  View total earnings for the current week or month, with a full list of every sale. Tap ✏️ to edit a sale or 🗑️ to delete it if recorded by mistake."
        )
        Spacer(Modifier.height(12.dp))

        AboutCard(
            emoji = "✨",
            title = "Features",
            content = "• 100 % offline — works without internet\n• Record sales by product, color, quantity & price\n• 18 handcraft product categories pre-loaded\n• Add your own custom products with a name, emoji & default price\n• Edit or delete any product (pre-loaded or custom)\n• Color name shown on selection for clarity\n• Default price pre-fills automatically when billing\n• ₹10,000+ sale confirmation popup\n• Edit or delete any recorded sale\n• Color-wise inventory tracking\n• Low stock alerts (< 3 units)\n• Weekly & monthly income summary\n• Best-seller analytics with pie chart\n• Simple, farmer-friendly interface"
        )
        Spacer(Modifier.height(24.dp))

        Text(
            text = buildAnnotatedString {
                append("Made with ")
                withStyle(SpanStyle(color = Color(0xFFE53935))) { append("♥") }
                append(" by Faisal Imam")
            },
            fontSize = 13.sp,
            color = Color(0xFF8D5A2A)
        )
        Text("for our farmer community", fontSize = 12.sp, color = Color(0xFFAA8866))
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun AboutCard(emoji: String, title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 20.sp)
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF5D3A1A))
            }
            Spacer(Modifier.height(10.dp))
            Text(content, fontSize = 13.sp, color = Color(0xFF6B4C2A), lineHeight = 21.sp)
        }
    }
}
