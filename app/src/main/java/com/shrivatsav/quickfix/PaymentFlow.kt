package com.shrivatsav.quickfix

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────────────────────
// Payment screen
// ─────────────────────────────────────────────────────────────────────────────

private data class PayMethod(val id: String, val label: String, val icon: ImageVector, val subtitle: String)

private val payMethods = listOf(
    PayMethod("upi",  "UPI",         Icons.Filled.QrCode,         "Google Pay, PhonePe, Paytm"),
    PayMethod("card", "Card",        Icons.Filled.CreditCard,     "Credit or debit card"),
    PayMethod("nb",   "Net Banking", Icons.Filled.AccountBalance,  "All major banks"),
    PayMethod("cash", "Cash",        Icons.Filled.Money,           "Pay the worker directly"),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PaymentScreen(
    booking: BookingState,
    onPaymentDone: (method: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedMethod by remember { mutableStateOf("upi") }
    var processing by remember { mutableStateOf(false) }
    var showCheck by remember { mutableStateOf(false) }

    LaunchedEffect(processing) {
        if (!processing) return@LaunchedEffect
        delay(1800)
        showCheck = true
        delay(600)
        onPaymentDone(selectedMethod)
    }

    if (processing) {
        // ── M3E payment processing loader ─────────────────────────────────────
        Box(
            modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(96.dp)) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !showCheck,
                        enter = fadeIn(spring(stiffness = Spring.StiffnessMedium)),
                        exit = fadeOut(spring(stiffness = Spring.StiffnessMedium)),
                    ) {
                        ContainedLoadingIndicator(
                            modifier = Modifier.size(96.dp),
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                        )
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showCheck,
                        enter = scaleIn(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)) + fadeIn(),
                    ) {
                        Box(
                            modifier = Modifier.size(96.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(44.dp))
                        }
                    }
                }
                AnimatedContent(
                    targetState = showCheck,
                    transitionSpec = {
                        (fadeIn(spring(stiffness = Spring.StiffnessMedium)) + slideInVertically { it / 3 }) togetherWith
                                (fadeOut(tween(120)) + slideOutVertically { -it / 3 })
                    },
                    label = "pay_label",
                ) { done ->
                    Text(
                        text = if (done) "Payment Successful!" else "Processing payment…",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Secured by 256-bit encryption", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(16.dp))
        Text("Choose payment", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
        Text("method.", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(6.dp))
        Text("Select how you'd like to pay for this service.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(24.dp))

        // Amount summary
        Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("AMOUNT DUE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), letterSpacing = 1.sp)
                    Text("₹${booking.totalAmount}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary)
                }
                Icon(Icons.Filled.Receipt, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("PAYMENT METHOD", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
        Spacer(Modifier.height(12.dp))

        payMethods.forEach { method ->
            val sel = selectedMethod == method.id
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .clip(MaterialTheme.shapes.large)
                    .border(if (sel) 2.dp else 0.dp, if (sel) MaterialTheme.colorScheme.primary else Color.Transparent, MaterialTheme.shapes.large)
                    .clickable { selectedMethod = method.id },
                shape = MaterialTheme.shapes.large,
                color = if (sel) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape)
                            .background(if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(method.icon, contentDescription = null, tint = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(22.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(method.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                        Text(method.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    if (sel) {
                        Box(modifier = Modifier.size(22.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(12.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("All payments are encrypted and secure", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { processing = true },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Text("Pay ₹${booking.totalAmount}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        Spacer(Modifier.height(24.dp))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Payment confirmation screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PaymentConfirmScreen(
    booking: BookingState,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaleAnim = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(36.dp))

        // Animated check
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(52.dp))
        }

        Spacer(Modifier.height(20.dp))
        Text("Payment\nConfirmed!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, lineHeight = 36.sp)
        Spacer(Modifier.height(8.dp))
        Text("₹${booking.totalAmount} paid via ${booking.paymentMethod}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)

        Spacer(Modifier.height(28.dp))

        // Receipt card
        Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Receipt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                ReceiptRow("Service", booking.selectedSubService?.name ?: "–")
                ReceiptRow("Category", booking.selectedCategory?.name ?: "–")
                ReceiptRow("Pro", booking.workerName)
                ReceiptRow("Duration", booking.selectedSubService?.estimatedDuration ?: "–")
                ReceiptRow("Payment", booking.paymentMethod)

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Paid", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("₹${booking.totalAmount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Rate the pro
        Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Rate ${booking.workerName.split(" ").first()}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(5) { i ->
                        Icon(Icons.Filled.Star, contentDescription = null, modifier = Modifier.size(28.dp).clickable {}, tint = if (i < 4) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                    }
                }
                Text("How was your experience?", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onReturnHome,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Icon(Icons.Filled.Home, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Return Home", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        Spacer(Modifier.height(12.dp))

        FilledTonalButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp), shape = MaterialTheme.shapes.extraLarge) {
            Icon(Icons.Filled.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Download Receipt")
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}