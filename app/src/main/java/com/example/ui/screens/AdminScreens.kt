package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.ProceduralImage
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.OleafViewModel

// ==========================================
// 11. ADMIN PANEL (DASHBOARD & LOGGING)
// ==========================================
@Composable
fun AdminPanelScreen(viewModel: OleafViewModel) {
    val context = LocalContext.current
    var isAuthorized by remember { mutableStateOf(false) }

    // Forms Login States
    var userVal by remember { mutableStateOf("") }
    var passVal by remember { mutableStateOf("") }

    if (!isAuthorized) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FBFD))
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, GoldPrimary),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("SD Foam Industries Portal", fontWeight = FontWeight.Black, fontSize = 16.sp, color = NavyPrimary)
                    Text("Oleaf Authorized Admin Login", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GoldDark)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = userVal,
                        onValueChange = { userVal = it },
                        label = { Text("Liaison Username") },
                        modifier = Modifier.fillMaxWidth().testTag("admin_username_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passVal,
                        onValueChange = { passVal = it },
                        label = { Text("Authentication Passcode") },
                        modifier = Modifier.fillMaxWidth().testTag("admin_password_input"),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (userVal.trim().equals("admin", true) && passVal == "password") {
                                isAuthorized = true
                                Toast.makeText(context, "Access Granted! Welcome Administrator.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Incorrect Credentials (sandbox: admin / password)", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("admin_login_submit")
                    ) {
                        Text("AUTHORIZE CONSOLE ACCESS", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    // REAL ADMIN ACTIVE PANEL SCREEN
    AdminConsoleDashboard(viewModel = viewModel, onLock = { isAuthorized = false })
}


@Composable
fun AdminConsoleDashboard(viewModel: OleafViewModel, onLock: () -> Unit) {
    val prds by viewModel.allProducts.collectAsState()
    val ords by viewModel.allOrders.collectAsState()
    val dlrs by viewModel.allDealers.collectAsState()
    val inqs by viewModel.allInquiries.collectAsState()
    val pdfs by viewModel.allPdfDocuments.collectAsState()
    val invLogs by viewModel.allInventoryLogs.collectAsState()
    val context = LocalContext.current

    var activeSubMenu by remember { mutableStateOf("Dashboard") }

    val totalIncome = ords.sumOf { it.totalAmount }
    val lowStockCount = prds.filter { it.stockQuantity <= 10 }.size

    // Forms to Create New products
    var showAddProductDialog by remember { mutableStateOf(false) }
    var formProdName by remember { mutableStateOf("") }
    var formProdCategory by remember { mutableStateOf("Mattress") }
    var formProdDesc by remember { mutableStateOf("") }
    var formProdSpecs by remember { mutableStateOf("Material:PU Foam;Density:32 HD;Warranty:5 Years") }
    var formProdPrice by remember { mutableStateOf("") }
    var formProdSizes by remember { mutableStateOf("Single,Double,Queen,King") }
    var formProdStock by remember { mutableStateOf("") }

    // Forms to Create catalogs
    var showAddPdfDialog by remember { mutableStateOf(false) }
    var formPdfTitle by remember { mutableStateOf("") }
    var formPdfType by remember { mutableStateOf("Catalog") }
    var formPdfFile by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECEFF1))
    ) {
        // Console Header toolbar
        Surface(color = NavyDark) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Oleaf Authorized Console", color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Button(
                    onClick = onLock,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("Exit Lock", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Sub Menu Bar horizontal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyPrimary)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val menus = listOf("Dashboard", "Products", "Orders", "Dealers", "Inquiries", "Catalogs", "Inventory Logs")
            menus.forEach { menu ->
                val active = menu == activeSubMenu
                TextButton(
                    onClick = { activeSubMenu = menu },
                    colors = ButtonDefaults.textButtonColors(contentColor = if (active) GoldPrimary else Color.White)
                ) {
                    Text(menu, fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Normal, fontSize = 12.sp)
                }
            }
        }

        // Dynamic Subscreen Routing
        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            when (activeSubMenu) {
                "Dashboard" -> {
                    // KPI Panels and Low Stock Alerts
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text("Operational Metrics Overview", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary)
                        }

                        // Low Stock Alert Banner (Inventory System)
                        if (lowStockCount > 0) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                                    border = BorderStroke(1.5.dp, Color.Red)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Filled.Warning, contentDescription = "*", tint = Color.Red, modifier = Modifier.size(36.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text("CRITICAL: LOW STOCK WARNINGS!", color = Color.Red, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                            Text("$lowStockCount product lines fell under buffer levels (<= 10 units limit).", color = Color.DarkGray, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Card(modifier = Modifier.weight(1.0f).padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Cumulative Gross Billing", color = Color.Gray, fontSize = 10.sp)
                                        Text("₹${String.format("%.2f", totalIncome)}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = NavyPrimary)
                                    }
                                }
                                Card(modifier = Modifier.weight(1.0f).padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Orders Executed", color = Color.Gray, fontSize = 10.sp)
                                        Text("${ords.size} Transacted", fontWeight = FontWeight.Black, fontSize = 16.sp, color = NavyPrimary)
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Card(modifier = Modifier.weight(1.0f).padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Pending Dealerships", color = Color.Gray, fontSize = 10.sp)
                                        Text("${dlrs.filter { it.status == "Pending" }.size} Applicants", fontWeight = FontWeight.Black, fontSize = 15.sp, color = GoldDark)
                                    }
                                }
                                Card(modifier = Modifier.weight(1.0f).padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Contact Inquiries", color = Color.Gray, fontSize = 10.sp)
                                        Text("${inqs.size} Queries", fontWeight = FontWeight.Black, fontSize = 15.sp, color = GoldDark)
                                    }
                                }
                            }
                        }

                        // Bullet low inventories summary
                        item {
                            Text("Inventory Critical Low Alert Details", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDark, modifier = Modifier.padding(top = 8.dp))
                        }
                        items(prds.filter { it.stockQuantity <= 10 }) { p ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(p.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                                        Text("Current Stock Level: ${p.stockQuantity}", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                    Button(
                                        onClick = { viewModel.adminAdjustStock(p, 50) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                    ) {
                                        Text("Stock In (+50)", color = NavyDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                "Products" -> {
                    // Product records dashboard list
                    Column(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = { showAddProductDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("admin_add_product_button")
                        ) {
                            Text("Add New Product To Catalog", color = NavyDark, fontWeight = FontWeight.Bold)
                        }

                        if (showAddProductDialog) {
                            AlertDialog(
                                onDismissRequest = { showAddProductDialog = false },
                                title = { Text("Product Setup Form") },
                                text = {
                                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                        OutlinedTextField(value = formProdName, onValueChange = { formProdName = it }, label = { Text("Name") }, modifier = Modifier.testTag("admin_new_prod_name"))
                                        OutlinedTextField(value = formProdCategory, onValueChange = { formProdCategory = it }, label = { Text("Category (Mattress, PU Foam, Pillow, Bonded Foam)") })
                                        OutlinedTextField(value = formProdDesc, onValueChange = { formProdDesc = it }, label = { Text("Description") })
                                        OutlinedTextField(value = formProdPrice, onValueChange = { formProdPrice = it }, label = { Text("Base Price") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                        OutlinedTextField(value = formProdStock, onValueChange = { formProdStock = it }, label = { Text("Starting Stock") })
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            val prc = formProdPrice.toDoubleOrNull() ?: 0.0
                                            val stk = formProdStock.toIntOrNull() ?: 0
                                            if (formProdName.isEmpty() || prc <= 0 || stk < 0) {
                                                Toast.makeText(context, "Invalid input details", Toast.LENGTH_SHORT).show()
                                            } else {
                                                val imgKey = when(formProdCategory) {
                                                    "Mattress" -> "mattress_pocket"
                                                    "PU Foam" -> "foam_pu"
                                                    "Bonded Foam" -> "foam_bonded"
                                                    else -> "pillow_memory"
                                                }
                                                viewModel.adminAddProduct(
                                                    formProdName, formProdCategory, formProdDesc, formProdSpecs, prc, formProdSizes, stk, imgKey
                                                )
                                                showAddProductDialog = false
                                                formProdName = ""
                                                formProdPrice = ""
                                                formProdStock = ""
                                                Toast.makeText(context, "Product appended dynamically inside SQLite!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.testTag("admin_new_prod_submit")
                                    ) { Text("Create Row") }
                                },
                                dismissButton = { TextButton(onClick = { showAddProductDialog = false }) { Text("Cancel") } }
                            )
                        }

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(prds) { product ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ProceduralImage(type = product.imagePlaceholder, modifier = Modifier.size(45.dp).clip(RoundedCornerShape(4.dp)))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1.0f)) {
                                            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                                            Text("₹${product.basePrice} • Stock: ${product.stockQuantity}", fontSize = 11.sp, color = Color.Gray)
                                        }

                                        Row {
                                            IconButton(onClick = { viewModel.adminAdjustStock(product, -10) }) {
                                                Icon(Icons.Filled.RemoveCircle, "Decrease", tint = Color.Red, modifier = Modifier.size(24.dp))
                                            }
                                            IconButton(onClick = { viewModel.adminAdjustStock(product, 10) }) {
                                                Icon(Icons.Filled.AddCircle, "Increase", tint = Color.Green, modifier = Modifier.size(24.dp))
                                            }
                                            IconButton(onClick = { viewModel.adminDeleteProduct(product) }) {
                                                Icon(Icons.Filled.Delete, "Delete", tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Orders" -> {
                    // Orders log tracker with full downloadable Bills
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(ords) { ord ->
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("admin_order_card_${ord.orderId}"),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Invoice: ${ord.invoiceNo}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                                        Text(ord.status.uppercase(), fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = if (ord.status == "Delivered") Color.Green else GoldDark)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Goods: ${ord.productSummary}", fontSize = 12.sp, color = Color.Gray)
                                    Text("Deliver to: ${ord.customerName} | ${ord.customerMobile}", fontSize = 11.sp)
                                    Text("Billing: ₹${ord.totalAmount} (GST: ₹${String.format("%.1f", ord.gstAmount)})", fontSize = 11.sp, fontWeight = FontWeight.Bold)

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Button(
                                                onClick = { viewModel.adminUpdateOrderStatus(ord, "Dispatched") },
                                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text("Dispatch", color = Color.White, fontSize = 10.sp)
                                            }
                                            Button(
                                                onClick = { viewModel.adminUpdateOrderStatus(ord, "Delivered") },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text("Deliver", color = NavyDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        IconButton(onClick = { viewModel.adminDeleteOrder(ord) }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Filled.Delete, "Delete", tint = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Dealers" -> {
                    // Dealership management system
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dlrs) { dlr ->
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("admin_dealer_card_${dlr.id}"),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(dlr.businessName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                                        Text(dlr.status, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (dlr.status == "Approved") Color.Green else Color.Gray)
                                    }
                                    Text("Owner: ${dlr.ownerName} | Mobile: ${dlr.mobile}", fontSize = 11.sp)
                                    Text("GSTIN: ${dlr.gstNumber}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Showroom Address: ${dlr.address}", fontSize = 11.sp, color = Color.Gray)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Button(
                                            onClick = { viewModel.adminUpdateDealerStatus(dlr, "Approved") },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text("Approve", color = NavyDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Button(
                                            onClick = { viewModel.adminUpdateDealerStatus(dlr, "Rejected") },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text("Reject", color = Color.White, fontSize = 10.sp)
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        IconButton(onClick = { viewModel.adminDeleteDealer(dlr) }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Filled.Delete, "Delete", tint = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Inquiries" -> {
                    // Contact desk inquiries
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(inqs) { inq ->
                            Card(
                                modifier = Modifier.fillMaxWidth().testTag("admin_inquiry_card_${inq.id}"),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1.0f)) {
                                        Text("From: ${inq.name} (${inq.city})", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyPrimary)
                                        Text("Mobile Liaison: ${inq.mobile}", fontSize = 11.sp)
                                        Text("Interest Range: ${inq.requirement}", fontSize = 11.sp, color = GoldDark, fontWeight = FontWeight.Bold)
                                        Text("Message: ${inq.message}", fontSize = 11.sp, color = Color.DarkGray)
                                    }
                                    IconButton(onClick = { viewModel.adminDeleteInquiry(inq) }) {
                                        Icon(Icons.Filled.Delete, "Delete Resolved", tint = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }

                "Catalogs" -> {
                    // Manage manuals catalog PDFs
                    Column(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = { showAddPdfDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Text("Append Wholesale Catalog PDF", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        if (showAddPdfDialog) {
                            AlertDialog(
                                onDismissRequest = { showAddPdfDialog = false },
                                title = { Text("Upload Catalog Setup") },
                                text = {
                                    Column {
                                        OutlinedTextField(value = formPdfTitle, onValueChange = { formPdfTitle = it }, label = { Text("Document Title") })
                                        OutlinedTextField(value = formPdfType, onValueChange = { formPdfType = it }, label = { Text("Doc Category (Price List, Brochure, Catalog)") })
                                        OutlinedTextField(value = formPdfFile, onValueChange = { formPdfFile = it }, label = { Text("Simulated Filename (.pdf)") })
                                    }
                                },
                                confirmButton = {
                                    Button(onClick = {
                                        if (formPdfTitle.isEmpty() || formPdfFile.isEmpty()) {
                                            Toast.makeText(context, "Fill titles and filenames", Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.adminAddPdf(formPdfTitle, formPdfType, formPdfFile, "2.4 MB")
                                            showAddPdfDialog = false
                                            formPdfTitle = ""
                                            formPdfFile = ""
                                            Toast.makeText(context, "PDF index register saved!", Toast.LENGTH_SHORT).show()
                                        }
                                    }) { Text("Register") }
                                },
                                dismissButton = { TextButton(onClick = { showAddPdfDialog = false }) { Text("Cancel") } }
                            )
                        }

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(pdfs) { d ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(d.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                                            Text("File: ${d.fileName} | Size: ${d.fileSize}", fontSize = 11.sp, color = Color.Gray)
                                        }
                                        IconButton(onClick = { viewModel.adminDeletePdf(d) }) {
                                            Icon(Icons.Filled.Delete, "Delete", tint = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Inventory Logs" -> {
                    // Log transactions block (Stock In / Stock Out log checklist)
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text("Continuous Stock Transit Audits", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(invLogs) { log ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(log.productName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                                            Text("Action: ${log.logType}", fontSize = 11.sp, color = if (log.changeQty > 0) Color.Green else Color.Red, fontWeight = FontWeight.Bold)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("Change: ${if (log.changeQty > 0) "+" else ""}${log.changeQty}", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                            Text("Balance: ${log.currentStock}", fontSize = 10.sp, color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}


// ==========================================
// 14. CUSTOMER SIGN-IN / PROFILE
// ==========================================
@Composable
fun UserProfileScreen(viewModel: OleafViewModel) {
    val isHindi by viewModel.isHindi.collectAsState()
    val user by viewModel.loggedInUser.collectAsState()
    val mobile by viewModel.userMobile.collectAsState()
    val address by viewModel.userAddress.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val context = LocalContext.current

    // Register login inputs
    var inputName by remember { mutableStateOf("") }
    var inputMobile by remember { mutableStateOf("") }
    var inputAddress by remember { mutableStateOf("") }

    if (user == null) {
        // Customer login / register Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FBFD))
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("customer_login_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, GoldPrimary),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Oleaf Customer Desk", fontWeight = FontWeight.Black, fontSize = 16.sp, color = NavyPrimary)
                    Text("Register or Login to manage sleep orders & wishlists", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputName,
                        onValueChange = { inputName = it },
                        label = { Text("Complete Name") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputMobile,
                        onValueChange = { inputMobile = it },
                        label = { Text("Mobile Contact No") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("reg_mobile_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputAddress,
                        onValueChange = { inputAddress = it },
                        label = { Text("Primary Delivery Address") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_address_input"),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (inputName.trim().isEmpty() || inputMobile.trim().isEmpty()) {
                                Toast.makeText(context, "Fill out a valid name and mobile phone number!", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.registerAndLogin(inputName, inputMobile, inputAddress)
                                Toast.makeText(context, "Authenticated successfully as customer!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        modifier = Modifier.fillMaxWidth().testTag("customer_login_submit")
                    ) {
                        Text("JOIN / LOGIN PORTAL", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    // AUTHENTICATED PROFILE & ORDERS HISTORY
    val userOrders = orders.filter { it.customerMobile == mobile }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBFD))
            .padding(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Account Profile", color = GoldPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Welcome back, $user", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                    Text("Liaison Mobile: $mobile", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("Default Dispatch address: $address", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.logout() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text("Log Out", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text("Your Historical Order Entries", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary, modifier = Modifier.padding(bottom = 8.dp))
        }

        if (userOrders.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        "No purchases recorded against your mobile ledger context.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(userOrders) { o ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Invoice ID: ${o.invoiceNo}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDark)
                            Text(o.status, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (o.status == "Delivered") Color.Green else GoldDark)
                        }
                        Text("Products: ${o.productSummary}", fontSize = 12.sp, color = Color.Gray)
                        Text("Charged Amount: ₹${o.totalAmount} (Includes 18% standard GST)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(90.dp)) }
    }
}
