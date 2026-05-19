package com.gpssafetytracker.ui.tracking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gpssafetytracker.data.model.Device
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DeviceListOverlay(devices: List<Device>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.widthIn(max = 300.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Connected Trackers",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            devices.forEach { device ->
                DeviceItem(device)
                if (device != devices.last()) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun DeviceItem(device: Device) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = device.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(
                text = "Last seen: ${formatTime(device.lastUpdated)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.BatteryFull,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (device.batteryLevel < 20) Color.Red else Color.Unspecified
            )
            Text(text = "${device.batteryLevel}%", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                Icons.Default.SignalCellularAlt,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(text = "${device.signalStrength}/5", style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
