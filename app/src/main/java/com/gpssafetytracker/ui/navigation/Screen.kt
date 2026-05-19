package com.gpssafetytracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Tracking : Screen("tracking", "Tracking", Icons.Rounded.Map)
    object Geofencing : Screen("geofencing", "Geofences", Icons.Rounded.NotificationsActive)
    object SOS : Screen("sos", "SOS", Icons.Rounded.NotificationsActive) // We'll use a better icon if needed
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Rounded.Dashboard)
}
