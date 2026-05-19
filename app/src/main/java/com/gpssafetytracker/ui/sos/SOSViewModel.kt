package com.gpssafetytracker.ui.sos

import androidx.lifecycle.ViewModel
import com.gpssafetytracker.data.model.SOSContact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class SOSViewModel : ViewModel() {

    private val _contacts = MutableStateFlow<List<SOSContact>>(emptyList())
    val contacts: StateFlow<List<SOSContact>> = _contacts.asStateFlow()

    init {
        // Mock data
        _contacts.value = listOf(
            SOSContact("1", "Mom", "555-0101", 1),
            SOSContact("2", "Dad", "555-0102", 2)
        )
    }

    fun addContact(name: String, phoneNumber: String, priority: Int) {
        val newContact = SOSContact(
            id = UUID.randomUUID().toString(),
            name = name,
            phoneNumber = phoneNumber,
            priority = priority
        )
        _contacts.value = _contacts.value + newContact
    }

    fun deleteContact(id: String) {
        _contacts.value = _contacts.value.filter { it.id != id }
    }
}
