package com.shrivatsav.quickfix

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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrivatsav.quickfix.ui.theme.QuickFixTheme

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var page by remember { mutableIntStateOf(0) }
    val totalPages = 3

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (page > 0) {
                IconButton(onClick = { page-- }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            } else {
                Spacer(Modifier.size(48.dp))
            }

            Text(
                text = "QuickFix",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            TextButton(onClick = onGetStarted) {
                Text("Skip", style = MaterialTheme.typography.labelLarge)
            }
        }

        // Page content — M3E spring slide transition
        AnimatedContent(
            targetState = page,
            transitionSpec = {
                val forward = targetState > initialState
                (slideInHorizontally(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                    initialOffsetX = { if (forward) it else -it },
                ) + fadeIn(tween(220))) togetherWith
                        (slideOutHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                            targetOffsetX = { if (forward) -it else it },
                        ) + fadeOut(tween(180)))
            },
            modifier = Modifier.weight(1f),
            label = "onboarding_page",
        ) { targetPage ->
            when (targetPage) {
                0 -> SplashPage()
                1 -> RadarPage()
                2 -> TrustPage()
                else -> SplashPage()
            }
        }

        // Page dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(totalPages) { index ->
                PageDot(selected = index == page)
                if (index < totalPages - 1) Spacer(Modifier.width(8.dp))
            }
        }

        // CTA button
        Button(
            onClick = { if (page < totalPages - 1) page++ else onGetStarted() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
                .height(56.dp),
            shape = MaterialTheme.shapes.extraLarge,
        ) {
            if (page < totalPages - 1) {
                Text("Next", style = MaterialTheme.typography.labelLarge, fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            } else {
                Text("Get started", style = MaterialTheme.typography.labelLarge, fontSize = 16.sp)
            }
        }
    }
}

// ── Page 1 – Splash ──────────────────────────────────────────────────────────

@Composable
private fun SplashPage() {
    val scaleAnim = remember { Animatable(0.6f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            1f,
            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))

        Surface(
            modifier = Modifier
                .size(160.dp)
                .scale(scaleAnim.value),
            shape = RoundedCornerShape(36.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            tonalElevation = 0.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Build,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(44.dp),
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "Everything\nFixed.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp,
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Quick, reliable help for your\nhome and office.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))
    }
}

// ── Page 2 – Radar ───────────────────────────────────────────────────────────

@Composable
private fun RadarPage() {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.93f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(0.5f))

        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center,
        ) {
            listOf(280.dp, 210.dp, 140.dp).forEachIndexed { index, size ->
                val alpha = 0.07f + index * 0.06f
                Box(
                    modifier = Modifier
                        .size(size * pulse)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha)),
                )
            }

            ServiceIcon(icon = Icons.Filled.Handyman, modifier = Modifier.offset(x = 90.dp, y = (-70).dp))
            ServiceIcon(icon = Icons.Filled.Bolt, modifier = Modifier.offset(x = 95.dp, y = 20.dp))
            ServiceIcon(icon = Icons.Filled.Build, modifier = Modifier.offset(x = (-90).dp, y = 50.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.GpsFixed,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp),
                )
            }
        }

        Spacer(Modifier.weight(0.5f))

        Text(
            text = "Find Help in\nSeconds.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp,
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Instantly connect with verified\nprofessionals in your area.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))
    }
}

// ── Page 3 – Trust ───────────────────────────────────────────────────────────

@Composable
private fun TrustPage() {
    val scaleAnim = remember { Animatable(0.7f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            1f,
            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(0.6f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.scale(scaleAnim.value),
        ) {
            TrustBadge(icon = Icons.Filled.Shield, label = "Verified", size = 72.dp)
            TrustBadge(icon = Icons.Filled.Star,   label = "Rated",    size = 88.dp)
            TrustBadge(icon = Icons.Filled.ThumbUp, label = "Trusted", size = 72.dp)
        }

        Spacer(Modifier.weight(0.6f))

        Text(
            text = "People You\nCan Rely On.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp,
        )

        Spacer(Modifier.height(14.dp))

        Text(
            text = "Every worker is background-checked,\nrated, and ready to help.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun TrustBadge(icon: ImageVector, label: String, size: Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            modifier = Modifier.size(size),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            shadowElevation = 4.dp,
            tonalElevation = 0.dp,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(size * 0.42f),
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

// ── Shared ────────────────────────────────────────────────────────────────────

@Composable
private fun ServiceIcon(icon: ImageVector, modifier: Modifier = Modifier, size: Dp = 52.dp) {
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        tonalElevation = 0.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun PageDot(selected: Boolean) {
    Box(
        modifier = Modifier
            .height(8.dp)
            .width(if (selected) 24.dp else 8.dp)
            .clip(CircleShape)
            .background(
                if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
            ),
    )
}

@Preview(showSystemUi = true)
@Composable
private fun OnboardingPreview() {
    QuickFixTheme { OnboardingScreen(onGetStarted = {}) }
}