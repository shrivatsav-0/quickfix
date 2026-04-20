package com.shrivatsav.quickfix

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shrivatsav.quickfix.data.ServiceCategory
import com.shrivatsav.quickfix.data.SubService

// ── Screen routing ────────────────────────────────────────────────────────────

enum class BookingScreen {
    Permissions,   // first-launch permission gate
    Home,          // main home (no bottom nav)
    Help,          // FAQ / help page
    Profile,       // profile (full page, no bottom nav)
    SubCategory,
    TellUsMore,
    BookingConfirm,
    Searching,
    WorkerFound,
    LiveTracking,
    JobInProgress,
    JobComplete,
    Payment,
    PaymentConfirm,
}

val BookingScreen.showBackArrow: Boolean
    get() = when (this) {
        BookingScreen.Permissions,
        BookingScreen.Home -> false
        else -> true
    }

// ── Booking state ─────────────────────────────────────────────────────────────

data class BookingState(
    val selectedCategory: ServiceCategory? = null,
    val selectedSubService: SubService? = null,
    val description: String = "",
    val photoUris: List<String> = emptyList(),
    val address: String = "Whitefield, Bengaluru",
    val scheduleNow: Boolean = true,
    val workerName: String = "Rajan Kumar",
    val workerRating: Double = 4.9,
    val workerReviews: Int = 118,
    val etaMins: Int = 8,
    val totalAmount: Int = 0,
    val paymentMethod: String = "UPI",
)

// ── Top bar ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuickFixTopBar(
    screen: BookingScreen,
    onBack: () -> Unit,
    onProfile: () -> Unit,
    onHelp: () -> Unit = {},
    onNotifications: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "QuickFix logo",
                    modifier = Modifier.size(50.dp),

                )
                Text(
                    text = "QuickFix",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        navigationIcon = {
            if (screen.showBackArrow) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (screen == BookingScreen.Home) {
                IconButton(onClick = onNotifications) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                }
                IconButton(onClick = onHelp) {
                    Icon(Icons.AutoMirrored.Filled.Help, contentDescription = "Help")
                }
            }
            IconButton(onClick = onProfile) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier,
    )
}