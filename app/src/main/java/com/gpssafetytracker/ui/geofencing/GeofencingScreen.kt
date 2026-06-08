package com.gpssafetytracker.ui.geofencing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gpssafetytracker.data.model.Geofence
import com.gpssafetytracker.data.model.GeofenceStatus

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
                TopAppBar(
                    title = { Text("Virtual Boundaries", fontWeight = FontWeight.Bold) },
                    windowInsets = WindowInsets(0, 0, 0, 0) // Handled by MainScreen
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0), // Handled by MainScreen
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddEditor = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Rounded.AddLocationAlt, contentDescription = "New Zone")
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Safety Zones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Configure virtual perimeters for automated alerts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (geofences.isEmpty()) {
                    item {
                        EmptyGeofencesPlaceholder()
                    }
                } else {
                    items(geofences) { geofence ->
                        ProfessionalGeofenceCard(
                            geofence = geofence,
                            onToggle = { viewModel.toggleGeofence(geofence.id) },
                            onDelete = { viewModel.deleteGeofence(geofence.id) }
                        )
                    }
                }
                
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun ProfessionalGeofenceCard(
    geofence: Geofence,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val isBreached = geofence.status == GeofenceStatus.BREACHED && geofence.isActive
    val statusColor = if (geofence.isActive) {
        if (isBreached) MaterialTheme.colorScheme.error else Color(0xFF006C4C)
    } else {
        MaterialTheme.colorScheme.outline
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isBreached) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isBreached) Icons.Rounded.GppMaybe else Icons.Rounded.VerifiedUser,
                    null,
                    tint = statusColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = geofence.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${geofence.radius.toInt()}m Radius",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(MaterialTheme.colorScheme.outline))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (geofence.isActive) geofence.status.name else "Disabled",
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Switch(
                checked = geofence.isActive,
                onCheckedChange = { onToggle() },
                thumbContent = {
                    Icon(
                        if (geofence.isActive) Icons.Rounded.PlayArrow else Icons.Rounded.Square,
                        null,
                        Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            )
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Rounded.DeleteSweep, null, tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun EmptyGeofencesPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.Map, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No safety zones defined.", color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Medium)
    }
}
