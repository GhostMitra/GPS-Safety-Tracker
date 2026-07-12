# Mock Data - SafeGuard

This document lists the mock data currently used for development and demonstration purposes within the application.

## 📱 Tracked Devices
Managed by `TrackingViewModel`.

| ID | Name | Initial Latitude | Initial Longitude | Battery | Signal | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `8C:94:DF:68:ED:40` | ESP32 Safety Tracker | 37.7749 | -122.4194 | 100% | 5/5 | ONLINE |

*Note: The location of the ESP32 Safety Tracker is simulated to update randomly every 5 seconds.*

## 🛡 Geofences
Managed by `GeofencingViewModel`.

| ID | Name | Latitude | Longitude | Radius (m) | Active |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `1` | Home | 37.7749 | -122.4194 | 500.0 | Yes |
| `2` | School | 37.7849 | -122.4294 | 300.0 | Yes |

## 🚨 Emergency SOS Contacts
Managed by `SOSViewModel`.

| ID | Name | Phone Number | Priority |
| :--- | :--- | :--- | :--- |
| `1` | Mom | 555-0101 | 1 |
| `2` | Dad | 555-0102 | 2 |

## 🛠 Simulation Details
- **Location Updates**: The `TrackingViewModel` uses a coroutine to jitter the coordinates of the device to simulate movement on the map.
- **Persistence**: Currently, mock data is reset every time the application process is restarted as Room persistence is pending full integration.
