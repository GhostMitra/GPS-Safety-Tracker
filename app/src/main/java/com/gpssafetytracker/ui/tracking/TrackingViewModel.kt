package com.gpssafetytracker.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gpssafetytracker.data.model.Device
import com.gpssafetytracker.data.model.DeviceStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class TrackingViewModel : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    init {
        // Initialize with some mock devices
        _devices.value = listOf(
            Device(
                id = "1",
                name = "Child 1 Tracker",
                latitude = 37.7749,
                longitude = -122.4194,
                batteryLevel = 85,
                signalStrength = 4,
                lastUpdated = System.currentTimeMillis(),
                status = DeviceStatus.ONLINE
            ),
            Device(
                id = "2",
                name = "Child 2 Tracker",
                latitude = 37.7849,
                longitude = -122.4294,
                batteryLevel = 45,
                signalStrength = 3,
                lastUpdated = System.currentTimeMillis(),
                status = DeviceStatus.ONLINE
            )
        )

        // Simulate periodic location updates
        startLocationSimulation()
    }

    private fun startLocationSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(5000) // Update every 5 seconds
                _devices.value = _devices.value.map { device ->
                    device.copy(
                        latitude = device.latitude + (Random.nextDouble() - 0.5) * 0.001,
                        longitude = device.longitude + (Random.nextDouble() - 0.5) * 0.001,
                        lastUpdated = System.currentTimeMillis()
                    )
                }
            }
        }
    }
}
