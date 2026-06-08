package com.gpssafetytracker.ui.geofencing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gpssafetytracker.data.SafetyRepository
import com.gpssafetytracker.data.model.Geofence
import com.gpssafetytracker.data.model.GeofenceStatus
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class GeofencingViewModel : ViewModel() {

    val geofences: StateFlow<List<Geofence>> = SafetyRepository.geofences

    fun addGeofence(name: String, lat: Double, lng: Double, radius: Double) {
        val newGeofence = Geofence(
            id = UUID.randomUUID().toString(),
            name = name,
            latitude = lat,
            longitude = lng,
            radius = radius,
            isActive = true,
            status = GeofenceStatus.SAFE
        )
        SafetyRepository.addGeofence(newGeofence)
    }

    fun toggleGeofence(id: String) {
        SafetyRepository.toggleGeofence(id)
    }

    fun deleteGeofence(id: String) {
        SafetyRepository.deleteGeofence(id)
    }
}
