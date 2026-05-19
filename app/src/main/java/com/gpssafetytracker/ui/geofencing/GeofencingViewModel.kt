package com.gpssafetytracker.ui.geofencing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gpssafetytracker.data.model.Geofence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class GeofencingViewModel : ViewModel() {

    private val _geofences = MutableStateFlow<List<Geofence>>(emptyList())
    val geofences: StateFlow<List<Geofence>> = _geofences.asStateFlow()

    init {
        // Mock data
        _geofences.value = listOf(
            Geofence("1", "Home", 37.7749, -122.4194, 500.0, true),
            Geofence("2", "School", 37.7849, -122.4294, 300.0, true)
        )
    }

    fun addGeofence(name: String, lat: Double, lng: Double, radius: Double) {
        val newGeofence = Geofence(
            id = UUID.randomUUID().toString(),
            name = name,
            latitude = lat,
            longitude = lng,
            radius = radius,
            isActive = true
        )
        _geofences.value = _geofences.value + newGeofence
    }

    fun toggleGeofence(id: String) {
        _geofences.value = _geofences.value.map {
            if (it.id == id) it.copy(isActive = !it.isActive) else it
        }
    }

    fun deleteGeofence(id: String) {
        _geofences.value = _geofences.value.filter { it.id != id }
    }
}
