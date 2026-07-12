package com.gpssafetytracker.ui.tracking

import androidx.lifecycle.ViewModel
import com.gpssafetytracker.data.SafetyRepository
import com.gpssafetytracker.data.model.Device
import kotlinx.coroutines.flow.StateFlow

class TrackingViewModel : ViewModel() {
    val devices: StateFlow<List<Device>> = SafetyRepository.devices
}
