# App Features Analysis (vs. Technical Specifications)

This document highlights features and implementation details present in the Android application that were **not** part of the original `technical.md` blueprint.

## 📱 Exclusive Android Features

### 1. Data Layer Enhancements
- **Signal Strength Indicator**: The `Device` model includes a `signal_strength` field (0-5 scale) and the UI displays this metric in the Dashboard cards.
- **Contact Priority**: The `SOSContact` model includes a numerical `priority` field, allowing users to rank emergency contacts for sequential notification protocols.

### 2. Native Hardware Integration
- **System Contact Picker**: The SOS screen integrates directly with the Android `ActivityResultContracts.PickContact()` contract, allowing users to import contacts directly from their phone's native address book.
- **Permission Management**: A dedicated `PermissionHandler` component manages Android-specific runtime permissions for fine/coarse location, contact reading, and (on Android 13+) push notifications.

### 3. UI/UX Refinements
- **Material 3 Theme**: The app utilizes the full `androidx.compose.material3` suite, including `LargeTopAppBar` and dynamic surface colors that automatically adapt to light/dark mode (system settings).
- **Navigation Graph**: Uses the official `androidx.navigation:navigation-compose` library with a strongly typed `Screen` sealed class for robust internal routing.
- **Coil Image Loading**: Uses the `Coil` library for efficient, lifecycle-aware remote avatar loading with automatic caching.

### 4. Technical Implementation Differences
- **OSM (osmdroid)**: Unlike the technical spec which suggests Leaflet (typically for web), the Android app uses `osmdroid` for native, offline-capable map rendering.
- **SafetyRepository Singleton**: Implements a centralized `SafetyRepository` object to synchronize real-time device updates, geofence breach logic, and path history across all feature ViewModels.
- **StateFlow Architecture**: Uses Kotlin `StateFlow` and `collectAsStateWithLifecycle` for high-performance, lifecycle-aware UI updates, exceeding the standard observable patterns in the spec.
