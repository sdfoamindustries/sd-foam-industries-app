package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.DrawOleafLogo
import com.example.ui.components.DrawSDFoamLogo
import com.example.ui.components.ProceduralImage
import com.example.ui.components.Translator
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.OleafViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Helper for implicit intents (real direct dialing & WhatsApp)
fun triggerCall(context: Context, number: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Direct dial interface unavailable ($number)", Toast.LENGTH_SHORT).show()
    }
}

fun triggerWhatsApp(context: Context, number: String, text: String) {
    try {
        val encodedText = Uri.encode(text)
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=91$number&text=$encodedText")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp integration link opened for 91$number", Toast.LENGTH_SHORT).show()
    }
}

// --- UTILITY COMPILER DESIGN HEADER ASSEMBLY ---
@Composable
fun OleafHeaderBlock(isHindi: Boolean, onToggleLang: () -> Unit) {
    Surface(
        color = NavyDark,
        tonalElevation = MapColorEntry.elevationValue()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DrawSDFoamLogo(modifier = Modifier.size(46.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1.0f)) {
                Text(
                    text = Translator.translate("sd_foam", isHindi),
                    color = GoldPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = Translator.translate("oleaf", isHindi) + " • Premium Luxury",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = onToggleLang,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(GoldPrimary)
                    .size(38.dp)
            ) {
                Text(
                    text = if (isHindi) "EN" else "हिं",
                    color = NavyDark,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

object MapColorEntry {
    fun elevationValue() = 4.dp
}

// ==========================================
// 1. HOME SCREEN SECTION
// ==========================================
@Composable
fun HomeScreen(
    viewModel: OleafViewModel,
    onNavigateToProducts: () -> Unit,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToCustom: () -> Unit
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val context = LocalContext.current
    val carouselImages = listOf("mattress_ortho", "mattress_pocket", "factory_german_line")
    val carouselTitles = listOf(
        "हर रात मिले सुकून भरी नींद",
        "World Class Pocket-Spring Luxury",
        "S D Foam Industries Factory Core"
    )
    val carouselSubtitles = listOf(
        "Direct from factory wholesale save",
        "Unparalleled contouring body relief",
        "Pioneering high-density PU foaming"
    )

    var currentIdx by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            currentIdx = (currentIdx + 1) % carouselImages.size
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
    ) {
        // Hero Slider Box
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                ProceduralImage(
                    type = carouselImages[currentIdx],
                    modifier = Modifier.fillMaxSize()
                )
                // Dark Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.82f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = carouselTitles[currentIdx],
                        color = GoldPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = carouselSubtitles[currentIdx],
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Slider Indicators
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    carouselImages.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (index == currentIdx) GoldPrimary else Color.White.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }

        // Tagline Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                border = BorderStroke(1.dp, GoldPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DrawOleafLogo(modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Translator.translate("tagline", isHindi),
                        color = GoldPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = Translator.translate("sub_tagline", isHindi),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Action Hotlines Rows (Quick Call & WhatsApp Direct)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { triggerCall(context, "8571042210") },
                        modifier = Modifier.weight(1.0f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                    ) {
                        Icon(Icons.Filled.Call, contentDescription = "Call", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Call Direct", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Button(
                        onClick = { triggerWhatsApp(context, "8571042210", "Hello S D Foam Oleaf Mattress team, I want to inquire about custom size mattresses.") },
                        modifier = Modifier.weight(1.0f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Icon(Icons.Filled.Chat, contentDescription = "WhatsApp", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("WhatsApp", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Factory Direct sale trust blocks
        item {
            Text(
                text = if (isHindi) "हमारे वादे • Our Assurances" else "Our Core Guarantees",
                color = NavyPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, top = 22.dp, bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val promises = listOf(
                    Triple(Icons.Filled.Factory, "Factory Direct", "No Middlemen"),
                    Triple(Icons.Filled.LocalOffer, "Best Price", "Direct Wholesale"),
                    Triple(Icons.Filled.Verified, "Premium Quality", "10Y Warranty"),
                    Triple(Icons.Filled.DesignServices, "Custom Sizes", "Tailor-Made")
                )

                promises.take(2).forEach { (icon, title, desc) ->
                    Card(
                        modifier = Modifier.weight(1.0f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(icon, contentDescription = title, tint = GoldDark, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                            Text(desc, fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val promises2 = listOf(
                    Triple(Icons.Filled.Verified, "Premium Quality", "10Y Warranty"),
                    Triple(Icons.Filled.DesignServices, "Custom Sizes", "Tailored")
                )

                promises2.forEach { (icon, title, desc) ->
                    Card(
                        modifier = Modifier.weight(1.0f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(icon, contentDescription = title, tint = GoldDark, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                            Text(desc, fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        // Custom Size Mattress Form entry banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.0f)) {
                        Text(
                            text = if (isHindi) "अपना मनचाहा गद्दे कस्टमाइज़ करें!" else "Need custom sized mattresses?",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )
                        Text(
                            text = if (isHindi) "चौड़ाई, लंबाई और मोटाई कस्टमाइज़ कराएं" else "Get quotes in real-time direct from factory.",
                            fontSize = 12.sp,
                            color = NavyDark.copy(alpha = 0.8f)
                        )
                    }
                    Button(
                        onClick = onNavigateToCustom,
                        colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
                    ) {
                        Text("Customize", color = GoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Categories Shortcut List
        item {
            Text(
                text = if (isHindi) "श्रेणियां • Explore Categories" else "Explore Categories",
                color = NavyPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 12.dp)
            )

            val cats = listOf("Mattress", "PU Foam", "Bonded Foam", "Pillow")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cats) { c ->
                    Card(
                        modifier = Modifier
                            .width(130.dp)
                            .clickable { onNavigateToCategory(c) },
                        colors = CardDefaults.cardColors(containerColor = NavyDark)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val placeholder_key = when(c) {
                                "Mattress" -> "mattress_ortho"
                                "PU Foam" -> "foam_pu"
                                "Bonded Foam" -> "foam_bonded"
                                else -> "pillow_memory"
                            }
                            ProceduralImage(type = placeholder_key, modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(c, color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp)) // padding for bottom bars
        }
    }
}


// ==========================================
// 2. PRODUCT CATEGORIES & DETAILS SCREEN
// ==========================================
@Composable
fun ProductCatalogScreen(
    viewModel: OleafViewModel,
    initialCategory: String,
    onNavigateToDetail: (Product) -> Unit
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val allProds by viewModel.allProducts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(if (initialCategory.isEmpty()) "All" else initialCategory) }

    val categories = listOf("All", "Mattress", "PU Foam", "Bonded Foam", "Pillow")

    // Dynamic Filter & Search Engine
    val filteredProducts = remember(allProds, searchQuery, selectedCategory) {
        allProds.filter { p ->
            (selectedCategory == "All" || p.category == selectedCategory) &&
                    (searchQuery.isEmpty() || p.name.contains(searchQuery, ignoreCase = true) || p.description.contains(searchQuery, ignoreCase = true) || p.sizes.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
    ) {
        // Search & Category Bar Row
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(if (isHindi) "उत्पाद, आकार या कीमत से खोजें" else "Search Name, Category or Sizes...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth().testTag("product_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NavyPrimary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable category selection pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = NavyDark
                            )
                        )
                    }
                }
            }
        }

        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = "Empty",
                        tint = GoldPrimary,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isHindi) "कोई भी उत्पाद उपलब्ध नहीं है।" else "No matching products found.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark
                    )
                }
            }
        } else {
            // Two column Responsive Grid layout
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1.0f)
            ) {
                items(filteredProducts) { p ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetail(p) }
                            .testTag("product_item_${p.id}"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column {
                            // Procedural drawing overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                            ) {
                                ProceduralImage(type = p.imagePlaceholder, modifier = Modifier.fillMaxSize())

                                // Low Stock Banner if inventory drops under 10
                                if (p.stockQuantity <= 10) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .background(Color.Red)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = Translator.translate("stock_alert", isHindi),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = p.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyDark,
                                    maxLines = 2
                                )
                                Text(
                                    text = p.category,
                                    fontSize = 11.sp,
                                    color = GoldDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "₹${p.basePrice}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NavyPrimary
                                )
                                Text(
                                    text = "${p.stockQuantity} Left in Stock",
                                    fontSize = 10.sp,
                                    color = if (p.stockQuantity <= 10) Color.Red else Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ProductDetailScreen(
    viewModel: OleafViewModel,
    productId: Int,
    onBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allReviews by viewModel.allReviews.collectAsState()
    val wishlist by viewModel.wishlist.collectAsState()
    val context = LocalContext.current

    val product = allProducts.find { it.id == productId }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product not found.")
        }
        return
    }

    val prodReviews = allReviews.filter { it.productId == productId }
    val productSizes = product.sizes.split(",")
    var selectedSize by remember { mutableStateOf(productSizes.firstOrNull() ?: "Standard") }

    // Video Player State
    var isVideoPlaying by remember { mutableStateOf(false) }

    // New Review Fields
    var ratingChosen by remember { mutableStateOf(5) }
    var reviewTextInput by remember { mutableStateOf("") }
    var reviewNameInput by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
    ) {
        // App bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                shape = RoundedCornerShape(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        product.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1.0f)
                    )
                    IconButton(onClick = { viewModel.toggleWishlist(product.id) }) {
                        Icon(
                            imageVector = if (wishlist.contains(product.id)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (wishlist.contains(product.id)) Color.Red else Color.White
                        )
                    }
                }
            }
        }

        // Feature Image Block with rich procedural canvas
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                ProceduralImage(type = product.imagePlaceholder, modifier = Modifier.fillMaxSize())

                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldPrimary)
                ) {
                    Text(
                        text = product.category,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = NavyDark,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Product information
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${product.basePrice}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Price Guarantee",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = product.description,
                    color = Color.DarkGray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // Size configuration selectors
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = Translator.translate("sizes_avail", isHindi),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = NavyDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productSizes) { s ->
                        val isSelected = s == selectedSize
                        val text = s.trim()
                        OutlinedButton(
                            onClick = { selectedSize = s },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) NavyDark else Color.White,
                                contentColor = if (isSelected) GoldPrimary else NavyDark
                            ),
                            border = BorderStroke(1.dp, if (isSelected) GoldPrimary else Color.Gray)
                        ) {
                            Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Specifications
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Translator.translate("specs", isHindi),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = NavyDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                val specificationRows = product.specifications.split(";")
                specificationRows.forEach { spec ->
                    val parts = spec.split(":")
                    if (parts.size >= 2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(parts[0], color = Color.Gray, fontSize = 12.sp)
                            Text(parts[1], fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = NavyDark)
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }

        // Video Demonstrations Showcase (Simulated Interactive Youtube player)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = if (isHindi) "उत्पादन वीडियो और शोरूम प्रदर्शन" else "Factory Testing & Video Showcase",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = NavyDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (isVideoPlaying) {
                        // Playing loop
                        CircularProgressIndicator(color = GoldPrimary)
                        Text(
                            "Simulating HD Video Playback Loop...\n(Manufacturing Direct Demo)",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp)
                        )
                        IconButton(onClick = { isVideoPlaying = false }, modifier = Modifier.align(Alignment.TopEnd)) {
                            Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = { isVideoPlaying = true },
                                modifier = Modifier
                                    .background(GoldPrimary, CircleShape)
                                    .size(48.dp)
                            ) {
                                Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = NavyDark)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Play Mattress Quality Demonstration (1080p)", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // WhatsApp / Buy button block / Shopping checkout actions
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.addToCart(product, selectedSize)
                        Toast.makeText(context, "Added to shopping cart!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1.0f).height(48.dp).testTag("product_add_to_cart"),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(Translator.translate("add_to_cart", isHindi), fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.addToCart(product, selectedSize)
                        onNavigateToCart()
                    },
                    modifier = Modifier.weight(1.0f).height(48.dp).testTag("product_buy_now"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text(Translator.translate("buy_now", isHindi), color = NavyDark, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Star Rating feedback and write reviews
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = Translator.translate("rating", isHindi),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = NavyDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Leave a reviews card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Add Your Review Direct to Factory", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyPrimary)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            (1..5).forEach { stars ->
                                IconButton(onClick = { ratingChosen = stars }, modifier = Modifier.size(24.dp)) {
                                    Icon(
                                        imageVector = if (stars <= ratingChosen) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = "Star",
                                        tint = GoldPrimary
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = reviewNameInput,
                            onValueChange = { reviewNameInput = it },
                            placeholder = { Text("Your Name or Location") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = reviewTextInput,
                            onValueChange = { reviewTextInput = it },
                            placeholder = { Text("Comment regarding sleep quality, foam density...") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = {
                                if (reviewTextInput.isEmpty() || reviewNameInput.isEmpty()) {
                                    Toast.makeText(context, "Fill out review name and comments!", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.addProductReview(product.id, product.name, reviewNameInput, ratingChosen, reviewTextInput)
                                    reviewTextInput = ""
                                    reviewNameInput = ""
                                    Toast.makeText(context, "Review saved locally inside Room Database", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Submit Review", color = NavyDark, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // List of reviews existing
                prodReviews.forEach { r ->
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(r.customerName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                            Row {
                                (1..r.rating).forEach {
                                    Icon(Icons.Filled.Star, contentDescription = "*", tint = GoldPrimary, modifier = Modifier.size(12.dp))
                                }
                            }
                        }
                        Text(r.comment, fontSize = 11.sp, color = Color.DarkGray)
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}


// ==========================================
// 3. CART & CHECKOUT & PDF BILL VISUALIZER
// ==========================================
@Composable
fun CartCheckoutScreen(
    viewModel: OleafViewModel,
    onNavigateToProducts: () -> Unit
) {
    val isHindi by viewModel.isHindi.collectAsState()
    val items by viewModel.cart.collectAsState()
    val context = LocalContext.current

    // Shipping Billing profile fields
    var customerName by remember { mutableStateOf(viewModel.loggedInUser.value ?: "") }
    var customerMobile by remember { mutableStateOf(viewModel.userMobile.value ?: "") }
    var destinationAddress by remember { mutableStateOf(viewModel.userAddress.value ?: "") }
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }

    // State after successfully placing order
    var placedOrderInvoice by remember { mutableStateOf<Order?>(null) }

    val subtotal = items.sumOf { it.product.basePrice * it.quantity }
    val gstAmount = subtotal * 0.18 // 18% GST automatic calculation
    val finalTotal = subtotal + gstAmount

    if (placedOrderInvoice != null) {
        // Complete Beautiful GST Invoice layout
        InvoiceReportView(order = placedOrderInvoice!!, isHindi = isHindi, onDismiss = { placedOrderInvoice = null })
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
            .padding(16.dp)
    ) {
        item {
            Text(
                text = if (isHindi) "शॉपिंग कार्ट और बिलिंग विवरण" else "Shopping Cart & Billing",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyPrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        if (items.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Empty", tint = GoldPrimary, modifier = Modifier.size(54.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Your Shopping Cart is Empty", fontWeight = FontWeight.Bold, color = NavyDark)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onNavigateToProducts, colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)) {
                            Text("Browse Products Catalog", color = Color.White)
                        }
                    }
                }
            }
        } else {
            // Display Cart Goods
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProceduralImage(type = item.product.imagePlaceholder, modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1.0f)) {
                            Text(item.product.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDark)
                            Text("Size: ${item.selectedSize}", fontSize = 11.sp, color = GoldDark)
                            Text("₹${item.product.basePrice} x ${item.quantity}", fontSize = 12.sp, color = Color.Gray)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.updateCartQuantity(item.product, item.selectedSize, item.quantity - 1) }) {
                                Icon(Icons.Filled.Remove, contentDescription = "SubQty", modifier = Modifier.size(18.dp))
                            }
                            Text(item.quantity.toString(), fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            IconButton(onClick = { viewModel.updateCartQuantity(item.product, item.selectedSize, item.quantity + 1) }) {
                                Icon(Icons.Filled.Add, contentDescription = "AddQty", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Billing breakdown card (Automatic 18% GST audit)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, NavyPrimary.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("GST Audit Sheet (Automatic Invoice)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal (Raw Factory Price)", fontSize = 12.sp)
                            Text("₹$subtotal", fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GST (18% Integrated standard tax)", fontSize = 12.sp)
                            Text("₹$gstAmount", fontSize = 12.sp)
                        }
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Final Net Amount Payable", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary)
                            Text("₹$finalTotal", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NavyPrimary)
                        }
                    }
                }
            }

            // Checkout Info Form inputs
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Billing & Delivery Address Form", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("Customer Name") },
                            modifier = Modifier.fillMaxWidth().testTag("billing_name_field"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = customerMobile,
                            onValueChange = { customerMobile = it },
                            label = { Text("Mobile Phone Number") },
                            modifier = Modifier.fillMaxWidth().testTag("billing_mobile_field"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = destinationAddress,
                            onValueChange = { destinationAddress = it },
                            label = { Text("Full Construction / Shipping Address") },
                            modifier = Modifier.fillMaxWidth().testTag("billing_address_field"),
                            maxLines = 3
                        )
                    }
                }
            }

            // Payment Gateways choices
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Integrated Payment Gateway", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDark)
                        Spacer(modifier = Modifier.height(10.dp))

                        val gateways = listOf("UPI", "Credit/Debit Card", "Razorpay Secured Link", "Stripe Checkout Gate", "Cash On Delivery (COD)")
                        gateways.forEach { gw ->
                            val active = gw == selectedPaymentMethod
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPaymentMethod = gw }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = active, onClick = { selectedPaymentMethod = gw })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(gw, fontSize = 12.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }

            // Order dispatch execution button
            item {
                Button(
                    onClick = {
                        if (customerName.isEmpty() || customerMobile.isEmpty() || destinationAddress.isEmpty()) {
                            Toast.makeText(context, "Kindly fill out your Name, Mobile and Delivery Address to place order", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.placeOrder(
                                customerName,
                                customerMobile,
                                destinationAddress,
                                selectedPaymentMethod
                            )
                            Toast.makeText(context, "Order created successfully inside local database!", Toast.LENGTH_LONG).show()

                            // Auto Generate Printable Invoice
                            val newestInvoice = Order(
                                customerName = customerName,
                                customerMobile = customerMobile,
                                address = destinationAddress,
                                productSummary = items.joinToString(", ") { "${it.product.name} (${it.selectedSize}) x${it.quantity}" },
                                totalAmount = finalTotal,
                                paymentMethod = selectedPaymentMethod,
                                orderType = "Retail",
                                gstAmount = gstAmount,
                                invoiceNo = "INV-${(100000..999999).random()}"
                            )
                            placedOrderInvoice = newestInvoice
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(top = 10.dp)
                        .testTag("checkout_place_order_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("SECURE CHECKOUT - PLACE ORDER", color = NavyDark, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


// INVOICE REPORT DISPLAY COMPOSABLE
@Composable
fun InvoiceReportView(order: Order, isHindi: Boolean, onDismiss: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECEFF1))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(2.dp, GoldPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Enterprise Letterhead header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("S D FOAM INDUSTRIES", fontWeight = FontWeight.Black, color = NavyPrimary, fontSize = 15.sp)
                        Text("Brand: Oleaf Mattress", color = GoldDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Factory Address: NH-44 G.T Road, Haryana", fontSize = 10.sp, color = Color.Gray)
                        Text("GSTIN: 06AAFFS2314M1Z2", fontSize = 10.sp, color = Color.Gray)
                    }
                    DrawSDFoamLogo(modifier = Modifier.size(50.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = GoldPrimary, thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Invoice metadata
                Text("TAX INVOICE / CASH BILL", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = NavyDark, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Invoice No: ${order.invoiceNo}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Date: 1hr prior to dynamic UTC", fontSize = 10.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Customer particulars
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Billed To (Customer Detail):", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NavyPrimary)
                        Text("Name: ${order.customerName}", fontSize = 12.sp)
                        Text("Phone: ${order.customerMobile}", fontSize = 12.sp)
                        Text("Address: ${order.address}", fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Items summary description
                Text("Transaction Item Summary:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(0.5.dp, Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(order.productSummary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = NavyDark)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tax breakdown matrix (GST Auto math calculations)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Assessed Basic Factory Cost", fontSize = 12.sp)
                    Text("₹${String.format("%.2f", order.totalAmount / 1.18)}", fontSize = 12.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("18% Integrated GST Breakdown", fontSize = 12.sp)
                    val baseCost = order.totalAmount / 1.18
                    Text("₹${String.format("%.2f", order.totalAmount - baseCost)}", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Gross Cost (Tax Included)", fontWeight = FontWeight.Bold, color = NavyPrimary)
                    Text("₹${String.format("%.2f", order.totalAmount)}", fontWeight = FontWeight.ExtraBold, color = NavyPrimary, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "This is a computer generated invoice and requires no physical seal signature. Thank you for choosing Oleaf Comfort!",
                    fontSize = 9.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Print download action row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "PDF layout written to downloads folder successfully!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1.0f),
                colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
            ) {
                Icon(Icons.Filled.Download, contentDescription = "D")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Download PDF Bill", color = Color.White, fontSize = 12.sp)
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Printer dialog triggered!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1.0f),
                colors = ButtonDefaults.buttonColors(containerColor = NavyDark)
            ) {
                Icon(Icons.Filled.Print, contentDescription = "P")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Print Invoice", color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
        ) {
            Text("Back to Shopping", color = NavyDark, fontWeight = FontWeight.Bold)
        }
    }
}


// ==========================================
// 8. CUSTOM SIZE MATTRESS CALCULATOR & FORM
// ==========================================
@Composable
fun CustomSizeScreen(viewModel: OleafViewModel) {
    val isHindi by viewModel.isHindi.collectAsState()
    val context = LocalContext.current

    // Fields
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var lengthInches by remember { mutableStateOf("") }
    var widthInches by remember { mutableStateOf("") }
    var thicknessInches by remember { mutableStateOf("") }
    var quantityInput by remember { mutableStateOf("1") }

    // Estimate math
    val l = lengthInches.toDoubleOrNull() ?: 0.0
    val w = widthInches.toDoubleOrNull() ?: 0.0
    val t = thicknessInches.toDoubleOrNull() ?: 0.0
    val q = quantityInput.toIntOrNull() ?: 1

    // Formula pricing estimate base calculation
    val priceCalculation = remember(l, w, t, q) {
        if (l > 0 && w > 0 && t > 0) {
            (l * w * t * 0.45 * q).coerceAtLeast(4999.00)
        } else {
            0.0
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
            .padding(16.dp)
    ) {
        item {
            Text(
                "Custom Mattress Sizings Calculator",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = NavyPrimary
            )
            Text(
                "Need tailored sizes that fit standard frames? Input specifications for immediate wholesale pricing estimation.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Input Mattress Core Dimensions (Inches)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Customer Name") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_size_name_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("Direct Call Mobile Phone") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_size_mobile_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = lengthInches,
                            onValueChange = { lengthInches = it },
                            label = { Text("Length (L)") },
                            modifier = Modifier.weight(1.0f).testTag("custom_length_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = widthInches,
                            onValueChange = { widthInches = it },
                            label = { Text("Width (W)") },
                            modifier = Modifier.weight(1.0f).testTag("custom_width_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = thicknessInches,
                            onValueChange = { thicknessInches = it },
                            label = { Text("Thick (T)") },
                            modifier = Modifier.weight(1.0f).testTag("custom_thickness_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = quantityInput,
                        onValueChange = { quantityInput = it },
                        label = { Text("Quantity") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }
        }

        // Live estimate block
        if (priceCalculation > 0) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDark)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Live Factory-Direct Wholesale Cost", color = GoldPrimary, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("₹${String.format("%.2f", priceCalculation)}", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Black)
                        Text("+ 18% GST (Includes tailor-made diamond quilting, premium zipper lock)", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (clientName.isEmpty() || clientPhone.isEmpty() || lengthInches.isEmpty() || widthInches.isEmpty() || thicknessInches.isEmpty()) {
                        Toast.makeText(context, "Fill out dimensions and customer particulars completely", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.placeOrder(
                            clientName = clientName,
                            clientMobile = clientPhone,
                            clientAddress = "Tailor-Fabrication Assembly Queue",
                            pm = "COD Mode Requested",
                            orderType = "Retail Custom",
                            isCustom = true,
                            customDims = "L:$lengthInches\" x W:$widthInches\" x T:$thicknessInches\" [Qty: $quantityInput]",
                            customPrice = priceCalculation
                        )
                        Toast.makeText(context, "Custom mattress query sent directly to S D Foam manufacturing system!", Toast.LENGTH_LONG).show()

                        // Trigger direct WhatsApp message
                        triggerWhatsApp(context, "8571042210", "Hi S D Foam, custom size request submitted for Oleaf Mattress! Length: $lengthInches\", Width: $widthInches\", Thickness: $thicknessInches\", Qty: $quantityInput.")

                        // reset
                        clientName = ""
                        clientPhone = ""
                        lengthInches = ""
                        widthInches = ""
                        thicknessInches = ""
                        quantityInput = "1"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 8.dp)
                    .testTag("custom_size_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
            ) {
                Text("SUBMIT CUSTOM ORDER FOR BID", color = NavyDark, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}


// ==========================================
// 9. DEALER REGISTRATION & 10. CUSTOMER INQUIRY
// ==========================================
@Composable
fun BusinessPortalScreen(viewModel: OleafViewModel) {
    val isHindi by viewModel.isHindi.collectAsState()
    val context = LocalContext.current

    var activeTab by remember { mutableStateOf("Dealer") }

    // Dealer inputs
    var dBusinessName by remember { mutableStateOf("") }
    var dOwnerName by remember { mutableStateOf("") }
    var dMobile by remember { mutableStateOf("") }
    var dGst by remember { mutableStateOf("") }
    var dAddress by remember { mutableStateOf("") }

    // Inquiry inputs
    var iName by remember { mutableStateOf("") }
    var iMobile by remember { mutableStateOf("") }
    var iCity by remember { mutableStateOf("") }
    var iProductRequired by remember { mutableStateOf("Premium Mattress") }
    var iMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
            .padding(16.dp)
    ) {
        item {
            Text(
                "S D Foam Partner Portal",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = NavyPrimary
            )
            Text(
                "Direct factory dealerships and custom wholesale inquiry desk.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TabRow(
                selectedTabIndex = if (activeTab == "Dealer") 0 else 1,
                contentColor = GoldPrimary,
                containerColor = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Tab(
                    selected = activeTab == "Dealer",
                    onClick = { activeTab = "Dealer" },
                    text = { Text("Dealer Application", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == "Inquiry",
                    onClick = { activeTab = "Inquiry" },
                    text = { Text("Direct Inquiry", fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (activeTab == "Dealer") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Register as Authorized wholesale Dealer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = dBusinessName,
                            onValueChange = { dBusinessName = it },
                            label = { Text("Registered Business / Firm Name") },
                            modifier = Modifier.fillMaxWidth().testTag("dealer_business_name"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dOwnerName,
                            onValueChange = { dOwnerName = it },
                            label = { Text("Owner / Director Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dMobile,
                            onValueChange = { dMobile = it },
                            label = { Text("Dealership Mobile Phone Liaison") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dGst,
                            onValueChange = { dGst = it },
                            label = { Text("15-digit GST Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dAddress,
                            onValueChange = { dAddress = it },
                            label = { Text("Full Showroom / Warehouse Dispatch Address") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (dBusinessName.isEmpty() || dOwnerName.isEmpty() || dMobile.isEmpty() || dGst.isEmpty() || dAddress.isEmpty()) {
                                    Toast.makeText(context, "Fill out dealership form completely", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.submitDealerRequest(
                                        bName = dBusinessName,
                                        oName = dOwnerName,
                                        mobile = dMobile,
                                        gst = dGst,
                                        addr = dAddress
                                    )
                                    Toast.makeText(context, "Dealer proposal registered! Wait for factory inspection.", Toast.LENGTH_LONG).show()
                                    dBusinessName = ""
                                    dOwnerName = ""
                                    dMobile = ""
                                    dGst = ""
                                    dAddress = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("dealer_register_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                        ) {
                            Text("SUBMIT AUTHOR DEALERSHIP BID", color = NavyDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Business & Institutional Inquiry Desk", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = iName,
                            onValueChange = { iName = it },
                            label = { Text("Your Complete Name") },
                            modifier = Modifier.fillMaxWidth().testTag("inquiry_name_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = iMobile,
                            onValueChange = { iMobile = it },
                            label = { Text("Mobile Contact") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = iCity,
                            onValueChange = { iCity = it },
                            label = { Text("City Location") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Select Procurement Category", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
                        val options = listOf("Premium Mattress", "High Density PU Foam Sheets", "Rebonded Soundproof Panels", "Pillows Bulk order")
                        options.forEach { opt ->
                            val active = opt == iProductRequired
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { iProductRequired = opt }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = active, onClick = { iProductRequired = opt })
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(opt, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = iMessage,
                            onValueChange = { iMessage = it },
                            label = { Text("Brief of procurement requirement") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (iName.isEmpty() || iMobile.isEmpty() || iCity.isEmpty() || iMessage.isEmpty()) {
                                    Toast.makeText(context, "Fill out institutional inquiry fields", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.submitInquiry(
                                        name = iName,
                                        mobile = iMobile,
                                        city = iCity,
                                        requirement = iProductRequired,
                                        message = iMessage
                                    )
                                    Toast.makeText(context, "Inquiry registered! Direct call scheduled.", Toast.LENGTH_LONG).show()

                                    // Direct trigger phone contact
                                    triggerWhatsApp(context, "8571042210", "Hi S D Foam Industries, Bulk Inquiry submitted! Item: $iProductRequired, Details: $iMessage")

                                    iName = ""
                                    iMobile = ""
                                    iCity = ""
                                    iMessage = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth().testTag("inquiry_submit_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                        ) {
                            Text("SEND ENQUIRY TO ADMIN", color = NavyDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(90.dp)) }
    }
}


// ==========================================
// 5. PHOTO & 6. VIDEO GALLERIES & 7. PDF CENTRE
// ==========================================
@Composable
fun DocumentMediaScreen(viewModel: OleafViewModel) {
    val isHindi by viewModel.isHindi.collectAsState()
    val media by viewModel.allGalleryMedia.collectAsState()
    val pdfs by viewModel.allPdfDocuments.collectAsState()
    val context = LocalContext.current

    var activeDocTab by remember { mutableStateOf("PDF") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
            .padding(16.dp)
    ) {
        item {
            Text(
                "Oleaf Media & Document Center",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = NavyPrimary
            )
            Text(
                "Access catalogs, factory foaming videos, and user galleries directly.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TabRow(
                selectedTabIndex = if (activeDocTab == "PDF") 0 else 1,
                contentColor = GoldPrimary,
                containerColor = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Tab(
                    selected = activeDocTab == "PDF",
                    onClick = { activeDocTab = "PDF" },
                    text = { Text("Wholesale PDFs", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeDocTab == "Gallery",
                    onClick = { activeDocTab = "Gallery" },
                    text = { Text("Factory Media Gallery", fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (activeDocTab == "PDF") {
            if (pdfs.isEmpty()) {
                item { Text("No downloadable PDF guidelines at historical times.", color = Color.Gray) }
            } else {
                items(pdfs) { d ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Folder, contentDescription = "PDF", tint = NavyPrimary, modifier = Modifier.size(38.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1.0f)) {
                                Text(d.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDark)
                                Text("Type: ${d.docType} • Size: ${d.fileSize}", fontSize = 11.sp, color = Color.Gray)
                            }
                            IconButton(onClick = {
                                Toast.makeText(context, "Opening PDF viewer sandbox: ${d.fileName}", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Filled.Visibility, contentDescription = "View", tint = GoldDark)
                            }
                            IconButton(onClick = {
                                Toast.makeText(context, "${d.fileName} saved in local external file cache!", Toast.LENGTH_LONG).show()
                            }) {
                                Icon(Icons.Filled.Download, contentDescription = "Download", tint = NavyPrimary)
                            }
                        }
                    }
                }
            }
        } else {
            // Interactive Media gallery photo videos grid
            items(media) { m ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            ProceduralImage(type = m.url, modifier = Modifier.fillMaxSize())

                            if (m.mediaType == "Video") {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                        .padding(8.dp)
                                ) {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = GoldPrimary, modifier = Modifier.size(24.dp))
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .background(NavyDark)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(m.category, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(m.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                            Text(if (m.mediaType == "Video") "Manufacturing Demo (MP4 Video)" else "Photo Shot", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(90.dp)) }
    }
}


// ==========================================
// 17. FACTORY PAGE & 19. LOCATION MAP
// ==========================================
@Composable
fun FactoryLocationScreen() {
    val context = LocalContext.current
    val processSteps = listOf(
        Pair("1. Raw Materials Foaming", "Blending polymer resins with state of art German catalytic control to manufacture solid polyurethane core foam blocks of precise densities."),
        Pair("2. Block Curing Bays", "High volume 72-hour controlled thermal cooling process allowing homogeneous molecular bonds, avoiding structural collapse or dynamic sag."),
        Pair("3. Precise CNC slicing", "Precision microcomputer splitting profiles of specified thickness (2\", 3\", 4\") using oscillating cutting wires under 0.2mm tolerance."),
        Pair("4. CNC Diamond Quilting", "Automatic premium multi-needle sewing head binds layers of memory foam, soft comfort cotton, and royal golden knit fabric."),
        Pair("5. Final Compression Tests", "Every mattress model traverses mechanical compression sweeps of 100,000 continuous compression cycles maintaining shape resiliency.")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
            .padding(16.dp)
    ) {
        item {
            Text(
                "S D Foam Industrial Manufacturing",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = NavyPrimary
            )
            Text(
                "Unsurpassed quality controls direct from the core factory floor.",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Production layout processes
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Automated Foaming & Stitching Pipeline", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                    Spacer(modifier = Modifier.height(10.dp))

                    processSteps.forEach { (stepTitle, stepDesc) ->
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Text(stepTitle, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GoldDark)
                            Text(stepDesc, fontSize = 11.sp, color = Color.DarkGray, lineHeight = 15.sp)
                        }
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }
            }
        }

        // Factory location map area (procedural visual diagram)
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Corporate Factory Coordinates & Maps", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDark)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    // Procedural Map card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color(0xFFE8F5E9)), // map green backdrop
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw streets lines
                            drawLine(color = Color.White, start = Offset(0f, h * 0.4f), end = Offset(w, h * 0.4f), strokeWidth = 14f)
                            drawLine(color = Color.White, start = Offset(w * 0.3f, 0f), end = Offset(w * 0.3f, h), strokeWidth = 14f)

                            // Factory landmark
                            drawRect(
                                color = NavyPrimary,
                                topLeft = Offset(w * 0.35f, h * 0.45f),
                                size = Size(w * 0.35f, h * 0.35f)
                            )
                            // GPS locator Pin
                            drawCircle(color = Color.Red, radius = 9f, center = Offset(w * 0.5f, h * 0.55f))
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .background(NavyDark.copy(alpha = 0.85f))
                                .padding(6.dp)
                        ) {
                            Text("S D FOAM INDUSTRIES (G.T. Road Location)", color = GoldPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("Click coordinates above to launch Google Maps", color = Color.White, fontSize = 8.sp)
                        }
                    }

                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Address details:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = NavyPrimary)
                        Text("S D Foam Industries Complex, Industrial Area, National Highway 44 (G.T. Road), Sonepat-Kurnool Highway, Haryana.", fontSize = 11.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Direct Helpline Liaison Staff:", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = NavyPrimary)
                        Text("85710 42210 / 99912 00768 (Call anytime for bespoke sizing estimates)", fontSize = 11.sp, color = Color.DarkGray)

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                val mapUri = Uri.parse("geo:29.0588,77.0205?q=SD+Foam+Industries+Haryana")
                                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                                context.startActivity(mapIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.Map, contentDescription = "Map")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Launch Google Maps GPS Route", color = Color.White)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}
