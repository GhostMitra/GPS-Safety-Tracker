package com.gpssafetytracker.ui.tracking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsDisplay
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun OSMMapContainer(
    modifier: Modifier = Modifier,
    showUserLocation: Boolean = true,
    onMapReady: (MapView) -> Unit = {}
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(15.0)
        }
    }

    val locationOverlay = remember(mapView) {
        MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
            enableMyLocation()
            enableFollowLocation()
        }
    }

    DisposableEffect(mapView, showUserLocation) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        
        if (showUserLocation) {
            mapView.overlays.add(locationOverlay)
        } else {
            mapView.overlays.remove(locationOverlay)
        }
        
        onMapReady(mapView)
        onDispose {
            locationOverlay.disableMyLocation()
            locationOverlay.disableFollowLocation()
            mapView.onDetach()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
