package com.gpssafetytracker.data

import com.gpssafetytracker.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object SafetyRepository {
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _geofences = MutableStateFlow<List<Geofence>>(emptyList())
    val geofences: StateFlow<List<Geofence>> = _geofences.asStateFlow()

    private val _logs = MutableStateFlow<List<AlertLog>>(emptyList())
    val logs: StateFlow<List<AlertLog>> = _logs.asStateFlow()

    private val _history = MutableStateFlow<Map<String, List<Pair<Double, Double>>>>(emptyMap())
    val history: StateFlow<Map<String, List<Pair<Double, Double>>>> = _history.asStateFlow()

    private val _sosActive = MutableStateFlow(false)
    val sosActive: StateFlow<Boolean> = _sosActive.asStateFlow()

    private val _sosCoordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val sosCoordinates: StateFlow<Pair<Double, Double>?> = _sosCoordinates.asStateFlow()

    init {
        // Initialize with mock data from technical.md
        _devices.value = listOf(
            Device(
                id = "8C:94:DF:68:ED:40",
                name = "ESP32 Safety Tracker",
                type = DeviceType.TRACKER,
                latitude = 37.7749,
                longitude = -122.4194,
                speed = 0.0,
                batteryLevel = 100,
                signalStrength = 5,
                lastUpdated = System.currentTimeMillis(),
                status = DeviceStatus.ONLINE
            )
        )

        _geofences.value = listOf(
            Geofence("1", "Home", 37.7749, -122.4194, 500.0, true, GeofenceStatus.SAFE),
            Geofence("2", "School", 37.7849, -122.4294, 300.0, true, GeofenceStatus.SAFE)
        )

        _logs.value = listOf(
            AlertLog(
                id = "log-1",
                timestamp = System.currentTimeMillis() - 3600000,
                deviceName = "ESP32 Safety Tracker",
                geofenceName = "Home",
                type = AlertType.ENTRY,
                message = "ESP32 Safety Tracker entered Home."
            )
        )
    }

    fun updateDevices(newDevices: List<Device>) {
        _devices.value = newDevices
        
        // Update history
        val currentHistory = _history.value.toMutableMap()
        newDevices.forEach { device ->
            val path = currentHistory[device.id]?.toMutableList() ?: mutableListOf()
            val lastPos = path.lastOrNull()
            if (lastPos == null || lastPos.first != device.latitude || lastPos.second != device.longitude) {
                path.add(Pair(device.latitude, device.longitude))
                if (path.size > 30) path.removeAt(0)
                currentHistory[device.id] = path
            }
        }
        _history.value = currentHistory

        checkGeofences()
    }

    fun addGeofence(geofence: Geofence) {
        _geofences.value = _geofences.value + geofence
        checkGeofences()
    }

    fun toggleGeofence(id: String) {
        _geofences.value = _geofences.value.map {
            if (it.id == id) it.copy(isActive = !it.isActive) else it
        }
        checkGeofences()
    }

    fun deleteGeofence(id: String) {
        _geofences.value = _geofences.value.filter { it.id != id }
        checkGeofences()
    }

    fun setSOSActive(active: Boolean, coords: Pair<Double, Double>? = null) {
        _sosActive.value = active
        _sosCoordinates.value = coords
    }

    private fun checkGeofences() {
        val currentDevices = _devices.value
        val currentFences = _geofences.value
        val newLogs = mutableListOf<AlertLog>()
        var stateChanged = false

        val updatedFences = currentFences.map { fence ->
            if (!fence.isActive) return@map fence.copy(status = GeofenceStatus.SAFE)

            var isFenceBreached = false
            currentDevices.forEach { device ->
                val distance = getDistance(device.latitude, device.longitude, fence.latitude, fence.longitude)
                val isInside = distance <= fence.radius

                // For simulation logic matching technical.md
                if (device.id == "8C:94:DF:68:ED:40" && !isInside) {
                    isFenceBreached = true
                }

                // Log entry/exit logic could be added here similar to technical.md
                // But for simplicity, we'll just check breach status
            }

            val newStatus = if (isFenceBreached) GeofenceStatus.BREACHED else GeofenceStatus.SAFE
            if (newStatus != fence.status) {
                stateChanged = true
                fence.copy(status = newStatus)
            } else {
                fence
            }
        }

        if (stateChanged) {
            _geofences.value = updatedFences
        }
    }

    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371e3 // Earth radius in meters
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val deltaPhi = Math.toRadians(lat2 - lat1)
        val deltaLambda = Math.toRadians(lon2 - lon1)

        val a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return r * c
    }

    fun addLog(deviceName: String, geofenceName: String, type: AlertType, message: String) {
        val newLog = AlertLog(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            deviceName = deviceName,
            geofenceName = geofenceName,
            type = type,
            message = message
        )
        _logs.value = (listOf(newLog) + _logs.value).take(50)
    }
}
