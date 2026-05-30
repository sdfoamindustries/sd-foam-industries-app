package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CartItem(
    val product: Product,
    val selectedSize: String,
    val quantity: Int
)

class OleafViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OleafRepository
    val isHindi = MutableStateFlow(false)

    // Reactive StateFlow bindings to Room DAO
    val allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allOrders = MutableStateFlow<List<Order>>(emptyList())
    val allDealers = MutableStateFlow<List<Dealer>>(emptyList())
    val allInquiries = MutableStateFlow<List<Inquiry>>(emptyList())
    val allReviews = MutableStateFlow<List<Review>>(emptyList())
    val allGalleryMedia = MutableStateFlow<List<GalleryMedia>>(emptyList())
    val allPdfDocuments = MutableStateFlow<List<PdfDocument>>(emptyList())
    val allInventoryLogs = MutableStateFlow<List<InventoryLog>>(emptyList())

    // App state flows (Cart, User, Wishlist)
    val cart = MutableStateFlow<List<CartItem>>(emptyList())
    val wishlist = MutableStateFlow<Set<Int>>(emptySet())
    val loggedInUser = MutableStateFlow<String?>(null) // Name of user
    val userMobile = MutableStateFlow<String?>(null)
    val userAddress = MutableStateFlow<String?>(null)

    // Custom Size temporary state
    val customOrderSuccess = MutableStateFlow<String?>(null)

    init {
        val database = OleafDatabase.getDatabase(application)
        repository = OleafRepository(database.appDao())

        viewModelScope.launch {
            // Seed base enterprise products, brochures, and media if empty
            repository.seedDatabaseIfNeeded()

            // Observe data
            launch { repository.allProducts.collect { allProducts.value = it } }
            launch { repository.allOrders.collect { allOrders.value = it } }
            launch { repository.allDealers.collect { allDealers.value = it } }
            launch { repository.allInquiries.collect { allInquiries.value = it } }
            launch { repository.allReviews.collect { allReviews.value = it } }
            launch { repository.allGalleryMedia.collect { allGalleryMedia.value = it } }
            launch { repository.allPdfDocuments.collect { allPdfDocuments.value = it } }
            launch { repository.allInventoryLogs.collect { allInventoryLogs.value = it } }
        }
    }

    // --- LANGUAGE SWITCHER ---
    fun toggleLanguage() {
        isHindi.value = !isHindi.value
    }

    // --- AUTHENTICATION ---
    fun registerAndLogin(name: String, mobile: String, address: String) {
        loggedInUser.value = name
        userMobile.value = mobile
        userAddress.value = address
    }

    fun logout() {
        loggedInUser.value = null
        userMobile.value = null
        userAddress.value = null
        cart.value = emptyList()
    }

    // --- WISHLIST ---
    fun toggleWishlist(productId: Int) {
        val current = wishlist.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        wishlist.value = current
    }

    // --- SHOPPING CART ---
    fun addToCart(product: Product, size: String, qty: Int = 1) {
        val current = cart.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id && it.selectedSize == size }
        if (index != -1) {
            current[index] = current[index].copy(quantity = current[index].quantity + qty)
        } else {
            current.add(CartItem(product, size, qty))
        }
        cart.value = current
    }

    fun updateCartQuantity(product: Product, size: String, qty: Int) {
        val current = cart.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id && it.selectedSize == size }
        if (index != -1) {
            if (qty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = qty)
            }
        }
        cart.value = current
    }

    fun removeFromCart(product: Product, size: String) {
        val current = cart.value.toMutableList()
        current.removeAll { it.product.id == product.id && it.selectedSize == size }
        cart.value = current
    }

    fun clearCart() {
        cart.value = emptyList()
    }

    // --- ORDERS & PAYMENTS (auto GST invoice) ---
    fun placeOrder(
        clientName: String,
        clientMobile: String,
        clientAddress: String,
        pm: String, // COD, UPI, Card, Net Banking, Stripe
        orderType: String = "Retail", // Retail, Bulk, Dealer
        isCustom: Boolean = false,
        customDims: String = "",
        customPrice: Double = 0.0
    ) {
        viewModelScope.launch {
            val totalAmt = if (isCustom) customPrice else cart.value.sumOf { it.product.basePrice * it.quantity }
            val summary = if (isCustom) {
                "Custom Size Mattress ($customDims) x1"
            } else {
                cart.value.joinToString(", ") { "${it.product.name} (${it.selectedSize}) x${it.quantity}" }
            }

            val gst = totalAmt * 0.18 // 18% GST auto calculation
            val invoiceNumber = "OD-${System.currentTimeMillis().toString().takeLast(6)}"

            val newOrder = Order(
                customerName = clientName,
                customerMobile = clientMobile,
                address = clientAddress,
                productSummary = summary,
                totalAmount = totalAmt,
                paymentMethod = pm,
                orderType = orderType,
                isCustomSize = isCustom,
                customDimensions = customDims,
                gstAmount = gst,
                invoiceNo = invoiceNumber,
                status = "Pending"
            )

            val orderId = repository.insertOrder(newOrder)

            // Dynamic Inventory Stock-out event
            if (!isCustom) {
                for (item in cart.value) {
                    val p = item.product
                    val currentStock = p.stockQuantity - item.quantity
                    val updatedProduct = p.copy(stockQuantity = if (currentStock < 0) 0 else currentStock)
                    repository.updateProduct(updatedProduct)

                    // Logs stock decrease
                    repository.insertInventoryLog(
                        InventoryLog(
                            productId = p.id,
                            productName = p.name,
                            changeQty = -item.quantity,
                            logType = "Stock Out",
                            currentStock = if (currentStock < 0) 0 else currentStock
                        )
                    )
                }
            }

            clearCart()
        }
    }

    // --- BUSINESS INQUIRY SHEET ---
    fun submitInquiry(name: String, mobile: String, city: String, requirement: String, message: String) {
        viewModelScope.launch {
            repository.insertInquiry(
                Inquiry(
                    name = name,
                    mobile = mobile,
                    city = city,
                    requirement = requirement,
                    message = message
                )
            )
        }
    }

    // --- DEALER REGISTRATION SHEET ---
    fun submitDealerRequest(bName: String, oName: String, mobile: String, gst: String, addr: String) {
        viewModelScope.launch {
            repository.insertDealer(
                Dealer(
                    businessName = bName,
                    ownerName = oName,
                    mobile = mobile,
                    gstNumber = gst,
                    address = addr
                )
            )
        }
    }

    // --- PRODUCT REVIEWS ---
    fun addProductReview(productId: Int, productName: String, name: String, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.insertReview(
                Review(
                    productId = productId,
                    productName = productName,
                    customerName = name,
                    rating = rating,
                    comment = comment
                )
            )
        }
    }

    // --- ADMIN OVERRIDES FOR ALL DATA ---
    fun adminAddProduct(name: String, category: String, desc: String, specs: String, price: Double, sizes: String, stock: Int, imagePlaceholder: String) {
        viewModelScope.launch {
            val p = Product(
                name = name,
                category = category,
                description = desc,
                specifications = specs,
                basePrice = price,
                sizes = sizes,
                stockQuantity = stock,
                imagePlaceholder = imagePlaceholder,
                videoUrl = ""
            )
            val pid = repository.insertProduct(p)
            // Log inventory initialization
            repository.insertInventoryLog(
                InventoryLog(
                    productId = pid.toInt(),
                    productName = name,
                    changeQty = stock,
                    logType = "Stock In",
                    currentStock = stock
                )
            )
        }
    }

    fun adminUpdateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun adminAdjustStock(p: Product, change: Int) {
        viewModelScope.launch {
            val nextStock = (p.stockQuantity + change).coerceAtLeast(0)
            repository.updateProduct(p.copy(stockQuantity = nextStock))
            repository.insertInventoryLog(
                InventoryLog(
                    productId = p.id,
                    productName = p.name,
                    changeQty = change,
                    logType = if (change > 0) "Stock In" else "Stock Out",
                    currentStock = nextStock
                )
            )
        }
    }

    fun adminDeleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun adminUpdateOrderStatus(order: Order, nextStatus: String) {
        viewModelScope.launch {
            repository.updateOrder(order.copy(status = nextStatus))
        }
    }

    fun adminUpdateDealerStatus(dealer: Dealer, nextStatus: String) {
        viewModelScope.launch {
            repository.updateDealer(dealer.copy(status = nextStatus))
        }
    }

    fun adminAddGalleryPhoto(title: String, category: String, tag: String) {
        viewModelScope.launch {
            repository.insertGalleryMedia(
                GalleryMedia(
                    mediaType = "Photo",
                    category = category,
                    title = title,
                    url = tag
                )
            )
        }
    }

    fun adminAddGalleryVideo(title: String, category: String, url: String) {
        viewModelScope.launch {
            repository.insertGalleryMedia(
                GalleryMedia(
                    mediaType = "Video",
                    category = category,
                    title = title,
                    url = url
                )
            )
        }
    }

    fun adminAddPdf(title: String, type: String, file: String, sizeString: String) {
        viewModelScope.launch {
            repository.insertPdfDocument(
                PdfDocument(
                    title = title,
                    docType = type,
                    fileName = file,
                    fileSize = sizeString
                )
            )
        }
    }

    fun adminDeletePdf(pdf: PdfDocument) {
        viewModelScope.launch {
            repository.deletePdfDocument(pdf)
        }
    }

    fun adminDeleteInquiry(inquiry: Inquiry) {
        viewModelScope.launch {
            repository.deleteInquiry(inquiry)
        }
    }

    fun adminDeleteDealer(dealer: Dealer) {
        viewModelScope.launch {
            repository.deleteDealer(dealer)
        }
    }

    fun adminDeleteOrder(order: Order) {
        viewModelScope.launch {
            repository.deleteOrder(order)
        }
    }
}
