package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Product
import com.example.ui.screens.*
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.OleafViewModel

enum class NavigationPortal {
    Home, Shop, Cart, Partners, Hub
}

@Composable
fun OleafAppNavigationContainer(viewModel: OleafViewModel) {
    val isHindi by viewModel.isHindi.collectAsState()
    val context = LocalContext.current

    var activePortal by remember { mutableStateOf(NavigationPortal.Home) }
    var selectedProductForDetail by remember { mutableStateOf<Product?>(null) }
    var shopCategoryFilter by remember { mutableStateOf("") }

    // Navigation trigger methods
    val navigateToShopAndFilterCategory: (String) -> Unit = { category ->
        selectedProductForDetail = null
        shopCategoryFilter = category
        activePortal = NavigationPortal.Shop
    }

    val navigateToShopCatalog: () -> Unit = {
        selectedProductForDetail = null
        shopCategoryFilter = ""
        activePortal = NavigationPortal.Shop
    }

    Scaffold(
        topBar = {
            OleafHeaderBlock(
                isHindi = isHindi,
                onToggleLang = { viewModel.toggleLanguage() }
            )
        },
        bottomBar = {
            // Standard M3 Bottom Navigation bar with safe drawing insets
            NavigationBar(
                containerColor = NavyDark,
                contentColor = Color.White,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars).testTag("bottom_nav_bar")
            ) {
                // Pin 5 primary sectors
                NavigationBarItem(
                    selected = activePortal == NavigationPortal.Home,
                    onClick = { activePortal = NavigationPortal.Home },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = activePortal == NavigationPortal.Shop,
                    onClick = {
                        selectedProductForDetail = null
                        shopCategoryFilter = ""
                        activePortal = NavigationPortal.Shop
                    },
                    icon = { Icon(Icons.Filled.ShoppingBag, contentDescription = "Shop") },
                    label = { Text("Shop", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_shop")
                )

                NavigationBarItem(
                    selected = activePortal == NavigationPortal.Cart,
                    onClick = { activePortal = NavigationPortal.Cart },
                    icon = {
                        val cartItems by viewModel.cart.collectAsState()
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge(containerColor = Color.Red) {
                                        Text(cartItems.sumOf { it.quantity }.toString(), color = Color.White, fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_cart")
                )

                NavigationBarItem(
                    selected = activePortal == NavigationPortal.Partners,
                    onClick = { activePortal = NavigationPortal.Partners },
                    icon = { Icon(Icons.Filled.People, contentDescription = "Partners") },
                    label = { Text("Partners", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_partners")
                )

                NavigationBarItem(
                    selected = activePortal == NavigationPortal.Hub,
                    onClick = { activePortal = NavigationPortal.Hub },
                    icon = { Icon(Icons.Filled.Business, contentDescription = "Hub") },
                    label = { Text("Hub", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyDark,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_item_hub")
                )
            }
        },
        floatingActionButton = {
            // Elegant brand Floating hotline triggers on Home/Product pages for instant help
            if (activePortal == NavigationPortal.Home || activePortal == NavigationPortal.Shop) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FloatingActionButton(
                        onClick = { triggerWhatsApp(context, "8571042210", "Hello Oleaf Mattress! I would like to enquire about bespoke products.") },
                        containerColor = Color(0xFF25D366),
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp).testTag("fab_whatsapp")
                    ) {
                        Icon(Icons.Filled.Chat, contentDescription = "WhatsApp Chat Support", tint = Color.White)
                    }

                    FloatingActionButton(
                        onClick = { triggerCall(context, "9991200768") },
                        containerColor = GoldPrimary,
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp).testTag("fab_call")
                    ) {
                        Icon(Icons.Filled.Call, contentDescription = "Dial direct call lines", tint = NavyDark)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Slide transition viewport routing
            AnimatedContent(
                targetState = activePortal,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "portal_anim_switcher"
            ) { targetPortal ->
                when (targetPortal) {
                    NavigationPortal.Home -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToProducts = navigateToShopCatalog,
                            onNavigateToCategory = navigateToShopAndFilterCategory,
                            onNavigateToCustom = { activePortal = NavigationPortal.Partners }
                        )
                    }

                    NavigationPortal.Shop -> {
                        val currentProd = selectedProductForDetail
                        if (currentProd != null) {
                            ProductDetailScreen(
                                viewModel = viewModel,
                                productId = currentProd.id,
                                onBack = { selectedProductForDetail = null },
                                onNavigateToCart = { activePortal = NavigationPortal.Cart }
                            )
                        } else {
                            ProductCatalogScreen(
                                viewModel = viewModel,
                                initialCategory = shopCategoryFilter,
                                onNavigateToDetail = { selectedProductForDetail = it }
                            )
                        }
                    }

                    NavigationPortal.Cart -> {
                        CartCheckoutScreen(
                            viewModel = viewModel,
                            onNavigateToProducts = navigateToShopCatalog
                        )
                    }

                    NavigationPortal.Partners -> {
                        // Consulated Custom Spec Mattress tool + Dealer Board
                        var partnerSubScreen by remember { mutableStateOf("Custom") }
                        Column(modifier = Modifier.fillMaxSize()) {
                            TabRow(
                                selectedTabIndex = if (partnerSubScreen == "Custom") 0 else 1,
                                contentColor = GoldPrimary,
                                containerColor = NavyDark
                            ) {
                                Tab(
                                    selected = partnerSubScreen == "Custom",
                                    onClick = { partnerSubScreen = "Custom" },
                                    text = { Text("Custom Size Config", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                )
                                Tab(
                                    selected = partnerSubScreen == "Dealer",
                                    onClick = { partnerSubScreen = "Dealer" },
                                    text = { Text("Dealer & Inquiry", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                                )
                            }
                            if (partnerSubScreen == "Custom") {
                                CustomSizeScreen(viewModel = viewModel)
                            } else {
                                BusinessPortalScreen(viewModel = viewModel)
                            }
                        }
                    }

                    NavigationPortal.Hub -> {
                        // Consolidated Hub: PDFs download, galleries, locations, profiles, and administration panels
                        var hubSubPage by remember { mutableStateOf("Media") }
                        Column(modifier = Modifier.fillMaxSize()) {
                            TabRow(
                                selectedTabIndex = when(hubSubPage) { "Media" -> 0; "Map" -> 1; "Profile" -> 2; else -> 3 },
                                contentColor = GoldPrimary,
                                containerColor = NavyDark
                            ) {
                                Tab(
                                    selected = hubSubPage == "Media",
                                    onClick = { hubSubPage = "Media" },
                                    text = { Text("Catalogs", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                                )
                                Tab(
                                    selected = hubSubPage == "Map",
                                    onClick = { hubSubPage = "Map" },
                                    text = { Text("Factory", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                                )
                                Tab(
                                    selected = hubSubPage == "Profile",
                                    onClick = { hubSubPage = "Profile" },
                                    text = { Text("Account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                                )
                                Tab(
                                    selected = hubSubPage == "Admin",
                                    onClick = { hubSubPage = "Admin" },
                                    text = { Text("Console", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                                )
                            }

                            when (hubSubPage) {
                                "Media" -> DocumentMediaScreen(viewModel = viewModel)
                                "Map" -> FactoryLocationScreen()
                                "Profile" -> UserProfileScreen(viewModel = viewModel)
                                "Admin" -> AdminPanelScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
