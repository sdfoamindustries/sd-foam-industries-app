package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

// --- SYSTEM TRANSLATIONS (ENGLISH / HINDI) ---
object Translator {
    private val dictionary = mapOf(
        "sd_foam" to Pair("S D FOAM INDUSTRIES", "एस डी फोम इंडस्ट्रीज"),
        "oleaf" to Pair("Oleaf Mattress", "ओलीफ मैट्रेस"),
        "tagline" to Pair("हर रात मिले सुकून भरी नींद", "हर रात मिले सुकून भरी नींद"),
        "sub_tagline" to Pair("World Class Comfort, Direct from Factory", "विश्व स्तरीय आराम, सीधे फैक्ट्री से"),
        "home" to Pair("Home", "होम"),
        "products" to Pair("Categories", "उत्पाद श्रेणियां"),
        "cart" to Pair("Cart", "कार्ट"),
        "pdf_center" to Pair("PDF Center", "पीडीएफ"),
        "gallery" to Pair("Gallery", "गैलरी"),
        "factory" to Pair("Factory", "फैक्ट्री"),
        "admin" to Pair("Admin", "एडमिन"),
        "dealer" to Pair("Be a Dealer", "डीलर बनें"),
        "contact" to Pair("Inquiry", "पूछताछ"),
        "profile" to Pair("Account", "प्रोफ़ाइल"),
        "buy_now" to Pair("Buy Now", "अभी खरीदें"),
        "add_to_cart" to Pair("Add to Cart", "कार्ट में जोड़ें"),
        "get_quote" to Pair("Get Quote", "कोटेशन लें"),
        "whatsapp_btn" to Pair("WhatsApp Inquiry", "व्हाट्सएप पूछताछ"),
        "sizes_avail" to Pair("Available Sizes", "उपलब्ध आकार"),
        "specs" to Pair("Specifications", "विशेष विवरण"),
        "custom_size" to Pair("Custom Size Mattress", "कस्टम साइज गद्दे"),
        "reviews" to Pair("Customer Reviews", "ग्राहक समीक्षा"),
        "rating" to Pair("Rating & Reviews", "रेटिंग एवं समीक्षाएं"),
        "stock_alert" to Pair("Low Stock", "कम स्टॉक अलर्ट")
    )

    fun translate(key: String, isHindi: Boolean): String {
        val entry = dictionary[key] ?: return key
        return if (isHindi) entry.second else entry.first
    }
}

// Custom Drawers for procedurally beautiful images
@Composable
fun DrawSDFoamLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(60.dp)) {
        val w = size.width
        val h = size.height

        // Background circle Navy
        drawCircle(
            color = NavyPrimary,
            radius = w / 2,
            center = Offset(w / 2, h / 2)
        )

        // Outer gold borders
        drawCircle(
            color = GoldPrimary,
            radius = (w / 2) - 4f,
            center = Offset(w / 2, h / 2),
            style = Stroke(width = 3f)
        )

        // Abstract S & D Foam Block shapes
        drawRoundRect(
            color = GoldPrimary,
            topLeft = Offset(w * 0.28f, h * 0.35f),
            size = Size(w * 0.44f, h * 0.3f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = 5f)
        )

        drawRect(
            color = Color.White,
            topLeft = Offset(w * 0.45f, h * 0.28f),
            size = Size(w * 0.1f, h * 0.44f)
        )
    }
}

@Composable
fun DrawOleafLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(80.dp)) {
        val w = size.width
        val h = size.height

        // Outer elegant golden ring
        drawCircle(
            color = GoldPrimary,
            radius = w * 0.45f,
            center = Offset(w / 2, h / 2),
            style = Stroke(width = 4f)
        )

        // Draw elegant decorative leaves (representing Oleaf organically)
        // Main stem
        drawLine(
            color = GoldPrimary,
            start = Offset(w * 0.5f, h * 0.8f),
            end = Offset(w * 0.5f, h * 0.25f),
            strokeWidth = 6f
        )

        // Leaf 1
        drawOval(
            color = Color.White,
            topLeft = Offset(w * 0.25f, h * 0.35f),
            size = Size(w * 0.25f, h * 0.18f)
        )
        drawOval(
            color = GoldPrimary,
            topLeft = Offset(w * 0.25f, h * 0.35f),
            size = Size(w * 0.25f, h * 0.18f),
            style = Stroke(width = 3f)
        )

        // Leaf 2
        drawOval(
            color = Color.White,
            topLeft = Offset(w * 0.5f, h * 0.45f),
            size = Size(w * 0.25f, h * 0.18f)
        )
        drawOval(
            color = GoldPrimary,
            topLeft = Offset(w * 0.5f, h * 0.45f),
            size = Size(w * 0.25f, h * 0.18f),
            style = Stroke(width = 3f)
        )
    }
}

