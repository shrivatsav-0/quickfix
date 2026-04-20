package com.shrivatsav.quickfix

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.shrivatsav.quickfix.ui.theme.QuickFixTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// ── DataStore ─────────────────────────────────────────────────────────────────

private val Context.prefs by preferencesDataStore(name = "quickfix_prefs")
private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")

// ── Navigation state ──────────────────────────────────────────────────────────

private enum class AppScreen {
    Loading,
    Onboarding,
    Phone,
    Otp,
    Verifying,
    Home,
}

// ── Entry point ───────────────────────────────────────────────────────────────

@Composable
fun AppEntryPoint(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val isLoggedIn by remember {
        context.prefs.data.map { it[KEY_LOGGED_IN] ?: false }
    }.collectAsState(initial = null)

    var screen by remember { mutableStateOf(AppScreen.Loading) }
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == null) return@LaunchedEffect
        screen = if (isLoggedIn == true) AppScreen.Home else AppScreen.Onboarding
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            (fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                    scaleIn(spring(stiffness = Spring.StiffnessMediumLow), initialScale = 0.96f)) togetherWith
                    (fadeOut(tween(180)) + scaleOut(tween(180), targetScale = 0.96f))
        },
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        label = "app_screen",
    ) { target ->
        when (target) {
            AppScreen.Loading    -> null
            AppScreen.Onboarding -> OnboardingScreen(onGetStarted = { screen = AppScreen.Phone })
            AppScreen.Phone      -> PhoneScreen(onContinue = { p -> phone = p; screen = AppScreen.Otp })
            AppScreen.Otp        -> OtpScreen(
                phone = phone,
                onVerified = {
                    screen = AppScreen.Verifying
                    scope.launch {
                        delay(2400)
                        context.prefs.edit { it[KEY_LOGGED_IN] = true }
                        screen = AppScreen.Home
                    }
                },
                onBack = { screen = AppScreen.Phone },
            )
            AppScreen.Verifying  -> VerifyingLoader()
            AppScreen.Home       -> MainNavigation()
        }
    }
}

// ── Splash loader ─────────────────────────────────────────────────────────────


// ── Phone screen ──────────────────────────────────────────────────────────────

@Composable
fun PhoneScreen(
    onContinue: (phone: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var phone by remember { mutableStateOf("") }
    val isValid = phone.length == 10 && phone.all { it.isDigit() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 28.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(0.8f))

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.Build,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(36.dp),
            )
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = "What's your\nphone number?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "We'll send a one-time code to verify\nyour number.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(36.dp))

        // ── M3E phone field ───────────────────────────────────────────────────
        // Segmented row: country code chip + number field, styled to match M3E
        // OutlinedTextField shape/border tokens.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Country code chip — M3E SuggestionChip-style surface
            Surface(
                shape = MaterialTheme.shapes.large,   // matches OutlinedTextField shape
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                ),
                modifier = Modifier.height(56.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        Icons.Filled.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "+91",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            // Number input — standard M3 OutlinedTextField, no prefix clutter
            OutlinedTextField(
                value = phone,
                onValueChange = { raw ->
                    // Strip any country code prefix the system autofill might inject
                    val digits = raw.filter { it.isDigit() }
                    val stripped = when {
                        digits.startsWith("91") && digits.length > 10 -> digits.drop(2)
                        digits.startsWith("0")  && digits.length > 10 -> digits.drop(1)
                        else -> digits
                    }
                    if (stripped.length <= 10) phone = stripped
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = MaterialTheme.shapes.large,
                placeholder = { Text("10-digit number") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isValid) {
                            keyboardController?.hide()
                            onContinue(phone)
                        }
                    },
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                keyboardController?.hide()
                onContinue(phone)
            },
            enabled = isValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Text("Send OTP", style = MaterialTheme.typography.labelLarge, fontSize = 16.sp)
        }

        Spacer(Modifier.height(32.dp))
    }
}

// ── OTP screen ────────────────────────────────────────────────────────────────

