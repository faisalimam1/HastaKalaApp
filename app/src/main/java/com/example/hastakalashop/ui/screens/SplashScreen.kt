package com.example.hastakalashop.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        onFinished()
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "splash_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🌾", fontSize = 80.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                text = "HastaKala",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D3A1A)
            )
            Text(
                text = "Farmer's Craft Shop",
                fontSize = 16.sp,
                color = Color(0xFF8D5A2A)
            )
            Spacer(Modifier.height(64.dp))
            Text(
                text = buildAnnotatedString {
                    append("Made with ")
                    withStyle(SpanStyle(color = Color(0xFFE53935), fontSize = 18.sp)) {
                        append("♥")
                    }
                    append(" by Faisal")
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5D3A1A)
            )
            Text(
                text = "for our farmer community",
                fontSize = 13.sp,
                color = Color(0xFF8D5A2A)
            )
        }
    }
}
