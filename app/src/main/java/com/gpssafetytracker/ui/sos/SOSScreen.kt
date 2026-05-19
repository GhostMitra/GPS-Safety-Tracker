package com.gpssafetytracker.ui.sos

import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gpssafetytracker.data.model.SOSContact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SOSScreen(
    viewModel: SOSViewModel = viewModel()
) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var prefilledName by remember { mutableStateOf("") }
    var prefilledPhone by remember { mutableStateOf("") }

    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let { contactUri ->
            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
            )
            context.contentResolver.query(contactUri, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    
                    prefilledName = name
                    
                    // Query phone number
                    context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )?.use { phoneCursor ->
                        if (phoneCursor.moveToFirst()) {
                            val phone = phoneCursor.getString(
                                phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            prefilledPhone = phone
                        }
                    }
                    showAddDialog = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Emergency SOS Management", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                SmallFloatingActionButton(
                    onClick = { contactPickerLauncher.launch(null) },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(Icons.Rounded.ContactPhone, contentDescription = "Pick Contact")
                }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = { 
                        prefilledName = ""
                        prefilledPhone = ""
                        showAddDialog = true 
                    },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add Contact")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SOSWarningCard()
            
            if (contacts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No emergency contacts. Add them to receive alerts.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(contacts) { contact ->
                        SOSContactCard(
                            contact = contact,
                            onDelete = { viewModel.deleteContact(contact.id) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            AddContactDialog(
                initialName = prefilledName,
                initialPhone = prefilledPhone,
                onDismiss = { showAddDialog = false },
                onAdd = { name, phone, priority ->
                    viewModel.addContact(name, phone, priority)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun SOSWarningCard() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "SOS mode will alert all contacts below simultaneously with live location updates.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun SOSContactCard(
    contact: SOSContact,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.Person,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddContactDialog(
    initialName: String = "",
    initialPhone: String = "",
    onDismiss: () -> Unit,
    onAdd: (String, String, Int) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }
    var priority by remember { mutableStateOf("1") }

    // Update state if initial values change (e.g. from picker)
    LaunchedEffect(initialName, initialPhone) {
        name = initialName
        phone = initialPhone
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Emergency Contact") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = priority,
                    onValueChange = { priority = it },
                    label = { Text("Priority (1 = High)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(name, phone, priority.toIntOrNull() ?: 1) },
                enabled = name.isNotBlank() && phone.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
