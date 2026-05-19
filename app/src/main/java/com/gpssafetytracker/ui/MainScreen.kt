package com.gpssafetytracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gpssafetytracker.ui.dashboard.DashboardScreen
import com.gpssafetytracker.ui.geofencing.GeofencingScreen
import com.gpssafetytracker.ui.navigation.Screen
import com.gpssafetytracker.ui.sos.SOSScreen
import com.gpssafetytracker.ui.tracking.MapScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Tracking,
        Screen.Geofencing,
        Screen.SOS,
        Screen.Dashboard
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tracking.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Tracking.route) { MapScreen() }
            composable(Screen.Geofencing.route) { GeofencingScreen() }
            composable(Screen.SOS.route) { SOSScreen() }
            composable(Screen.Dashboard.route) { DashboardScreen() }
        }
    }
}
