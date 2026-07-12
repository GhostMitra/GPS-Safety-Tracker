# Implementation Details - SafeGuard

This document provides a technical deep-dive into the implementation of the SafeGuard application.

## 🏛 Architecture

The project follows the **MVVM (Model-View-ViewModel)** architectural pattern, ensuring a clean separation of concerns and testability.

### Package Structure
- `com.gpssafetytracker`: Root package.
    - `data`: Data layer handling both remote and local data sources.
        - `model`: Data entities (Device, Geofence, SOSContact).
        - `remote`: Retrofit API services and network configuration.
    - `ui`: UI layer containing Composables and ViewModels.
        - `dashboard`: Command Center overview with system health monitoring and timeline.
        - `tracking`: Real-time map tracking with theme-integrated monochrome filters.
        - `geofencing`: Geofence management and interactive map-based editor.
        - `sos`: Emergency contact management and prominent SOS trigger.
        - `components`: Shared UI components (Permissions, Custom Buttons).
        - `navigation`: Navigation graphs and screen definitions.
        - `theme`: Material 3 theme configuration (Emerald & Slate palette).

## 🗺 Map & Tracking Implementation

The tracking feature is built using **osmdroid**, a powerful OpenStreetMap library for Android.

- **Monochrome Tile Engine**: A custom `ColorMatrixColorFilter` is applied to the map tiles, mapping their tones to the Material 3 `surface` and `primary` color ranges. This creates a professional, branded look that integrates perfectly with the app's theme.
- **`TrackingViewModel`**: Manages the state of tracked devices using `StateFlow`.
- **`OSMMapContainer`**: A custom wrapper around the osmdroid `MapView` that handles dynamic theme-based color filtering and lifecycle management.

## 🛡 Geofencing Implementation

Geofencing allows users to define virtual boundaries and receive alerts.

- **Interactive Setup**: Users can tap on the map to set a geofence center.
- **Radius Control**: A slider-based interface allows real-time adjustment of the geofence radius, with immediate visual feedback on the map.
- **State Management**: `GeofencingViewModel` tracks active geofences and their status.

## 🚨 Emergency SOS System

The SOS system is designed for quick access during emergencies.

- **Contact Picker**: Uses the system contact picker to allow users to easily add emergency contacts from their phonebook.
- **SOS Trigger**: A prominent SOS button that, when pressed, initiates the emergency protocol (currently simulated location sharing).
- **`SOSViewModel`**: Manages the list of emergency contacts, persisted locally.

## 💾 Data Layer & Persistence

- **Networking**: **Retrofit** is configured with **Moshi** for JSON parsing. `TrackerApiService` defines the endpoints for syncing device data with a remote backend.
- **Local Storage**: The project includes dependencies for **Room Persistence Library**, intended for local caching of device locations, geofence configurations, and emergency contacts.
- **State Handling**: Modern `StateFlow` and `collectAsStateWithLifecycle` are used to ensure the UI reactively updates to data changes while respecting the Android lifecycle.

## 🛠 Tech Stack Highlights

- **Jetpack Compose**: Entirely declarative UI built with Material 3 components.
- **Permissions**: **Accompanist Permissions** library simplifies the handling of location and contact permissions.
- **Dependency Management**: Uses Gradle Version Catalogs (`libs.versions.toml`) for centralized dependency management.
- **Coroutines & Flow**: Used extensively for asynchronous tasks and reactive data streams.

## 🚀 Future Roadmap

- **Full Room Integration**: Implement the `AppDatabase`, DAOs, and Repositories to fully leverage local persistence.
- **Background Tracking**: Implement a Foreground Service to maintain tracking even when the app is in the background.
- **Push Notifications**: Integrate Firebase Cloud Messaging (FCM) for real-time geofence breach alerts.
- **Hardened Security**: Implement encrypted storage for sensitive contact information.
