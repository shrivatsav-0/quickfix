
# AGENTS.md – QuickFix Codebase Guide

## Project Overview

**QuickFix** is an Android service marketplace application (customer-worker platform) built with **Jetpack Compose** and **Material Design 3 Expressive (M3E)** animations. The app follows a single-module structure featuring authentication flows, service booking workflow, job tracking, and payment handling with highly animated UI transitions.

### Tech Stack
- **Language**: Kotlin 2.2.10
- **Framework**: Jetpack Compose (Material 3 alpha 1.5.0-a17)
- **Min SDK**: 31 | **Target SDK**: 36 | **Compile SDK**: 36
- **Architecture**: Local DataStore (preferences-based persistence)
- **Build System**: Gradle 9.1.1 with version catalog (`gradle/libs.versions.toml`)

---

## Architecture & Key Components

### Navigation Hierarchy

The app's entry point (`MainActivity.kt`) calls **`AppEntryPoint()`** which manages a unified state machine:

```
AppEntryPoint() in AuthFlow.kt:
  Loading → Onboarding → Phone → OTP → Verifying → MainNavigation()
```

**Flow Details**:
- **`AppEntryPoint()`** (`AuthFlow.kt`): Checks DataStore login state; routes to auth screens or main app
- **Onboarding Screen**: User taps "Get Started" → calls `onGetStarted` callback → transitions to `MainNavigation()`
- **`MainNavigation()`** (`MainNavigation.kt`): Main app composable after auth succeeds
  - **Bottom Nav Tabs**: TabHome, TabBookings, TabPayments, TabProfile
  - **Booking Flow**: Category → SubCategory → TellUsMore → BookingConfirm → Searching → WorkerFound → LiveTracking → JobInProgress → JobComplete → Payment → PaymentConfirm

**Routing enums**: `AppScreen` (auth states, in AuthFlow.kt) and `BookingScreen` (main app states, in SharedComponents.kt)

### Authentication Screens (`AuthFlow.kt`)
- **`AppEntryPoint()`**: Main entry composable checking DataStore login state
- **`PhoneScreen`**: 10-digit number input with country code chip (+91). Strips autofilled prefixes (`91` or leading `0`).
- **`OtpScreen`**: 6-digit OTP with visual box display. Auto-fill triggers after 1.2s with demo OTP `"123456"`. Auto-verification on complete input.
- **`VerifyingLoader`**: Animated loader → checkmark transition using `ContainedLoadingIndicator` (M3E experimental API).

### Onboarding (`OnboardingScreen.kt`)
Three-page paged flow with spring-based horizontal slide transitions:
1. **Splash**: Logo scale-in animation
2. **Radar**: Physics-based pulse with scattered service icons
3. **Trust**: Scaled badge row (final page with "Get Started" button)

**Navigation**: Tapping "Get Started" on the final page calls `onGetStarted` callback, which transitions to `MainNavigation()`.

### Main App Screens

**Booking Flow** (`BookingFlow.kt`):
- **`HomeScreen`**: Service category grid + carousel of featured services
- **`CategoryScreen` / `SubCategoryScreen`**: Service selection with descriptions and pricing
- **`TellUsMoreScreen`**: Text description + photo picker (camera/gallery via ActivityResult)
- **`BookingConfirmScreen`**: Address input, schedule toggle, booking summary
- **`SearchingScreen`**: Animated search with cancel option
- **`WorkerFoundScreen`**: Worker card with profile, rating, accept/decline actions

**Job Tracking** (`JobFlow.kt`):
- **`LiveTrackingScreen`**: Map placeholder, worker location, ETA
- **`JobInProgressScreen`**: Timer, worker status, "Work Complete" action
- **`JobCompleteScreen`**: Summary with rating and payment button

**Payment** (`PaymentFlow.kt`):
- **`PaymentScreen`**: Radio buttons for payment methods (Card, Bank, UPI, Cash)
- **`PaymentConfirmScreen`**: Receipt and "Back to Home" button

**Tabs** (`TabScreens.kt`):
- **`BookingsScreen`**: Past bookings list (mock data)
- **`PaymentsTabScreen`**: Payment history
- **`ProfileScreen`**: User profile section (placeholder)

### Shared Components (`SharedComponents.kt`)
- **`QuickFixTopBar`**: Conditional back button (via `showBackArrow` helper)
- **`QuickFixBottomNav`**: 4-tab navigation with animated icons
- **`BookingState`**: Data class hoisting booking data through nav stack
- **`BookingScreen` enum**: 15 states defining main app flow
- **Helper extensions**: `showBottomNav`, `showBackArrow` for conditional UI rendering

