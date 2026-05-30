package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- PRODUCT QUERIES ---
    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): Product?

    @Query("SELECT * FROM products WHERE category = :category ORDER BY id DESC")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    // --- ORDER QUERIES ---
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE customerMobile = :mobile ORDER BY orderDate DESC")
    fun getOrdersByMobile(mobile: String): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    // --- DEALER QUERIES ---
    @Query("SELECT * FROM dealers ORDER BY appliedDate DESC")
    fun getAllDealers(): Flow<List<Dealer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDealer(dealer: Dealer): Long

    @Update
    suspend fun updateDealer(dealer: Dealer)

    @Delete
    suspend fun deleteDealer(dealer: Dealer)

    // --- INQUIRY QUERIES ---
    @Query("SELECT * FROM inquiries ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<Inquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: Inquiry): Long

    @Delete
    suspend fun deleteInquiry(inquiry: Inquiry)

    // --- REVIEW QUERIES ---
    @Query("SELECT * FROM reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY timestamp DESC")
    fun getReviewsByProduct(productId: Int): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    // --- GALLERY MEDIA QUERIES ---
    @Query("SELECT * FROM gallery_media ORDER BY id DESC")
    fun getAllGalleryMedia(): Flow<List<GalleryMedia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleryMedia(media: GalleryMedia): Long

    @Delete
    suspend fun deleteGalleryMedia(media: GalleryMedia)

    // --- PDF SECTION QUERIES ---
    @Query("SELECT * FROM pdf_documents ORDER BY id DESC")
    fun getAllPdfDocuments(): Flow<List<PdfDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPdfDocument(pdf: PdfDocument): Long

    @Delete
    suspend fun deletePdfDocument(pdf: PdfDocument)

    // --- INVENTORY LOG QUERIES ---
    @Query("SELECT * FROM inventory_logs ORDER BY timestamp DESC")
    fun getAllInventoryLogs(): Flow<List<InventoryLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryLog(log: InventoryLog): Long
}