@Composable
fun ProceduralImage(type: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (type) {
                "mattress_ortho" -> {
                    // Luxurious mattress drawing
                    drawRect(
                        brush = Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    // Quilted Diamond pattern in Gold
                    for (i in 0..10) {
                        drawLine(
                            color = GoldPrimary.copy(alpha = 0.25f),
                            start = Offset(0f, (h / 10) * i),
                            end = Offset(w, (h / 10) * i + w),
                            strokeWidth = 2f
                        )
                        drawLine(
                            color = GoldPrimary.copy(alpha = 0.25f),
                            start = Offset(w, (h / 10) * i),
                            end = Offset(0f, (h / 10) * i + w),
                            strokeWidth = 2f
                        )
                    }
                    // Border Stitching
                    drawRoundRect(
                        color = GoldPrimary,
                        topLeft = Offset(10f, 10f),
                        size = Size(w - 20f, h - 20f),
                        cornerRadius = CornerRadius(16f, 16f),
                        style = Stroke(width = 4f)
                    )
                    // Brand Badge
                    drawRect(
                        color = NavyDark,
                        topLeft = Offset(w * 0.1f, h * 0.1f),
                        size = Size(w * 0.42f, h * 0.22f)
                    )
                    drawRect(
                        color = GoldPrimary,
                        topLeft = Offset(w * 0.1f, h * 0.1f),
                        size = Size(w * 0.42f, h * 0.22f),
                        style = Stroke(width = 2f)
                    )
                }
                "mattress_pocket" -> {
                    // Elegant dark mattress with spring concept
                    drawRect(
                        brush = Brush.radialGradient(listOf(NavyPrimary, NavyDark)),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    // Pocket Sprung waves
                    for (x in 2..8) {
                        for (y in 2..8) {
                            drawCircle(
                                color = GoldPrimary.copy(alpha = 0.12f),
                                radius = w * 0.04f,
                                center = Offset((w / 10) * x, (h / 10) * y)
                            )
                        }
                    }
                    // Gold stitched crest
                    drawRoundRect(
                        color = GoldPrimary,
                        topLeft = Offset(12f, 12f),
                        size = Size(w - 24f, h - 24f),
                        cornerRadius = CornerRadius(12f, 12f),
                        style = Stroke(width = 4f)
                    )
                }
                "foam_pu" -> {
                    // Porous foam texture sheet
                    drawRect(
                        color = Color(0xFFB3E5FC), // Sky blue soft foam PU
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    // Porous cell drawing dots
                    for (i in 0..120) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.5f),
                            radius = 6f,
                            center = Offset((0..100).random() * w / 100f, (0..100).random() * h / 100f)
                        )
                    }
                }
                "foam_bonded" -> {
                    // Rebonded multicolored compression block look
                    drawRect(
                        color = Color(0xFFECEFF1),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    // Compressed foam crumbs
                    val chips = listOf(Color(0xFFE57373), Color(0xFF81C784), Color(0xFF64B5F6), Color(0xFFFFB74D), Color(0xFF90A4AE))
                    for (i in 0..90) {
                        drawRect(
                            color = chips.random().copy(alpha = 0.45f),
                            topLeft = Offset((0..100).random() * w / 100f, (0..100).random() * h / 100f),
                            size = Size(w * 0.15f, h * 0.08f)
                        )
                    }
                }
                "pillow_memory" -> {
                    // Contour memory foam pillow
                    drawRect(
                        color = Color(0xFFF1F1F5),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    // Waves modeling neck contour support curve
                    drawLine(
                        color = NavyPrimary.copy(alpha = 0.4f),
                        start = Offset(w * 0.1f, h * 0.5f),
                        end = Offset(w * 0.9f, h * 0.5f),
                        strokeWidth = 8f
                    )
                    drawLine(
                        color = GoldPrimary,
                        start = Offset(w * 0.1f, h * 0.35f),
                        end = Offset(w * 0.9f, h * 0.35f),
                        strokeWidth = 3f
                    )
                }
                "factory_german_line" -> {
                    // Abstract production apparatus block
                    drawRect(
                        brush = Brush.verticalGradient(listOf(Color(0xFF37474F), Color(0xFF212121))),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    drawRoundRect(
                        color = GoldPrimary,
                        topLeft = Offset(w * 0.1f, h * 0.3f),
                        size = Size(w * 0.8f, h * 0.4f),
                        cornerRadius = CornerRadius(8f, 8f),
                        style = Stroke(width = 3f)
                    )
                }
                "factory_cnc_quilting" -> {
                    // Needle CNC head simulator
                    drawRect(
                        brush = Brush.sweepGradient(listOf(Color(0xFF546E7A), Color(0xFF263238))),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    drawLine(
                        color = GoldPrimary,
                        start = Offset(w * 0.5f, 0f),
                        end = Offset(w * 0.5f, h),
                        strokeWidth = 5f
                    )
                }
                else -> {
                    // Default Gold and Navy pattern
                    drawRect(
                        brush = Brush.linearGradient(listOf(NavyPrimary, NavyDark)),
                        topLeft = Offset(0f, 0f),
                        size = size
                    )
                    drawCircle(
                        color = GoldPrimary,
                        radius = (w / 10),
                        center = Offset(w/2, h/2),
                        style = Stroke(width = 2f)
                    )
                }
            }
        }
    }
}
