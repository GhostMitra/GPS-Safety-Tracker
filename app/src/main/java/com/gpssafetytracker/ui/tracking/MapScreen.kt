package com.gpssafetytracker.ui.tracking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gpssafetytracker.data.model.Device
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: TrackingViewModel = viewModel()
) {
    val devices by viewModel.devices.collectAsStateWithLifecycle()
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }

    // Synchronize markers when devices update
    LaunchedEffect(devices, mapViewInstance) {
        val map = mapViewInstance ?: return@LaunchedEffect
        map.overlays.clear()
        devices.forEach { device ->
            val marker = Marker(map)
            marker.position = GeoPoint(device.latitude, device.longitude)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = device.name
            marker.snippet = "Battery: ${device.batteryLevel}% | Signal: ${device.signalStrength}/5"
            map.overlays.add(marker)
        }
        map.invalidate()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("GPS Safety Tracker", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (devices.isNotEmpty()) {
                        val firstDevice = devices.first()
                        mapViewInstance?.controller?.animateTo(
                            GeoPoint(firstDevice.latitude, firstDevice.longitude),
                            17.0,
                            1000L
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Center on Tracker")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            OSMMapContainer(
                modifier = Modifier.fillMaxSize(),
                onMapReady = { mapView ->
                    mapViewInstance = mapView
                    // Initial center
                    mapView.controller.setCenter(GeoPoint(37.7749, -122.4194))
                }
            )

            DeviceListOverlay(
                devices = devices,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}