private const val OTP_LENGTH = 6
private const val DEMO_OTP = "123456"
private const val AUTOFILL_DELAY_MS = 1200L

@Composable
fun OtpScreen(
    phone: String,
    onVerified: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var otp by remember { mutableStateOf("") }
    var resendTimer by remember { mutableIntStateOf(30) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    LaunchedEffect(Unit) {
        while (resendTimer > 0) { delay(1000); resendTimer-- }
    }

    // Demo autofill — only the 10-digit portion, no country code prefix
    LaunchedEffect(Unit) {
        delay(AUTOFILL_DELAY_MS)
        otp = DEMO_OTP   // already pure digits, no stripping needed
    }

    // Auto-verify once all digits filled (from autofill or manual)
    LaunchedEffect(otp) {
        if (otp.length == OTP_LENGTH) {
            delay(300) // brief pause so user sees filled boxes
            keyboardController?.hide()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 28.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(0.8f))

        Text(
            text = "Enter the code",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Sent to +91 $phone",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(40.dp))

        // Invisible field captures input; OtpBoxRow is the visual decoration
        BasicTextField(
            value = otp,
            onValueChange = { raw ->
                // Strip any country-code prefix autofill might inject here too
                val digits = raw.filter { it.isDigit() }
                if (digits.length <= OTP_LENGTH) otp = digits
            },
            modifier = Modifier.focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (otp.length == OTP_LENGTH) {
                        keyboardController?.hide()
                        onVerified()
                    }
                },
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
            decorationBox = { OtpBoxRow(otp = otp) },
        )

        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Didn't receive it? ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (resendTimer > 0) {
                Text(
                    "Resend in ${resendTimer}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                TextButton(onClick = { resendTimer = 30; otp = "" }) {
                    Text("Resend OTP", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                keyboardController?.hide()   // ← dismiss keyboard on Verify tap
                onVerified()
            },
            enabled = otp.length == OTP_LENGTH,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            Text("Verify", style = MaterialTheme.typography.labelLarge, fontSize = 16.sp)
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text("Change number")
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun OtpBoxRow(otp: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(OTP_LENGTH) { index ->
            val char = otp.getOrNull(index)
            val isFocused = index == otp.length

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        if (char != null) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.surfaceContainerLow,
                    )
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = when {
                            isFocused -> MaterialTheme.colorScheme.primary
                            char != null -> MaterialTheme.colorScheme.outline
                            else -> MaterialTheme.colorScheme.outlineVariant
                        },
                        shape = MaterialTheme.shapes.medium,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = char?.toString() ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

// ── Verifying loader — M3E ContainedLoadingIndicator ─────────────────────────

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun VerifyingLoader() {
    var showCheck by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1800)
        showCheck = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(96.dp),
            ) {
                // M3E expressive loading indicator
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

                // Spring-pop checkmark
                androidx.compose.animation.AnimatedVisibility(
                    visible = showCheck,
                    enter = scaleIn(
                        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
                    ) + fadeIn(),
                    exit = fadeOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Verified",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(44.dp),
                        )
                    }
                }
            }

            // Animated label
            AnimatedContent(
                targetState = showCheck,
                transitionSpec = {
                    (fadeIn(spring(stiffness = Spring.StiffnessMedium)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessMedium)) { it / 3 }) togetherWith
                            (fadeOut(tween(120)) + slideOutVertically { -it / 3 })
                },
                label = "verify_label",
            ) { verified ->
                Text(
                    text = if (verified) "You're all set!" else "Verifying your number…",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (verified) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ── Placeholder auth home ────────────────────────────────────────────────────

@Composable
fun AuthHomePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "🏠  Home",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showSystemUi = true)
@Composable
private fun PhonePreview() {
    QuickFixTheme { PhoneScreen(onContinue = {}) }
}

@Preview(showSystemUi = true)
@Composable
private fun OtpPreview() {
    QuickFixTheme { OtpScreen(phone = "9876543210", onVerified = {}, onBack = {}) }
}