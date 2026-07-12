package com.gpssafetytracker.ui.dashboard

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val hasBreach = geofences.any { it.isActive && it.status == GeofenceStatus.BREACHED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Command Center",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. System Health Hero
            item {
                SystemHealthHero(sosActive, hasBreach)
            }

            // 2. Metrics Grid
            item {
                MetricsGrid(devices, geofences)
            }

            // 3. Live Device Feed
            item {
                SectionHeader("Active Trackers", "${devices.size} Live")
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    devices.forEach { device ->
                        CompactDeviceItem(device)
                    }
                }
            }

            // 4. Security Timeline
            item {
                SectionHeader("Recent Activity", "Timeline")
                Spacer(modifier = Modifier.height(16.dp))
                SecurityTimeline()
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SystemHealthHero(sosActive: Boolean, hasBreach: Boolean) {
    val statusColor = when {
        sosActive -> MaterialTheme.colorScheme.error
        hasBreach -> Color(0xFFE28400) // Warning Orange
        else -> MaterialTheme.colorScheme.primary
    }
    
    val statusText = when {
        sosActive -> "SOS BROADCASTING"
        hasBreach -> "SECURITY BREACH"
        else -> "SYSTEM SECURE"
    }

    val statusDesc = when {
        sosActive -> "Emergency contacts have been notified."
        hasBreach -> "A safety zone has been violated."
        else -> "All parameters within normal limits."
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = statusColor.copy(alpha = 0.08f),
        shape = RoundedCornerShape(24.dp),
        border = WindowInsets(0).let { _ -> 
            androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.2f))
        }
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(statusColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (sosActive || hasBreach) Icons.Rounded.GppMaybe else Icons.Rounded.VerifiedUser,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = statusColor
                )
                Text(
                    text = statusDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MetricsGrid(devices: List<Device>, geofences: List<Geofence>) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        MetricCard(
            modifier = Modifier.weight(1f),
            label = "Trackers",
            value = devices.size.toString(),
            subValue = "${devices.count { it.status == DeviceStatus.ONLINE }} Online",
            icon = Icons.Rounded.Sensors,
            color = MaterialTheme.colorScheme.primary
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            label = "Boundaries",
            value = geofences.size.toString(),
            subValue = "${geofences.count { it.isActive }} Active",
            icon = Icons.Rounded.GppGood,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    subValue: String,
    icon: ImageVector,
    color: Color
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(subValue, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CompactDeviceItem(device: Device) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Navigation, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(device.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text("Last updated: Just now", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            StatusBadge(device.status)
        }
    }
}

@Composable
fun StatusBadge(status: DeviceStatus) {
    val color = when (status) {
        DeviceStatus.ONLINE -> MaterialTheme.colorScheme.primary
        DeviceStatus.LOW_BATTERY -> MaterialTheme.colorScheme.error
        DeviceStatus.OFFLINE -> MaterialTheme.colorScheme.outline
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}

@Composable
fun SecurityTimeline() {
    Column {
        TimelineItem("Home perimeter entered", "12:45 PM", isLatest = true)
        TimelineItem("School zone check-in", "11:20 AM")
        TimelineItem("Device connected to network", "09:00 AM", isLast = true)
    }
}

@Composable
fun TimelineItem(text: String, time: String, isLatest: Boolean = false, isLast: Boolean = false) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (isLatest) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
        Column(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(text, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            Text(time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SectionHeader(title: String, badge: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            shape = CircleShape
        ) {
            Text(
                badge,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
