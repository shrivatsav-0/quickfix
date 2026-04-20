# QuickFix – Material 3 Expressive UI Design Spec

## Core M3E Principles
- **Spring Physics:** All transitions use natural overshoot.
- **Shared Element Transitions:** Cards morph into details; icons expand into headers.
- **Expressive Motion:** Immediate tactile feedback on touch.
- **Container Transforms:** Navigation feels like content unfolding.
- **Predictive Back:** Swipe-back gesture shows the screen behind.

## Customer App Screens
1. **Home Screen:** Responsive grid of service cards, M3 Search behavior, bottom nav with animated pill.
2. **Service Selection:** Container transform entrance, staggered list items, checkmark morph, spring bottom sheet.
3. **Booking Confirmation:** Bottom sheet with drag physics/rubber-banding, segmented buttons with spring indicators, Extended FAB with scroll-aware behavior.
4. **Searching/Matching:** Physics-based radar pulses, worker avatars with spring scale, interruptible circular countdown.
5. **Worker Found:** Modal bottom sheet transform from map pin, M3 Avatar scale spring, touch-point ripples.
6. **Live Tracking:** Smoothed map camera, peek-state draggable bottom sheet, idle spring loop on pins.

## Worker App Screens
1. **Dashboard:** M3 Switch with rubber-band physics, animated earnings counter, pulsing job heatmap.
2. **Incoming Job Alert:** Full-screen modal with spring overshoot, physics-aware progress ring, burst/ripple on Accept.
3. **Active Job:** Checklist with path-draw animations, scale-burst success state.

## Global Motion Rules
- **Enter:** Container transform / shared element.
- **Exit:** Fade + slide back.
- **Press:** Spring scale down.
- **Sheet Open:** Spring rise with overshoot.
- **Tokens:** Extra-large rounded corners, pill shapes, 150-500ms durations.