### Data Models (`data/ServiceData.kt`)
```kotlin
data class ServiceCategory(
    val id: String, name: String, description: String, icon: ImageVector,
    val subServices: List<SubService>
)

data class SubService(
    val id: String, name: String, description: String, icon: ImageVector,
    val estimatedPriceMin: Int, estimatedPriceMax: Int, estimatedDuration: String
)
```
Service categories include: Plumbing, Electrical, Cleaning, AC, Carpentry, Pest Control, Painting, Garden Work, Appliances, and more.

### Data Persistence
**DataStore location**: `Context.prefs` (file: `quickfix_prefs`)  
**Key pattern**: `booleanPreferencesKey("logged_in")`  
All persistence flows through coroutines (`scope.launch`) with hardcoded delays (e.g., 2400ms after OTP verify).

---

## M3E Motion & Animation Patterns

### Spring Configuration (Project Standard)
```kotlin
Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow  // Default entrance
Spring.DampingRatioNoBouncy, Spring.StiffnessMedium         // Exit (snappier)
```

### Common Transitions
- **Scale + Fade In**: `scaleIn(spring(...), initialScale = 0.96f) + fadeIn(spring(...))`
- **Slide + Fade**: `slideInHorizontally(spring(...)) + fadeIn(tween(220))`
- **AnimatedContent Default**: Uses `togetherWith` to combine enter and exit specs

### Key M3E Composables Used
- `ContainedLoadingIndicator` (experimental, in VerifyingLoader)
- `Material3.Button`, `TextButton`, `OutlinedTextField` with M3 shapes (`shapes.extraLarge`, `shapes.large`)
- No custom animations—all use Compose animation core + spring physics

### Design System References
See `app/design/quickfix_design_specification.md`:
- Extra-large rounded corners (tokens from M3)
- Pill-shaped buttons
- 150–500ms durations (mostly 220–1800ms)
- Spring overshoot on modal opens, fade + slide on exit

---

## Build & Development Workflows

### Gradle Build
```bash
./gradlew assemble              # Build APK
./gradlew assembleRelease       # Release build (minify disabled)
./gradlew test                  # Run unit tests
./gradlew connectedAndroidTest  # Run instrumented tests
```

### Preview & Debugging
- **Compose Preview**: `@Preview(showSystemUi = true)` on most screens (PhoneScreen, OtpScreen, OnboardingScreen)
- **Theme Preview**: Wrap in `QuickFixTheme { ... }` for consistent theme colors
- **No Custom Build Variants**: Single default config; no flavors defined

### Dependencies
- Core Compose: BOM-managed (2025.02.00)
- Material 3: `1.5.0-alpha17` (note: alpha version, use experimental API opt-ins)
- DataStore: `1.1.1`
- JUnit + Espresso for testing

---

## Project-Specific Patterns & Conventions

### State Management
- **Local State Only**: No ViewModel, no Flow-based state. Use `mutableStateOf` + `rememberCoroutineScope` for navigation.
- **Coroutine Delays**: Hardcoded delays for UX timing (1.2s autofill, 2.4s verification, 1.8s loader).

### Input Validation
- **Phone**: `length == 10 && all { it.isDigit() }`
- **OTP**: `length == 6`
- **Autofill Cleanup**: Strip country codes (`91`, leading `0`) from system autofill in `onValueChange`.

### Keyboard Handling
- `LocalSoftwareKeyboardController.current?.hide()` on button tap
- `FocusRequester` for auto-focus on screen entry
- `KeyboardOptions(KeyboardType.Phone / NumberPassword)` + `KeyboardActions(onDone = ...)`

### Resource Loading
- **Icons**: Material Icons (Filled, AutoMirrored variants)
- **Strings**: Hard-coded in Composables (no strings.xml yet)
- **Assets**: Default M3 color tokens; no custom drawables observed

### Naming Conventions
- **Composables**: PascalCase (SplashPage, OtpBoxRow)
- **Enums for Routes**: Single-word enum states (Onboarding, Phone, Otp, Home)
- **Private Functions**: Marked with `private fun` for encapsulation

---

## Critical Integration Points

### DataStore & Persistence
```kotlin
val context = LocalContext.current
context.prefs.data.map { it[KEY_LOGGED_IN] ?: false }.collectAsState(initial = null)
context.prefs.edit { it[KEY_LOGGED_IN] = true }
```
Always use `rememberCoroutineScope()` to launch persistence edits.

### Navigation & Screen Transitions
Transitions between auth screens happen via callback lambdas (`onContinue`, `onVerified`, `onBack`). State is managed by the parent `AppEntryPoint` composable.

### Theme Integration
All screens must be wrapped with `QuickFixTheme` (from `ui/theme/Theme.kt`). Color tokens reference `MaterialTheme.colorScheme.*` (no hardcoded colors).

---

## Testing & Release

