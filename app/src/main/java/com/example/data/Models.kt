package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // Mattress, PU Foam, Bonded Foam, Pillow
    val description: String,
    val specifications: String, // Semi-colon separated
    val basePrice: Double,
    val sizes: String, // Comma separated, e.g. "Single (72x36), Double (72x48), Queen (72x60)"
    val stockQuantity: Int,
    val imagePlaceholder: String, // Visual description/asset key
    val videoUrl: String,
    val isCustomizable: Boolean = true
) : Serializable

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val customerName: String,
    val customerMobile: String,
    val address: String,
    val productSummary: String, // "Oleaf Luxury Mattress (Queen) x1"
    val totalAmount: Double,
    val paymentMethod: String, // "COD", "UPI", "Credit/Debit Card", "Net Banking", "Stripe"
    val orderType: String, // "Retail", "Bulk", "Dealer"
    val isCustomSize: Boolean = false,
    val customDimensions: String = "", // "Length: 72, Width: 48, Thickness: 6, Qty: 2"
    val status: String = "Pending", // Pending, Dispatched, Delivered
    val orderDate: Long = System.currentTimeMillis(),
    val gstAmount: Double = 0.0,
    val invoiceNo: String = ""
) : Serializable

@Entity(tableName = "dealers")
data class Dealer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val businessName: String,
    val ownerName: String,
    val mobile: String,
    val gstNumber: String,
    val address: String,
    val status: String = "Pending", // Pending, Approved, Rejected
    val appliedDate: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "inquiries")
data class Inquiry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val mobile: String,
    val city: String,
    val requirement: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val customerName: String,
    val rating: Int, // 1 to 5 stars
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "gallery_media")
data class GalleryMedia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mediaType: String, // "Photo", "Video"
    val category: String, // "Factory", "Mattress", "Foam", "Customer"
    val title: String,
    val url: String // local placeholder tag or descriptive URI
) : Serializable

@Entity(tableName = "pdf_documents")
data class PdfDocument(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val docType: String, // "Catalog", "Price List", "Brochure"
    val fileName: String,
    val fileSize: String
) : Serializable

@Entity(tableName = "inventory_logs")
data class InventoryLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val changeQty: Int, // positive for addition, negative for deduction
    val logType: String, // "Stock In", "Stock Out", "Adjustment"
    val currentStock: Int,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
