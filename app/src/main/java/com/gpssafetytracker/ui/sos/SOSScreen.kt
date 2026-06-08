package com.gpssafetytracker.ui.sos

import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gpssafetytracker.data.model.SOSContact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SOSScreen(
    viewModel: SOSViewModel = viewModel()
) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    val sosActive by viewModel.sosActive.collectAsStateWithLifecycle()
    val sosCoords by viewModel.sosCoordinates.collectAsStateWithLifecycle()
    
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
            TopAppBar(
                title = { Text("Emergency Center", fontWeight = FontWeight.Bold) },
                windowInsets = WindowInsets(0, 0, 0, 0) // Handled by MainScreen
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0), // Handled by MainScreen
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallFloatingActionButton(
                    onClick = { contactPickerLauncher.launch(null) },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(Icons.Rounded.PersonAdd, contentDescription = "Import from Phone")
                }
                FloatingActionButton(
                    onClick = {
                        prefilledName = ""
                        prefilledPhone = ""
                        showAddDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Rounded.Add, "Add Contact Manually")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Panic Button Section
            item {
                PanicSection(
                    active = sosActive,
                    coords = sosCoords,
                    onToggle = { if (sosActive) viewModel.cancelSOS() else viewModel.triggerSOS() }
                )
            }

            // Contacts Header
            item {
                Text(
                    "Emergency Recipients",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Contacts who will receive alerts during an SOS event.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (contacts.isEmpty()) {
                item {
                    EmptyContactsPlaceholder()
                }
            } else {
                items(contacts) { contact ->
                    ProfessionalContactCard(
                        contact = contact,
                        onToggleAlert = { viewModel.toggleAlertEnabled(contact.id) },
                        onDelete = { viewModel.deleteContact(contact.id) }
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        if (showAddDialog) {
            AddContactDialog(
                initialName = prefilledName,
                initialPhone = prefilledPhone,
                onDismiss = { showAddDialog = false },
                onAdd = { name, relation, phone, email ->
                    viewModel.addContact(name, phone, relation, email)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun PanicSection(
    active: Boolean,
    coords: Pair<Double, Double>?,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Professional Pulse Button
            Box(contentAlignment = Alignment.Center) {
                if (active) {
                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.8f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = LinearOutSlowInEasing),
                            repeatMode = RepeatMode.Restart
                        ), label = "scale"
                    )
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.5f,
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = LinearOutSlowInEasing),
                            repeatMode = RepeatMode.Restart
                        ), label = "alpha"
                    )
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = alpha))
                    )
                }

                Surface(
                    onClick = onToggle,
                    modifier = Modifier.size(180.dp),
                    shape = CircleShape,
                    color = if (active) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.error.copy(alpha = 0.9f),
                    shadowElevation = 16.dp,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Emergency,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (active) "STOP" else "SOS",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Coords/Status Badge
            Surface(
                color = if (active) MaterialTheme.colorScheme.error.copy(alpha = 0.1f) else Color(0xFF006C4C).copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (active) Icons.Rounded.BroadcastOnHome else Icons.Rounded.Shield,
                        null,
                        modifier = Modifier.size(18.dp),
                        tint = if (active) MaterialTheme.colorScheme.error else Color(0xFF006C4C)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        if (active && coords != null) "${String.format("%.4f", coords.first)}, ${String.format("%.4f", coords.second)}" else "SYSTEM ARMED & READY",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (active) MaterialTheme.colorScheme.error else Color(0xFF006C4C)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfessionalContactCard(
    contact: SOSContact,
    onToggleAlert: () -> Unit,
    onDelete: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    contact.name.take(1).uppercase(),
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(contact.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${contact.phoneNumber} • ${contact.relation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Switch(
                checked = contact.isAlertEnabled,
                onCheckedChange = { onToggleAlert() },
                thumbContent = {
                    Icon(
                        if (contact.isAlertEnabled) Icons.Rounded.NotificationsActive else Icons.Rounded.NotificationsOff,
                        null,
                        Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            )

            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.DeleteOutline, null, tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun EmptyContactsPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.PeopleOutline, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No emergency contacts found.", color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AddContactDialog(
    initialName: String = "",
    initialPhone: String = "",
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var relation by remember { mutableStateOf("Family") }
    var phone by remember { mutableStateOf(initialPhone) }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Contact Details") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = relation, onValueChange = { relation = it }, label = { Text("Relationship") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email (Optional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(name, relation, phone, email) },
                enabled = name.isNotBlank() && phone.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Contact")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
