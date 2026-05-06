package com.example.hastakalashop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hastakalashop.ui.screens.*
import com.example.hastakalashop.ui.theme.HastaKalaShopTheme
import com.example.hastakalashop.viewmodel.HastaKalaViewModel
import com.example.hastakalashop.viewmodel.HastaKalaViewModelFactory

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Bill      : Screen("bill",      "Quick Bill",  Icons.Default.Home)
    object Dashboard : Screen("dashboard", "Best Seller", Icons.Default.Star)
    object Stock     : Screen("stock",     "Stock",       Icons.Default.ShoppingCart)
    object Income    : Screen("income",    "Income",      Icons.AutoMirrored.Filled.List)
}

val screens = listOf(Screen.Bill, Screen.Dashboard, Screen.Stock, Screen.Income)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = (application as HastaKalaApp).repository
        setContent {
            HastaKalaShopTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(onFinished = { showSplash = false })
                } else {
                    val navController = rememberNavController()
                    val viewModel: HastaKalaViewModel =
                        viewModel(factory = HastaKalaViewModelFactory(repository))
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        topBar = {
                            if (currentRoute == "about") {
                                TopAppBar(
                                    title = { Text("About", color = Color(0xFF5D3A1A)) },
                                    navigationIcon = {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back",
                                                tint = Color(0xFF5D3A1A)
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color(0xFFFFF3E0)
                                    )
                                )
                            } else {
                                TopAppBar(
                                    title = {
                                        Text("🌾 HastaKala", color = Color(0xFF5D3A1A))
                                    },
                                    actions = {
                                        IconButton(onClick = { navController.navigate("about") }) {
                                            Icon(
                                                Icons.Default.Info,
                                                contentDescription = "About",
                                                tint = Color(0xFF5D3A1A)
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color(0xFFFFF3E0)
                                    )
                                )
                            }
                        },
                        bottomBar = {
                            if (currentRoute != "about") {
                                NavigationBar(containerColor = Color.White) {
                                    val currentDestination = navBackStackEntry?.destination
                                    screens.forEach { screen ->
                                        NavigationBarItem(
                                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                                            label = { Text(screen.label) },
                                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                            onClick = {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color(0xFFE07B39),
                                                selectedTextColor = Color(0xFFE07B39),
                                                indicatorColor = Color(0xFFFFE0CC)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController,
                            startDestination = Screen.Bill.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Bill.route)      { QuickBillScreen(viewModel) }
                            composable(Screen.Dashboard.route) { DashboardScreen(viewModel) }
                            composable(Screen.Stock.route)     { StockScreen(viewModel) }
                            composable(Screen.Income.route)    { IncomeScreen(viewModel) }
                            composable("about")                { AboutScreen() }
                        }
                    }
                }
            }
        }
    }
}
