# SafeGuard Technical Documentation

## 1. System Overview
SafeGuard is a professional-grade Android application designed for real-time location tracking, proactive safety monitoring, and emergency response management. The system integrates mobile geolocation services with external hardware tracking (e.g., ESP32-based devices) to provide a comprehensive safety ecosystem.

## 2. Technical Stack
- **Language**: Kotlin 1.9+
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel) with Repository Pattern
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Navigation**: Jetpack Compose Navigation
- **Networking**: Retrofit 2 & OkHttp 4 (with Logging Interceptors)
- **Serialization**: Moshi (JSON Reflection & Codegen)
- **Local Persistence**: DataStore (Preferences) & Room (Configured for future schema)
- **Map Engine**: OpenStreetMap (osmdroid)
- **Dependency Management**: Gradle (Version Catalog)
- **Minimum SDK**: 31 (Android 12)
- **Target SDK**: 36 (Android 15)

## 3. Architecture & Design Patterns
The application is built using a modern, reactive architecture based on Clean Architecture principles and Google's recommended app architecture.

### 3.1 Architectural Layers
1.  **Presentation Layer (UI)**: Built entirely with **Jetpack Compose**. It is passive and purely reactive, observing state from ViewModels. It consists of Composable functions, State holders (ViewModels), and Navigation logic.
2.  **Domain/Data Layer**: Combined in this implementation for simplicity. It contains the business logic (Geofencing Engine), Data Models (Moshi-annotated POJOs), and the **SafetyRepository**.
3.  **Data Source Layer**: Consists of the **Networking Module** (Retrofit) and the potential **Persistence Module** (Room/DataStore).

### 3.2 MVVM Pattern
-   **Model**: Represents the state of the system (e.g., `Device`, `Geofence`). These are immutable data classes.
-   **View**: Jetpack Compose screens (e.g., `MapScreen`, `DashboardScreen`). They use `collectAsStateWithLifecycle()` to stay updated with the Model via the ViewModel.
-   **ViewModel**: Acts as the bridge. It transforms data from the Repository into UI state and handles user intents (e.g., `triggerSOS()`, `addGeofence()`).

### 3.3 Repository Pattern (`SafetyRepository`)
The `SafetyRepository` is implemented as a **Kotlin Singleton (`object`)**. It serves as the "Single Source of Truth" (SSOT) for the application.
-   **State Management**: It maintains `MutableStateFlow` streams for all critical data.
-   **Background Processing**: It hosts a long-running coroutine for API polling, ensuring data stays fresh even as the user navigates between screens.
-   **Logic Orchestration**: When new data arrives via the API, the Repository automatically triggers the Geofencing Engine and updates all dependent StateFlows.

### 3.4 Unidirectional Data Flow (UDF)
The app strictly enforces UDF:
-   **Events** flow up: UI -> ViewModel -> Repository.
-   **State** flows down: Repository -> ViewModel -> UI.
This pattern ensures that the UI is always a direct reflection of the underlying data and makes the app significantly easier to debug and test.

### 3.5 Dependency Management
Currently, the app uses **Manual Dependency Injection** via Singleton objects (e.g., `NetworkModule`, `SafetyRepository`). This approach is chosen for its simplicity and low overhead, given the current scale of the project, while remaining compatible with future migration to Hilt or Koin.

## 4. Detailed Component Breakdown

### 4.1 Data Models & Schema
The system uses Moshi for JSON mapping to ensure high performance and safety.

#### Device (`com.gpssafetytracker.data.model.Device`)
Represents a tracked hardware unit.
- `id`: Unique MAC address or Serial.
- `type`: `WATCH`, `COLLAR`, `TRACKER`, `PHONE`.
- `status`: `ONLINE`, `OFFLINE`, `LOW_BATTERY`.
- `latitude` / `longitude`: Double-precision coordinates.
- `batteryLevel`: 0-100 percentage.
- `signalStrength`: 0-5 mapping from RSSI.

