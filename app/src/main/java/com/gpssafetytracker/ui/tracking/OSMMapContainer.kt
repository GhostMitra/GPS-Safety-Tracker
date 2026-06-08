package com.gpssafetytracker.ui.tracking

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
    val isDarkTheme = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(15.0)
        }
    }

    // Apply Material-style Monochrome filters to match the theme perfectly
    LaunchedEffect(isDarkTheme, surfaceColor, onSurfaceColor) {
        val matrix = ColorMatrix()
        
        if (isDarkTheme) {
            // Dark Mode: Sophisticated Deep Charcoal integration
            // 1. Invert + Desaturate
            matrix.set(floatArrayOf(
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
            ))
            val desaturate = ColorMatrix()
            desaturate.setSaturation(0f)
            matrix.postConcat(desaturate)
            
            // 2. Map to Surface and a muted tone of Primary/OnSurface
            val rSurface = (surfaceColor shr 16 and 0xFF) / 255f
            val gSurface = (surfaceColor shr 8 and 0xFF) / 255f
            val bSurface = (surfaceColor and 0xFF) / 255f
            
            val rTarget = (onSurfaceColor shr 16 and 0xFF) / 255f
            val gTarget = (onSurfaceColor shr 8 and 0xFF) / 255f
            val bTarget = (onSurfaceColor and 0xFF) / 255f
            
            // Mute the target colors for a more subtle "professional" map look
            val rs = (rTarget - rSurface) * 0.4f
            val gs = (gTarget - gSurface) * 0.4f
            val bs = (bTarget - bSurface) * 0.4f
            
            matrix.postConcat(ColorMatrix(floatArrayOf(
                rs, 0f, 0f, 0f, rSurface * 255f,
                0f, gs, 0f, 0f, gSurface * 255f,
                0f, 0f, bs, 0f, bSurface * 255f,
                0f, 0f, 0f, 1f, 0f
            )))
        } else {
            // Light Mode: Clean architectural grayscale
            matrix.setSaturation(0f)
            val scale = 0.85f
            matrix.postConcat(ColorMatrix(floatArrayOf(
                scale, 0f, 0f, 0f, 30f,
                0f, scale, 0f, 0f, 30f,
                0f, 0f, scale, 0f, 30f,
                0f, 0f, 0f, 1f, 0f
            )))
        }
        
        mapView.overlayManager.tilesOverlay.setColorFilter(ColorMatrixColorFilter(matrix))
        mapView.setBackgroundColor(surfaceColor)
        mapView.invalidate()
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