### Test Structure
- `app/src/androidTest/`: Instrumented tests (Espresso + Compose test JUnit4)
- `app/src/test/`: Unit tests (JUnit)
- **No test files found yet** — add tests following AAA pattern (Arrange, Act, Assert)

### Release Build
- **Minify**: Disabled (flag in build.gradle.kts)
- **ProGuard**: Default rules + `app/proguard-rules.pro`
- **APK Output**: `app/release/app-release.apk`

### Signing
`local.properties` should contain keystore details for release signing (not in repo).

---

## Common Tasks & Examples

### Add a New Booking Flow Screen
1. Create `@Composable` function in appropriate file (BookingFlow.kt, JobFlow.kt, or PaymentFlow.kt)
2. Add state to `BookingScreen` enum in `SharedComponents.kt`
3. Add case to `when (target)` block in `MainNavigation.kt` AnimatedContent
4. Define transition spec (copy existing spring pattern from line ~67–71)
5. Pass `BookingState` down and call state-updating lambdas (e.g., `onContinue`)

**Example**:
```kotlin
// SharedComponents.kt
enum class BookingScreen {
    // ...existing states...
    NewScreen,
}

// MainNavigation.kt
BookingScreen.NewScreen -> NewScreenComposable(
    booking = booking,
    onContinue = { updatedBooking ->
        booking = updatedBooking
        goTo(BookingScreen.NextScreen)
    }
)

// BookingFlow.kt / JobFlow.kt
@Composable
private fun NewScreenComposable(
    booking: BookingState,
    onContinue: (BookingState) -> Unit
) {
    // Use spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow) for entrance
}
```

### Add a Photo Picker to a Screen
Use `rememberLauncherForActivityResult` with `ActivityResultContracts.PickVisualMedia()` (see `TellUsMoreScreen` in BookingFlow.kt for implementation). Store URIs as strings in `BookingState.photoUris: List<String>`.

### Modify Auth Screen Timing/Delays
Search `delay()` calls in `AuthFlow.kt` (1200L autofill, 2400ms verify, 1800ms loader). Adjust in `LaunchedEffect` blocks. Also check hardcoded `scheduleExecution` delays if present.

### Update Theme Colors
Edit `ui/theme/Color.kt` (light/dark schemes) and reference via `MaterialTheme.colorScheme.*` in Composables. Remember to wrap previews in `QuickFixTheme { ... }`.

---

## AI Agent Priorities

1. **Motion First**: All UX updates should respect M3E spring physics; avoid tween-only animations.
2. **DataStore Persistence**: Any state retention must go through the prefs DataStore pattern; no SharedPreferences.
3. **Input Sanitization**: Phone/OTP inputs must strip autofill prefixes; validate length strictly.
4. **Callback-Based Navigation**: Do not introduce a routing library; use parent-managed state with lambdas.
5. **Experimental API Awareness**: Material 3 alpha modules require `@OptIn(ExperimentalMaterial3ExpressiveApi::class)`.

---

## Quick Reference

| Aspect | File/Location |
|--------|---------------|
| Entry Point | `AuthFlow.kt` (AppEntryPoint) |
| Main App Navigation | `MainNavigation.kt` |
| Onboarding Flow | `OnboardingScreen.kt` |
| Booking Screens | `BookingFlow.kt` (HomeScreen, CategoryScreen, SubCategoryScreen, TellUsMoreScreen, BookingConfirmScreen, SearchingScreen, WorkerFoundScreen) |
| Job Tracking | `JobFlow.kt` (LiveTrackingScreen, JobInProgressScreen, JobCompleteScreen) |
| Payment Screens | `PaymentFlow.kt` (PaymentScreen, PaymentConfirmScreen) |
| Tab Screens | `TabScreens.kt` (BookingsScreen, PaymentsTabScreen, ProfileScreen) |
| Shared Components | `SharedComponents.kt` (QuickFixTopBar, QuickFixBottomNav, BookingScreen enum, BookingState) |
| Theme & Colors | `ui/theme/{Theme,Color,Type}.kt` |
| Data Models | `data/ServiceData.kt` (ServiceCategory, SubService, serviceCategories) |
| Build Config | `app/build.gradle.kts` |
| Dependencies | `gradle/libs.versions.toml` |
| Design Spec | `app/design/quickfix_design_specification.md` |
| App Permissions | `AndroidManifest.xml` (CAMERA, POST_NOTIFICATIONS) |

---

## Known Limitations & TODOs

- ✗ No backend API integration yet (no networking dependencies)
- ✗ No multi-module structure (single `app/` module)
- ✗ No ViewModel or advanced state management
- ✗ Hardcoded OTP (`"123456"`) and phone prefixes (demo only)
- ✗ No i18n/localization (all strings hard-coded in Kotlin)
- ✗ Camera & notification permissions declared but not used yet

