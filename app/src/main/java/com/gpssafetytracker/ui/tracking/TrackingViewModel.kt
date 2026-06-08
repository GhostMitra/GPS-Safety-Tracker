package com.gpssafetytracker.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gpssafetytracker.data.SafetyRepository
import com.gpssafetytracker.data.model.Device
import com.gpssafetytracker.data.model.DeviceStatus
import com.gpssafetytracker.data.model.DeviceType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class TrackingViewModel : ViewModel() {

    val devices: StateFlow<List<Device>> = SafetyRepository.devices

    private val targetMac = "8C:94:DF:68:ED:40"

    init {
        // Simulate periodic location updates for the model data
        startLocationSimulation()
    }

    private fun startLocationSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(5000) // Update every 5 seconds
                val currentDevices = devices.value
                val updatedDevices = currentDevices.map { device ->
                    val latJitter = (Random.nextDouble() - 0.5) * 0.0006
                    val lngJitter = (Random.nextDouble() - 0.5) * 0.0006
                    val newLat = device.latitude + latJitter
                    val newLng = device.longitude + lngJitter

                    val speedChange = (Random.nextDouble() - 0.5) * 2
                    val newSpeed = Math.max(0.0, Math.round((device.speed + speedChange) * 10) / 10.0)

                    val newBattery = Math.max(0, device.batteryLevel - (if (Random.nextDouble() > 0.9) 1 else 0))
                    val newStatus = if (newBattery < 20) DeviceStatus.LOW_BATTERY else DeviceStatus.ONLINE

                    device.copy(
                        latitude = newLat,
                        longitude = newLng,
                        speed = newSpeed,
                        batteryLevel = newBattery,
                        status = newStatus,
                        lastUpdated = System.currentTimeMillis()
                    )
                }
                SafetyRepository.updateDevices(updatedDevices)
            }
        }
    }
}
