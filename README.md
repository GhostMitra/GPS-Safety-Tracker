# GPS Safety Tracker

A modern Android application designed for real-time tracking, safety monitoring, and emergency response. Built with Jetpack Compose, Material 3, and OpenStreetMap.

## 🚀 Features

- **Real-time Tracking**: Monitor device locations in real-time on an interactive map using OpenStreetMap (osmdroid).
- **Interactive Geofencing**: Easily create and manage safety boundaries. 
    - Tap on the map to set geofence centers.
    - Interactive sliders to adjust radius.
    - Toggle active/inactive states for each zone.
- **Emergency SOS Management**:
    - Manage emergency contacts.
    - Integrated system contact picker for quick setup.
    - One-tap SOS triggers for immediate location sharing.
- **Device Dashboard**: Overview of connected devices, including battery levels and signal strength.
- **Modern Material 3 UI**: Clean, accessible, and responsive design following the latest Android standards.

## 🛠️ Tech Stack

- **UI**: Jetpack Compose (Material 3)
- **Navigation**: Compose Navigation
- **Map Engine**: OpenStreetMap (osmdroid)
- **Networking**: Retrofit & Moshi
- **Local Storage**: Room Persistence Library
- **Permissions**: Accompanist Permissions
- **Image Loading**: Coil
- **Concurrency**: Kotlin Coroutines & Flow

## 📦 Architecture

The project follows modern Android development best practices:
- **MVVM Architecture**: Separation of concerns between UI, Business Logic, and Data.
- **State Management**: Using `StateFlow` and `collectAsStateWithLifecycle` for lifecycle-aware UI updates.
- **Dependency Injection**: Modularized data and remote layers.

## ⚙️ Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/debarghaya/GPS-Safety-Tracker.git
   ```

2. **Open in Android Studio**:
   Import the project as a Gradle project (requires Android Studio Ladybug or newer).

3. **Permissions**:
   The app requires the following permissions to function:
   - `ACCESS_FINE_LOCATION` & `ACCESS_COARSE_LOCATION` (Tracking & Geofencing)
   - `READ_CONTACTS` (SOS Contact Picker)
   - `POST_NOTIFICATIONS` (Alerts)
   - `INTERNET` (Map tiles and remote sync)

4. **Build and Run**:
   Sync Gradle and run the `:app` module on an emulator or physical device.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
