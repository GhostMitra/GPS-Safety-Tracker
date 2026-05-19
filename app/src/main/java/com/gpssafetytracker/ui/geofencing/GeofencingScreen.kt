package com.gpssafetytracker.ui.geofencing

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gpssafetytracker.data.model.Geofence

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeofencingScreen(
    viewModel: GeofencingViewModel = viewModel()
) {
    val geofences by viewModel.geofences.collectAsStateWithLifecycle()
    var showAddEditor by remember { mutableStateOf(false) }

    if (showAddEditor) {
        GeofenceEditorScreen(
            onDismiss = { showAddEditor = false },
            onSave = { name, lat, lng, radius ->
                viewModel.addGeofence(name, lat, lng, radius)
                showAddEditor = false
            }
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Geofencing Management", fontWeight = FontWeight.Bold) }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddEditor = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add Geofence")
                }
            }
        ) { padding ->
            if (geofences.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No geofences defined. Tap + to add one.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(geofences) { geofence ->
                        GeofenceCard(
                            geofence = geofence,
                            onToggle = { viewModel.toggleGeofence(geofence.id) },
                            onDelete = { viewModel.deleteGeofence(geofence.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GeofenceCard(
    geofence: Geofence,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = geofence.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Radius: ${geofence.radius}m",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = geofence.isActive,
                onCheckedChange = { onToggle() }
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
