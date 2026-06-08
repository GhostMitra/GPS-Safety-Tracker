package com.gpssafetytracker.ui.tracking

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
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
import org.osmdroid.views.overlay.Polyline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: TrackingViewModel = viewModel()
) {
    val devices by viewModel.devices.collectAsStateWithLifecycle()
    val history by com.gpssafetytracker.data.SafetyRepository.history.collectAsStateWithLifecycle()
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }

    // Synchronize markers when devices update
    LaunchedEffect(devices, history, mapViewInstance) {
        val map = mapViewInstance ?: return@LaunchedEffect
        map.overlays.clear()

        // Draw history paths with professional emerald color
        history.forEach { (_, path) ->
            if (path.size >= 2) {
                val polyline = Polyline(map)
                polyline.setPoints(path.map { GeoPoint(it.first, it.second) })
                polyline.outlinePaint.color = android.graphics.Color.parseColor("#006C4C") // Primary Emerald
                polyline.outlinePaint.strokeWidth = 8f
                polyline.outlinePaint.isAntiAlias = true
                map.overlays.add(polyline)
            }
        }

        devices.forEach { device ->
            val marker = Marker(map)
            marker.position = GeoPoint(device.latitude, device.longitude)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = device.name
            marker.snippet = "Battery: ${device.batteryLevel}% | Speed: ${device.speed} km/h"
            map.overlays.add(marker)
        }
        map.invalidate()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Tracking", fontWeight = FontWeight.Bold) },
                windowInsets = WindowInsets(0, 0, 0, 0), // Handled by MainScreen
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                )
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0), // Handled by MainScreen
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
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Rounded.MyLocation, contentDescription = "Center on Tracker")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            OSMMapContainer(
                modifier = Modifier.fillMaxSize(),
                onMapReady = { mapView ->
                    mapViewInstance = mapView
                    mapView.controller.setCenter(GeoPoint(37.7749, -122.4194))
                    mapView.controller.setZoom(15.0)
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