#### LocationPingDto (`com.gpssafetytracker.data.model.LocationPingDto`)
The raw network response format.
- `wifi_rssi`: Received Signal Strength Indicator for network health.
- `gps_locked`: Boolean indicating if coordinates are fresh.
- `satellites`: Count of GPS satellites visible.

#### Geofence (`com.gpssafetytracker.data.model.Geofence`)
- `radius`: Defined in meters.
- `status`: `SAFE` or `BREACHED`.
- `isActive`: Toggle for monitoring.

### 4.2 UI Layer (`com.gpssafetytracker.ui`)
- **MainScreen**: Manages the application scaffold.
    - **Emergency Banner System**: `AnimatedVisibility` banners for SOS (Red) and Geofence Breach (Orange).
    - **Bottom Navigation**: Integration with `NavHost` for seamless transitions.
- **OSMMapContainer**: A specialized `AndroidView` wrapper for `osmdroid`.
    - **Monochrome Engine**: Uses `ColorMatrix` to apply real-time architectural filters.
    - **Dark Mode**: Inversion and desaturation mapped to `MaterialTheme.colorScheme.surface`.
    - **Light Mode**: Grayscale with reduced contrast for "Architectural" aesthetic.

### 4.3 Networking Layer (`com.gpssafetytracker.data.remote`)
- **Base URL**: `http://10.0.2.2:8000/` (Optimized for Android Emulator localhost access).
- **Endpoints**:
    - `GET api/devices/{deviceId}/latest`: Fetches the most recent ping.
    - `GET api/devices/{deviceId}/history`: Returns a list of the last 50 coordinates for path drawing.

## 5. Key Algorithms & Logic

### 5.1 Real-time Polling Logic
Polling is handled in `SafetyRepository` via an infinite loop in a `Dispatchers.IO` coroutine:
```kotlin
while (true) {
    try {
        val ping = NetworkModule.apiService.getLatest(deviceId)
        // Update device state and trigger geofence checks
    } catch (e: Exception) {
        // Log error and retry after delay
    }
    delay(8000) // 8s Polling interval
}
```

### 5.2 Geofencing Engine (Haversine Implementation)
The system calculates distances between the device and every active geofence center.
- **Formula**:
  $$a = \sin^2(\frac{\Delta\phi}{2}) + \cos\phi_1 \cdot \cos\phi_2 \cdot \sin^2(\frac{\Delta\lambda}{2})$$
  $$c = 2 \cdot \operatorname{atan2}(\sqrt{a}, \sqrt{1-a})$$
  $$d = R \cdot c$$ (where $R$ is Earth's radius, 6371km).
- **Breach Trigger**: If $d > \text{radius}$, the state changes to `BREACHED`, which automatically updates the global `hasBreach` flow in `MainScreen`.

## 6. Security & Permissions
- **Accompanist Permissions**: Declarative permission handling.
- **Location Access**: Requires `ACCESS_FINE_LOCATION` for mapping and relative distance tracking.
- **Contacts**: `READ_CONTACTS` for SOS contact selection.
- **Notifications**: `POST_NOTIFICATIONS` for critical system alerts in the background.

## 7. Configuration & Environment
- **local.properties**: Stores `MAPS_API_KEY`.
- **User-Agent**: `osmdroid` requires a unique User-Agent (configured to the application ID) to comply with tile server policies.

## 8. Build & Dependencies
Key libraries used:
- **Compose**: UI & Foundation.
- **Lifecycle**: `lifecycle-runtime-compose` for StateFlow consumption.
- **Navigation**: `navigation-compose`.
- **Networking**: `retrofit`, `okhttp`, `moshi`.
- **Utilities**: `coil` (Image loading), `osmdroid` (Mapping), `accompanist-permissions`.

## 9. Future Roadmap
- **WorkManager**: Background sync when the UI is not visible.
- **Tile Caching**: SQL-based caching for offline map support.
- **Advanced Path Smoothing**: Kalman filter implementation for noisy GPS data.
