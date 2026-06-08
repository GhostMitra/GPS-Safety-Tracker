package com.gpssafetytracker.ui.sos

import androidx.lifecycle.ViewModel
import com.gpssafetytracker.data.SafetyRepository
import com.gpssafetytracker.data.model.SOSContact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class SOSViewModel : ViewModel() {

    private val _contacts = MutableStateFlow<List<SOSContact>>(emptyList())
    val contacts: StateFlow<List<SOSContact>> = _contacts.asStateFlow()

    val sosActive: StateFlow<Boolean> = SafetyRepository.sosActive
    val sosCoordinates: StateFlow<Pair<Double, Double>?> = SafetyRepository.sosCoordinates

    init {
        // Mock data matching technical.md
        _contacts.value = listOf(
            SOSContact("1", "Mom", "Priority 1", "555-0101", "mom@example.com", true, 1),
            SOSContact("2", "Dad", "Priority 2", "555-0102", "dad@example.com", true, 2)
        )
    }

    fun triggerSOS() {
        SafetyRepository.setSOSActive(true, Pair(37.7749, -122.4194))
    }

    fun cancelSOS() {
        SafetyRepository.setSOSActive(false)
    }

    fun toggleAlertEnabled(id: String) {
        _contacts.value = _contacts.value.map {
            if (it.id == id) it.copy(isAlertEnabled = !it.isAlertEnabled) else it
        }
    }

    fun addContact(name: String, phoneNumber: String, relation: String, email: String) {
        val newContact = SOSContact(
            id = UUID.randomUUID().toString(),
            name = name,
            relation = relation,
            phoneNumber = phoneNumber,
            email = email,
            priority = _contacts.value.size + 1
        )
        _contacts.value = _contacts.value + newContact
    }

    fun deleteContact(id: String) {
        _contacts.value = _contacts.value.filter { it.id != id }
    }
}
