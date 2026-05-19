package com.gpssafetytracker.ui.geofencing

import android.view.MotionEvent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gpssafetytracker.ui.tracking.OSMMapContainer
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Overlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeofenceEditorScreen(
    onDismiss: () -> Unit,
    onSave: (String, Double, Double, Double) -> Unit
) {
    var name by remember { mutableStateOf("Safe Zone") }
    var radius by remember { mutableFloatStateOf(500f) }
    var center by remember { mutableStateOf(GeoPoint(37.7749, -122.4194)) }
    var mapViewInstance by remember { mutableStateOf<MapView?>(null) }

    // Overlay to handle tap for center position
    val tapOverlay = remember {
        object : Overlay() {
            override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
                val projection = mapView.projection
                val geoPoint = projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                center = geoPoint
                return true
            }
        }
    }

    LaunchedEffect(center, radius, mapViewInstance) {
        val map = mapViewInstance ?: return@LaunchedEffect
        map.overlays.clear()
        
        // Add tap overlay
        map.overlays.add(tapOverlay)

        // Circle Polygon
        val circle = Polygon(map)
        circle.points = Polygon.pointsAsCircle(center, radius.toDouble())
        circle.fillPaint.color = Color(0x33FF6D00).hashCode() // Transparent Orange
        circle.outlinePaint.color = Color(0xFFFF6D00).hashCode()
        circle.outlinePaint.strokeWidth = 5f
        map.overlays.add(circle)

        // Center Marker
        val marker = Marker(map)
        marker.position = center
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Center"
        marker.isDraggable = true
        marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {
                center = marker.position
            }
            override fun onMarkerDragEnd(marker: Marker) {
                center = marker.position
            }
            override fun onMarkerDragStart(marker: Marker) {}
        })
        map.overlays.add(marker)

        map.invalidate()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Interactive Geofence", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    IconButton(onClick = { onSave(name, center.latitude, center.longitude, radius.toDouble()) }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Box(modifier = Modifier.weight(1f)) {
                OSMMapContainer(
                    modifier = Modifier.fillMaxSize(),
                    onMapReady = { mapView ->
                        mapViewInstance = mapView
                        mapView.controller.setCenter(center)
                    }
                )
                
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Radius: ${radius.toInt()}m", style = MaterialTheme.typography.bodySmall)
                        Slider(
                            value = radius,
                            onValueChange = { radius = it },
                            valueRange = 100f..2000f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
