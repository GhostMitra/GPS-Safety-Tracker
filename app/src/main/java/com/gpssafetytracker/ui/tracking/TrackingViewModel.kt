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

    private val targetMac = "8C:94:DF:68:ED:40"

    init {
        // Initialize with the ESP32 as static model data
        _devices.value = listOf(
            Device(
                id = targetMac,
                name = "ESP32 Safety Tracker",
                latitude = 37.7749,
                longitude = -122.4194,
                batteryLevel = 100,
                signalStrength = 5,
                lastUpdated = System.currentTimeMillis(),
                status = DeviceStatus.ONLINE
            )
        )

        // Simulate periodic location updates for the model data
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
