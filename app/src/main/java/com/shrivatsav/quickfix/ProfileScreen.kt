package com.shrivatsav.quickfix

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// ── Modal sheet selector ──────────────────────────────────────────────────────

private enum class ProfileModal {
    Bookings, Payments, PersonalInfo, PaymentMethods, Notifications, Security, None
}

// ── Sample data ───────────────────────────────────────────────────────────────

private data class ProfilePastBooking(
    val id: String,
    val service: String,
    val category: String,
    val worker: String,
    val rating: Double,
    val date: String,
    val amount: Int,
    val status: String,
)

private val sampleBookings = listOf(
    ProfilePastBooking("1", "Leak Repair", "Plumbing", "Rajan K.", 4.9, "15 Apr 2025", 650, "Completed"),
    ProfilePastBooking("2", "Fan Installation", "Electrical", "Suresh M.", 4.8, "2 Apr 2025", 350, "Completed"),
    ProfilePastBooking("3", "Home Cleaning", "Cleaning", "Priya S.", 5.0, "20 Mar 2025", 1200, "Completed"),
    ProfilePastBooking("4", "AC Service", "AC & Appliances", "Arun T.", 4.7, "5 Mar 2025", 799, "Completed"),
    ProfilePastBooking("5", "Pipe Installation", "Plumbing", "Rajan K.", 4.9, "10 Feb 2025", 1800, "Completed"),
)

// ─────────────────────────────────────────────────────────────────────────────
// ProfileScreen
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var activeModal by remember { mutableStateOf(ProfileModal.None) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    fun openModal(m: ProfileModal) { activeModal = m }
    fun closeModal() { scope.launch { sheetState.hide() }.invokeOnCompletion { activeModal = ProfileModal.None } }

    val totalSpent = sampleBookings.sumOf { it.amount }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // ── Custom top bar with back arrow ────────────────────────────────────
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            IconButton(onClick = onBack) {
//                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//            }
//            Text(
//                "My Profile",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.weight(1f).padding(start = 4.dp),
//            )
//        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // ── User header card ──────────────────────────────────────────────
            item {
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                            Text("A", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
                        }
                        Text("Alex Henderson", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                        Text("+91 98765 43210", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.primaryContainer) {
                                Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Filled.Verified, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                                    Text("Verified", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.secondaryContainer) {
                                Text("Member since 2023", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                            }
                        }
                    }
                }
            }

            // ── Stats row ─────────────────────────────────────────────────────
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    StatCard("Bookings", "${sampleBookings.size}", modifier = Modifier.weight(1f))
                    StatCard("Total Spent", "₹$totalSpent", modifier = Modifier.weight(1f))
                    StatCard("Avg Rating", "4.9 ★", modifier = Modifier.weight(1f))
                }
            }

            // ── My Bookings (opens modal) ─────────────────────────────────────
            item {
                ProfileSection(title = "Activity") {
                    ProfileRow(icon = Icons.Filled.History, label = "My Bookings", subtitle = "${sampleBookings.size} total", onClick = { openModal(ProfileModal.Bookings) })
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileRow(icon = Icons.Filled.Payment, label = "Payment History", subtitle = "₹$totalSpent spent", onClick = { openModal(ProfileModal.Payments) })
                }
            }

            // ── Payment methods ───────────────────────────────────────────────
            item {
                ProfileSection(title = "Payment") {
                    ProfileRow(icon = Icons.Filled.CreditCard, label = "Payment Methods", subtitle = "Manage cards & UPI", onClick = { openModal(ProfileModal.PaymentMethods) })
                }
            }

            // ── Account settings ──────────────────────────────────────────────
            item {
                ProfileSection(title = "Account") {
                    ProfileRow(icon = Icons.Filled.Person, label = "Personal Information", subtitle = "Name, phone, email", onClick = { openModal(ProfileModal.PersonalInfo) })
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileRow(icon = Icons.Filled.Notifications, label = "Notifications", subtitle = "Alerts & reminders", onClick = { openModal(ProfileModal.Notifications) })
                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = MaterialTheme.colorScheme.outlineVariant)
                    ProfileRow(icon = Icons.Filled.Security, label = "Security & Privacy", subtitle = "Password, 2FA", onClick = { openModal(ProfileModal.Security) })
                }
            }

            // ── Log out ───────────────────────────────────────────────────────
            item {
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.clickable {}.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.errorContainer), contentAlignment = Alignment.Center) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                        }
                        Text("Log Out", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    // ── Modal bottom sheets ───────────────────────────────────────────────────

    if (activeModal != ProfileModal.None) {
        ModalBottomSheet(
            onDismissRequest = { activeModal = ProfileModal.None },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        ) {
            when (activeModal) {
                ProfileModal.Bookings       -> BookingsModal(bookings = sampleBookings, onDismiss = ::closeModal)
                ProfileModal.Payments       -> PaymentsModal(bookings = sampleBookings, totalSpent = totalSpent, onDismiss = ::closeModal)
                ProfileModal.PersonalInfo   -> PersonalInfoModal(onDismiss = ::closeModal)
                ProfileModal.PaymentMethods -> PaymentMethodsModal(onDismiss = ::closeModal)
                ProfileModal.Notifications  -> NotificationsModal(onDismiss = ::closeModal)
                ProfileModal.Security       -> SecurityModal(onDismiss = ::closeModal)
                ProfileModal.None           -> {}
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Shared profile UI components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp, modifier = Modifier.padding(bottom = 8.dp))
        Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
            Column(content = content)
        }
    }
}

