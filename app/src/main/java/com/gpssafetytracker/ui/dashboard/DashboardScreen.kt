package com.gpssafetytracker.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gpssafetytracker.data.model.*
import com.gpssafetytracker.ui.geofencing.GeofencingViewModel
import com.gpssafetytracker.ui.sos.SOSViewModel
import com.gpssafetytracker.ui.tracking.TrackingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    trackingViewModel: TrackingViewModel = viewModel(),
    geofencingViewModel: GeofencingViewModel = viewModel(),
    sosViewModel: SOSViewModel = viewModel()
) {
    val devices by trackingViewModel.devices.collectAsStateWithLifecycle()
    val geofences by geofencingViewModel.geofences.collectAsStateWithLifecycle()
    val sosActive by sosViewModel.sosActive.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Overview", fontWeight = FontWeight.Bold)
                },
                windowInsets = WindowInsets(0, 0, 0, 0), // Already handled by MainScreen
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // Already handled by MainScreen
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Grid
            item {
                StatsSection(devices, geofences, sosActive)
            }

            // Device Status Section
            item {
                SectionHeader("Tracked Devices", "${devices.size} Total")
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    devices.forEach { device ->
                        ProfessionalDeviceCard(device)
                    }
                }
            }

            // Recent Activity Section
            item {
                SectionHeader("Security Activity", "Last 24h")
                Spacer(modifier = Modifier.height(12.dp))
                ActivityLogItem(
                    message = "ESP32 Safety Tracker entered Home zone.",
                    time = "10:30 AM",
                    type = AlertType.ENTRY
                )
                Spacer(modifier = Modifier.height(8.dp))
                ActivityLogItem(
                    message = "Geofence 'School' was deactivated.",
                    time = "09:15 AM",
                    type = AlertType.EXIT
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun StatsSection(devices: List<Device>, geofences: List<Geofence>, sosActive: Boolean) {
    val onlineCount = devices.count { it.status == DeviceStatus.ONLINE }
    val breaches = geofences.count { it.isActive && it.status == GeofenceStatus.BREACHED }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // High-level overview row
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Connected",
                value = "$onlineCount/${devices.size}",
                subValue = "Devices Online",
                icon = Icons.Rounded.Devices,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Safety Zones",
                value = geofences.size.toString(),
                subValue = "${geofences.count { it.isActive }} Active",
                icon = Icons.Rounded.GppGood,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        
        // Critical Status Card - Spans full width for professionalism and prominence
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = if (sosActive || breaches > 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            if (sosActive || breaches > 0) MaterialTheme.colorScheme.error 
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (sosActive) Icons.Rounded.Emergency 
                        else if (breaches > 0) Icons.Rounded.GppMaybe 
                        else Icons.Rounded.Shield,
                        contentDescription = null,
                        tint = if (sosActive || breaches > 0) Color.White else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (sosActive) "SOS BROADCASTING" 
                        else if (breaches > 0) "SECURITY BREACH" 
                        else "System Secure",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (sosActive || breaches > 0) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        if (sosActive) "Emergency contacts notified" 
                        else if (breaches > 0) "$breaches zone(s) violated" 
                        else "All monitored zones safe",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (sosActive || breaches > 0) MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                if (sosActive || breaches > 0) {
                    Icon(
                        Icons.Rounded.ChevronRight,
                        null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subValue: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subValue, style = MaterialTheme.typography.labelSmall, color = contentColor.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun ProfessionalDeviceCard(device: Device) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = device.avatar,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(device.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "ID: ${device.id.take(8)}... • ${device.type.name.lowercase().replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusIndicator(device.status)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Rounded.SignalCellularAlt, null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("${device.signalStrength}/5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${device.speed} km/h", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                LinearBatteryIndicator(device.batteryLevel)
            }
        }
    }
}

@Composable
fun StatusIndicator(status: DeviceStatus) {
    val color = when (status) {
        DeviceStatus.ONLINE -> Color(0xFF006C4C)
        DeviceStatus.LOW_BATTERY -> Color(0xFFBA1A1A)
        DeviceStatus.OFFLINE -> MaterialTheme.colorScheme.outline
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(status.name.lowercase(), style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LinearBatteryIndicator(level: Int) {
    val color = when {
        level >= 50 -> Color(0xFF006C4C)
        level >= 20 -> Color(0xFFE28400)
        else -> Color(0xFFBA1A1A)
    }
    Column(horizontalAlignment = Alignment.End) {
        Text("$level%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = color)
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { level / 100f },
            modifier = Modifier.width(40.dp).height(4.dp).clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun ActivityLogItem(message: String, time: String, type: AlertType) {
    val color = if (type == AlertType.ENTRY) Color(0xFF006C4C) else Color(0xFFBA1A1A)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (type == AlertType.ENTRY) Icons.Rounded.VerifiedUser else Icons.Rounded.NotificationImportant,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(message, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
