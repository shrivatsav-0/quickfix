package com.shrivatsav.quickfix

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Hexagon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── FAQ data ──────────────────────────────────────────────────────────────────

private data class FaqCategory(val title: String, val faqs: List<Pair<String, String>>)

private val faqCategories = listOf(
    FaqCategory(
        "Booking",
        listOf(
            "How do I reschedule a service?" to
                    "You can reschedule up to 2 hours before the appointment from the My Bookings section. Tap the booking, then tap \"Change date/time\".",
            "What if the professional doesn't show up?" to
                    "If your pro hasn't arrived within 15 minutes of the expected time, you'll see a \"Get help\" button in the Live Tracking screen. We'll find a replacement or give you a full refund.",
            "Can I request a specific professional?" to
                    "Yes! After your first booking with a pro, you can save them as a favourite and request them directly for future jobs.",
            "How far in advance can I book?" to
                    "You can book up to 30 days in advance. For same-day service, we need at least 1 hour lead time.",
        ),
    ),
    FaqCategory(
        "Payments",
        listOf(
            "When am I charged for the service?" to
                    "Payment is processed only after the service is marked complete by both you and the professional. We hold funds in escrow to ensure your satisfaction.",
            "Do you accept cash payments?" to
                    "Yes, cash is accepted. Select \"Cash\" as your payment method when confirming the booking. No platform fee applies for cash payments.",
            "What is the cancellation policy?" to
                    "Free cancellation up to 2 hours before the scheduled time. Cancellations within 2 hours incur a ₹50 fee. No-shows are charged the full estimated price.",
        ),
    ),
    FaqCategory(
        "Trust & Safety",
        listOf(
            "Are the professionals background-checked?" to
                    "Every professional on QuickFix undergoes a multi-step verification: identity checks, criminal background screening, and skill assessments before they can take bookings.",
            "What is the QuickFix Guarantee?" to
                    "If you're not satisfied with the quality of the work within 7 days, we'll send another pro to fix it at no extra cost. All bookings through our platform are insured up to ₹10,00,000.",
            "Is my payment information secure?" to
                    "All transactions are encrypted with 256-bit SSL. We never store your full card number — only the last 4 digits are kept for display.",
        ),
    ),
    FaqCategory(
        "Pricing",
        listOf(
            "How are service prices determined?" to
                    "Prices are set by professionals and vary by location, complexity and demand. We show you an estimate upfront; the final bill is based on actual time and parts used.",
            "Are there any hidden fees?" to
                    "No hidden fees. The price shown at booking is what you'll pay unless the scope of work changes (which must be approved by you before any extra charges apply).",
        ),
    ),
)

// ─────────────────────────────────────────────────────────────────────────────
// HelpScreen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HelpScreen(modifier: Modifier = Modifier) {
    var expandedIndex by remember { mutableIntStateOf(-1) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Hero
        item {
            Spacer(Modifier.height(8.dp))
            Text("Help & Support", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            Text("Find answers to common questions or get in touch.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
        }

        // FAQ categories — each has an expanding sub-list
        var globalIndex = 0
        faqCategories.forEach { cat ->
            item(key = cat.title) {
                Text(
                    cat.title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
            cat.faqs.forEachIndexed { i, (q, a) ->
                val idx = globalIndex + i
                item(key = "${cat.title}_$i") {
                    FaqRow(
                        question = q,
                        answer = a,
                        isExpanded = expandedIndex == idx,
                        onToggle = { expandedIndex = if (expandedIndex == idx) -1 else idx },
                        showDivider = i < cat.faqs.size - 1,
                        isFirst = i == 0,
                        isLast = i == cat.faqs.size - 1,
                    )
                }
            }
            globalIndex += cat.faqs.size
            item(key = "${cat.title}_spacer") { Spacer(Modifier.height(4.dp)) }
        }

        // Still need help
        item {
            Spacer(Modifier.height(8.dp))
            Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Still need assistance?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Our support team is available 24/7.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ContactChip(Icons.Filled.Chat, "Live Chat", Modifier.weight(1f))
                        ContactChip(Icons.Filled.Mail, "Email", Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ContactChip(Icons.Filled.Call, "Phone", Modifier.weight(1f))
                        ContactChip(Icons.Filled.Hexagon, "Twitter", Modifier.weight(1f))
                    }
                }
            }
        }

        // QuickFix Guarantee
        item {
            Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(Icons.Filled.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                        Text("The QuickFix Guarantee", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        "Your peace of mind is our priority. If you're not satisfied with the quality of the work within 7 days, we'll redo it at no extra cost. Every service booked through QuickFix is insured up to ₹10,00,000.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    listOf(
                        "100% Satisfaction or Money Back",
                        "₹10L Property Damage Protection",
                        "Vetted & Background Checked Pros",
                    ).forEach { point ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Text(point, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// FAQ row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FaqRow(
    question: String,
    answer: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    showDivider: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
) {
    // Round only top corners on first, bottom on last, both on single
    val shape = when {
        isFirst && isLast -> MaterialTheme.shapes.large
        isFirst           -> androidx.compose.foundation.shape.RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        isLast            -> androidx.compose.foundation.shape.RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        else              -> androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
    }
    Surface(modifier = Modifier.fillMaxWidth(), shape = shape, color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(question, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)) + fadeIn(),
                exit = shrinkVertically(spring(Spring.DampingRatioNoBouncy, Spring.StiffnessMedium)) + fadeOut(),
            ) {
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                )
            }
            if (showDivider) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Contact chip
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ContactChip(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.clickable {}, shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surface) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}