@Composable
private fun ProfileRow(icon: ImageVector, label: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun ModalHeader(title: String, onDismiss: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        IconButton(onClick = onDismiss) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Spacer(Modifier.height(4.dp))
}

// ─────────────────────────────────────────────────────────────────────────────
// Bookings modal
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BookingsModal(bookings: List<ProfilePastBooking>, onDismiss: () -> Unit) {
    Column {
        ModalHeader("My Bookings", onDismiss)
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.height(520.dp),
        ) {
            items(bookings, key = { it.id }) { b ->
                BookingCard(b)
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun BookingCard(b: ProfilePastBooking) {
    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text(b.service, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(b.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.primaryContainer) {
                    Text(b.status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(26.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                        Text(b.worker.first().toString(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    Text(b.worker, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Icon(Icons.Filled.Star, contentDescription = null, modifier = Modifier.size(11.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("${b.rating}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("₹${b.amount}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(b.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Payments modal
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PaymentsModal(bookings: List<ProfilePastBooking>, totalSpent: Int, onDismiss: () -> Unit) {
    Column {
        ModalHeader("Payment History", onDismiss)
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(520.dp),
        ) {
            item {
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("TOTAL SPENT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), letterSpacing = 1.sp)
                        Text("₹$totalSpent", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary)
                        Text("Across ${bookings.size} services", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            items(bookings, key = { it.id }) { b ->
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.size(42.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(b.service, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text(b.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text("₹${b.amount}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Personal info modal
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PersonalInfoModal(onDismiss: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        ModalHeader("Personal Information", onDismiss)
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoField(label = "Full Name", value = "Alex Henderson", icon = Icons.Filled.Person)
            InfoField(label = "Phone Number", value = "+91 98765 43210", icon = Icons.Filled.Phone)
            InfoField(label = "Email", value = "alex@example.com", icon = Icons.Filled.Edit)
            InfoField(label = "Address", value = "Whitefield, Bengaluru", icon = Icons.Filled.Lock)
            Spacer(Modifier.height(8.dp))
            Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.clickable {}.padding(horizontal = 20.dp, vertical = 14.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Edit Information", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
private fun InfoField(label: String, value: String, icon: ImageVector) {
    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Payment methods modal
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PaymentMethodsModal(onDismiss: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        ModalHeader("Payment Methods", onDismiss)
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Saved methods
            listOf(
                "•••• •••• •••• 4242" to "Visa — Default",
                "UPI: alex@upi" to "Google Pay",
            ).forEach { (num, label) ->
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Filled.CreditCard, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(num, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.primaryContainer) {
                            Text("Default", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                        }
                    }
                }
            }
            // Add new
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(modifier = Modifier.clickable {}.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("Add New Method", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Notifications modal
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun NotificationsModal(onDismiss: () -> Unit) {
    var bookingAlerts by remember { mutableStateOf(true) }
    var promoAlerts by remember { mutableStateOf(false) }
    var smsAlerts by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        ModalHeader("Notifications", onDismiss)
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            NotifToggleRow("Booking updates", "Worker on the way, job complete, etc.", bookingAlerts) { bookingAlerts = it }
            NotifToggleRow("Promotions & offers", "Discounts and seasonal deals", promoAlerts) { promoAlerts = it }
            NotifToggleRow("SMS alerts", "Critical updates via SMS", smsAlerts) { smsAlerts = it }
        }
    }
}

@Composable
private fun NotifToggleRow(label: String, sub: String, checked: Boolean, onCheck: (Boolean) -> Unit) {
    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            androidx.compose.material3.Switch(checked = checked, onCheckedChange = onCheck)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Security modal
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SecurityModal(onDismiss: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        ModalHeader("Security & Privacy", onDismiss)
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(
                Triple(Icons.Filled.Lock, "Change Password", "Last changed 3 months ago"),
                Triple(Icons.Filled.Security, "Two-Factor Authentication", "Currently disabled"),
                Triple(Icons.Filled.Lock, "Data & Privacy", "Manage your data"),
            ).forEach { (icon, label, sub) ->
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerLow, modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.clickable {}.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text(sub, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}