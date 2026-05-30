package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class OleafRepository(private val appDao: AppDao) {

    val allProducts: Flow<List<Product>> = appDao.getAllProducts()
    val allOrders: Flow<List<Order>> = appDao.getAllOrders()
    val allDealers: Flow<List<Dealer>> = appDao.getAllDealers()
    val allInquiries: Flow<List<Inquiry>> = appDao.getAllInquiries()
    val allReviews: Flow<List<Review>> = appDao.getAllReviews()
    val allGalleryMedia: Flow<List<GalleryMedia>> = appDao.getAllGalleryMedia()
    val allPdfDocuments: Flow<List<PdfDocument>> = appDao.getAllPdfDocuments()
    val allInventoryLogs: Flow<List<InventoryLog>> = appDao.getAllInventoryLogs()

    fun getProductsByCategory(category: String): Flow<List<Product>> =
        appDao.getProductsByCategory(category)

    fun getOrdersByMobile(mobile: String): Flow<List<Order>> =
        appDao.getOrdersByMobile(mobile)

    fun getReviewsByProduct(productId: Int): Flow<List<Review>> =
        appDao.getReviewsByProduct(productId)

    suspend fun getProductById(id: Int): Product? =
        appDao.getProductById(id)

    // --- WRITE OPERATIONS ---
    suspend fun insertProduct(product: Product): Long = appDao.insertProduct(product)
    suspend fun updateProduct(product: Product) = appDao.updateProduct(product)
    suspend fun deleteProduct(product: Product) = appDao.deleteProduct(product)

    suspend fun insertOrder(order: Order): Long = appDao.insertOrder(order)
    suspend fun updateOrder(order: Order) = appDao.updateOrder(order)
    suspend fun deleteOrder(order: Order) = appDao.deleteOrder(order)

    suspend fun insertDealer(dealer: Dealer): Long = appDao.insertDealer(dealer)
    suspend fun updateDealer(dealer: Dealer) = appDao.updateDealer(dealer)
    suspend fun deleteDealer(dealer: Dealer) = appDao.deleteDealer(dealer)

    suspend fun insertInquiry(inquiry: Inquiry): Long = appDao.insertInquiry(inquiry)
    suspend fun deleteInquiry(inquiry: Inquiry) = appDao.deleteInquiry(inquiry)

    suspend fun insertReview(review: Review): Long = appDao.insertReview(review)

    suspend fun insertGalleryMedia(media: GalleryMedia): Long = appDao.insertGalleryMedia(media)
    suspend fun deleteGalleryMedia(media: GalleryMedia) = appDao.deleteGalleryMedia(media)

    suspend fun insertPdfDocument(pdf: PdfDocument): Long = appDao.insertPdfDocument(pdf)
    suspend fun deletePdfDocument(pdf: PdfDocument) = appDao.deletePdfDocument(pdf)

    suspend fun insertInventoryLog(log: InventoryLog): Long = appDao.insertInventoryLog(log)

    // --- DATABASE SEEDER ---
    suspend fun seedDatabaseIfNeeded() {
        val existingProducts = allProducts.first()
        if (existingProducts.isEmpty()) {
            // Seed premium products
            val seedProducts = listOf(
                Product(
                    name = "Oleaf Ortho Comfort Mattress",
                    category = "Mattress",
                    description = "Engineered with orthopaedic doctor-recommended posture alignment support. Features high-density bonded foam with a gold-threaded breathable memory foam cover for unparalleled luxury and zero partner disturbance.",
                    specifications = "Material: High Density Bonded Foam & Memory Foam;Firmness: Medium Firm;Warranty: 10 Years;Thickness: 5, 6, 8 inches;Specialty: Advanced spine alignment technology",
                    basePrice = 14999.00,
                    sizes = "Single (72x36),Double (72x48),Queen (72x60),King (72x72)",
                    stockQuantity = 45,
                    imagePlaceholder = "mattress_ortho",
                    videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                    isCustomizable = true
                ),
                Product(
                    name = "Oleaf Pocket Spring Luxury Mattress",
                    category = "Mattress",
                    description = "Our flagship luxury masterwork. Individually encased pocket springs deliver precision relief to every curve of your body, overlaid with organic latex foam and quilted velvet fabric. Zero partner motion transfer guarantees deep sleep.",
                    specifications = "Material: Pocket Springs & Natural Organic Latex;Firmness: Medium Soft;Warranty: 12 Years;Thickness: 6, 8, 10 inches;Specialty: Pocket spring isolation with organic cashmere layer",
                    basePrice = 19499.00,
                    sizes = "Single (72x36),Double (72x48),Queen (72x60),King (72x72)",
                    stockQuantity = 30,
                    imagePlaceholder = "mattress_pocket",
                    videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                    isCustomizable = true
                ),
                Product(
                    name = "Oleaf Super Soft PU Foam Sheet",
                    category = "PU Foam",
                    description = "World class high resilience polyurethane foam direct from the SD Foam modern factory. Engineered with uniform cellular structure, delivering exceptional recovery rate and supreme flexibility for high-end sofa and comfort seating accessories.",
                    specifications = "Grade: Ultra Soft HR;Density: 32 Density to 40 Density;Sizes: 72x36, 75x60;Thickness: 2, 3, 4 inches;Color: Sky Blue Profile",
                    basePrice = 2899.00,
                    sizes = "Standard (72x36),Large (75x60)",
                    stockQuantity = 120,
                    imagePlaceholder = "foam_pu",
                    videoUrl = "",
                    isCustomizable = false
                ),
                Product(
                    name = "Oleaf Rebonded Ortho Core Block",
                    category = "Bonded Foam",
                    description = "Premium class heavy density rebonded foam block developed for dual-use applications: professional orthopaedic mattress cores and corporate heavy-duty acoustic soundproof panels. Resistant to sag and decomposition.",
                    specifications = "Density: 80 - 100 kg/m3;Material: High pressure thermal bonded recycled PU;Warranty: 5 Years;Usage: Heavy mattress core & acoustical sound absorption",
                    basePrice = 4599.00,
                    sizes = "Standard (72x36),Double (72x48),Queen (72x60)",
                    stockQuantity = 80,
                    imagePlaceholder = "foam_bonded",
                    videoUrl = "",
                    isCustomizable = false
                ),
                Product(
                    name = "Oleaf Contour Memory Foam Pillow",
                    category = "Pillow",
                    description = "Ergonomically contoured pillow designed to fit the natural curvature of your head, neck, and shoulder. Infused with cooling gel micro-spheres to regulate sleep temperature and promote fresh muscle recovery.",
                    specifications = "Material: Gel-infused Memory Foam;Cover: Bamboo organic breathable zippered case;Dimensions: 24 x 16 inches x 4.5 thickness;Specialty: Anti-bacterial neck alignment support",
                    basePrice = 1499.00,
                    sizes = "Standard (24x16)",
                    stockQuantity = 200,
                    imagePlaceholder = "pillow_memory",
                    videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                    isCustomizable = false
                )
            )
            for (prod in seedProducts) {
                appDao.insertProduct(prod)
            }
        }

        val existingPdf = allPdfDocuments.first()
        if (existingPdf.isEmpty()) {
            val seedPdfs = listOf(
                PdfDocument(title = "Oleaf Mattress Premium Catalog 2026", docType = "Catalog", fileName = "Oleaf_Catalog_2026.pdf", fileSize = "6.8 MB"),
                PdfDocument(title = "SD Foam Institutional Distributor Price List", docType = "Price List", fileName = "SD_Foam_Price_List_Wholesale.pdf", fileSize = "1.2 MB"),
                PdfDocument(title = "Ortho Foam & Spine Health Brochure", docType = "Brochure", fileName = "Postural_Health_Brochure.pdf", fileSize = "3.4 MB")
            )
            for (p in seedPdfs) {
                appDao.insertPdfDocument(p)
            }
        }

        val existingMedia = allGalleryMedia.first()
        if (existingMedia.isEmpty()) {
            val seedMedia = listOf(
                GalleryMedia(mediaType = "Photo", category = "Factory", title = "German Foam Foaming Line", url = "factory_german_line"),
                GalleryMedia(mediaType = "Photo", category = "Factory", title = "Precision Mattress Quilting CNC", url = "factory_cnc_quilting"),
                GalleryMedia(mediaType = "Photo", category = "Mattress", title = "Oleaf Luxurious Gold Layer Stitching", url = "mattress_quilt_detail"),
                GalleryMedia(mediaType = "Photo", category = "Foam", title = "Dense Rebonded Foam Blocks Stacked", url = "foam_blocks_stack"),
                GalleryMedia(mediaType = "Photo", category = "Customer", title = "Delighted Family in New Delhi Home", url = "customer_review_bed"),
                GalleryMedia(mediaType = "Video", category = "Factory", title = "Behind the Scenes: Foam Block Curing", url = "video_block_curing"),
                GalleryMedia(mediaType = "Video", category = "Factory", title = "Mattress Core Compression Testing", url = "video_compression_test"),
                GalleryMedia(mediaType = "Video", category = "Customer", title = "Unboxing Oleaf Smart Roll-Pack Mattress", url = "video_unboxing")
            )
            for (m in seedMedia) {
                appDao.insertGalleryMedia(m)
            }
        }

        val existingReviews = allReviews.first()
        if (existingReviews.isEmpty()) {
            val seedReviews = listOf(
                Review(productId = 1, productName = "Oleaf Ortho Comfort Mattress", customerName = "Arvind Sharma (Gurugram)", rating = 5, comment = "Excellent back spine support. My persistent morning back ache of 2 years solved inside one week! Truly direct from factory best price.", timestamp = System.currentTimeMillis() - 86400000),
                Review(productId = 2, productName = "Oleaf Pocket Spring Luxury Mattress", customerName = "Priya Patel (Ahmedabad)", rating = 5, comment = "Amazing. Zero motion transfer, luxury gold embroidery feels supreme, premium hotel like sleep at home. Delivery was fast too.", timestamp = System.currentTimeMillis() - 172800000),
                Review(productId = 1, productName = "Oleaf Ortho Comfort Mattress", customerName = "Rajesh Gupta (New Delhi)", rating = 4, comment = "Very solid build quality. Customer service customized the length exactly to 74 inches inside 2 hours of chat! Fully recommended.", timestamp = System.currentTimeMillis() - 259200000)
            )
            for (r in seedReviews) {
                appDao.insertReview(r)
            }
        }
    }
}
