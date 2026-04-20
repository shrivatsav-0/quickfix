package com.shrivatsav.quickfix

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.shrivatsav.quickfix.data.serviceCategories
import com.shrivatsav.quickfix.data.ServiceCategory
import com.shrivatsav.quickfix.data.SubService

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var screen by remember {
        mutableStateOf(
            if (areRequiredPermissionsGranted(context)) BookingScreen.Home else BookingScreen.Permissions,
        )
    }
    var booking by remember { mutableStateOf(BookingState()) }

    fun goTo(s: BookingScreen) { screen = s }
    fun goBack() {
        previousScreenFor(screen)?.let { previous ->
            screen = previous
        }
    }

    // Intercept system back for in-flow screens; allow default app-exit on root screens.
    BackHandler(enabled = previousScreenFor(screen) != null) {
        goBack()
    }

    Scaffold(
        topBar = {
            if (screen != BookingScreen.Permissions) {
                QuickFixTopBar(
                    screen = screen,
                    onBack = { goBack() },
                    onProfile  = { goTo(BookingScreen.Profile) },
                    onHelp     = { goTo(BookingScreen.Help) },
                )
            }
        },
        // NO bottomBar — removed entirely
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,
    ) { innerPadding ->
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                (fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                        scaleIn(spring(stiffness = Spring.StiffnessMediumLow), initialScale = 0.97f)) togetherWith
                        (fadeOut(spring(stiffness = Spring.StiffnessMedium)) +
                                scaleOut(spring(stiffness = Spring.StiffnessMedium), targetScale = 0.97f))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "main_screen",
        ) { target ->
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                when (target) {

                    BookingScreen.Permissions -> PermissionsScreen(
                        onGranted = { goTo(BookingScreen.Home) },
                    )

                    BookingScreen.Home -> HomeScreen(
                        onSubServiceSelected = { cat: ServiceCategory, sub: SubService ->
                            booking = booking.copy(selectedCategory = cat, selectedSubService = sub)
                            goTo(BookingScreen.TellUsMore)
                        },
                    )

                    BookingScreen.Help -> HelpScreen()

                    BookingScreen.Profile -> ProfileScreen(
                        onBack = { goTo(BookingScreen.Home) },
                    )

                    BookingScreen.SubCategory -> SubCategoryScreen(
                        category = booking.selectedCategory ?: serviceCategories.first(),
                        onSubServiceSelected = { sub ->
                            booking = booking.copy(selectedSubService = sub)
                            goTo(BookingScreen.TellUsMore)
                        },
                    )

                    BookingScreen.TellUsMore -> TellUsMoreScreen(
                        onContinue = { desc, uris ->
                            booking = booking.copy(description = desc, photoUris = uris)
                            goTo(BookingScreen.BookingConfirm)
                        },
                    )

                    BookingScreen.BookingConfirm -> BookingConfirmScreen(
                        booking = booking,
                        onAddressEdit = { booking = booking.copy(address = it) },
                        onScheduleToggle = { booking = booking.copy(scheduleNow = it) },
                        onFindWorker = { goTo(BookingScreen.Searching) },
                    )

                    BookingScreen.Searching -> SearchingScreen(
                        onWorkerFound = { goTo(BookingScreen.WorkerFound) },
                        onCancel = { goTo(BookingScreen.Home) },
                    )

                    BookingScreen.WorkerFound -> WorkerFoundScreen(
                        booking = booking,
                        onTrack = { goTo(BookingScreen.LiveTracking) },
                        onCancel = { goTo(BookingScreen.Home) },
                    )

                    BookingScreen.LiveTracking -> LiveTrackingScreen(
                        booking = booking,
                        onWorkerArrived = { goTo(BookingScreen.JobInProgress) },
                    )

                    BookingScreen.JobInProgress -> JobInProgressScreen(
                        booking = booking,
                        onJobComplete = { total ->
                            booking = booking.copy(totalAmount = total)
                            goTo(BookingScreen.JobComplete)
                        },
                    )

                    BookingScreen.JobComplete -> JobCompleteScreen(
                        booking = booking,
                        onProceedToPayment = { goTo(BookingScreen.Payment) },
                    )

                    BookingScreen.Payment -> PaymentScreen(
                        booking = booking,
                        onPaymentDone = { method ->
                            booking = booking.copy(paymentMethod = method)
                            goTo(BookingScreen.PaymentConfirm)
                        },
                    )

                    BookingScreen.PaymentConfirm -> PaymentConfirmScreen(
                        booking = booking,
                        onReturnHome = {
                            booking = BookingState()
                            goTo(BookingScreen.Home)
                        },
                    )
                }
            }
        }
    }
}

private fun previousScreenFor(screen: BookingScreen): BookingScreen? {
    return when (screen) {
        BookingScreen.Permissions -> null
        BookingScreen.Home -> null
        BookingScreen.Help -> BookingScreen.Home
        BookingScreen.Profile -> BookingScreen.Home
        BookingScreen.SubCategory -> BookingScreen.Home
        BookingScreen.TellUsMore -> BookingScreen.SubCategory
        BookingScreen.BookingConfirm -> BookingScreen.TellUsMore
        BookingScreen.Searching -> BookingScreen.BookingConfirm
        BookingScreen.WorkerFound -> BookingScreen.Searching
        BookingScreen.LiveTracking -> BookingScreen.WorkerFound
        BookingScreen.Payment -> BookingScreen.JobComplete
        else -> BookingScreen.Home
    }
}
