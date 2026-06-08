package com.gpssafetytracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.GppMaybe
import androidx.compose.material.icons.rounded.NotificationImportant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gpssafetytracker.data.model.GeofenceStatus
import com.gpssafetytracker.ui.dashboard.DashboardScreen
import com.gpssafetytracker.ui.geofencing.GeofencingScreen
import com.gpssafetytracker.ui.geofencing.GeofencingViewModel
import com.gpssafetytracker.ui.navigation.Screen
import com.gpssafetytracker.ui.sos.SOSScreen
import com.gpssafetytracker.ui.sos.SOSViewModel
import com.gpssafetytracker.ui.tracking.MapScreen

@Composable
fun MainScreen(
    sosViewModel: SOSViewModel = viewModel(),
    geofencingViewModel: GeofencingViewModel = viewModel()
) {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Dashboard,
        Screen.Tracking,
        Screen.Geofencing,
        Screen.SOS
    )

    val sosActive by sosViewModel.sosActive.collectAsStateWithLifecycle()
    val geofences by geofencingViewModel.geofences.collectAsStateWithLifecycle()
    val hasBreach = geofences.any { it.isActive && it.status == GeofenceStatus.BREACHED }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title, style = MaterialTheme.typography.labelMedium) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // System Status Bar Spacer
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

            // SOS Active Banner - Professional Emergency UI
            AnimatedVisibility(
                visible = sosActive,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.NotificationImportant, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("SOS BROADCAST ACTIVE", color = Color.White, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black)
                            Text("Emergency contacts notified", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                        }
                        Button(
                            onClick = { sosViewModel.cancelSOS() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.error),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("STOP", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Geofence Breach Banner - Polished Alert UI
            AnimatedVisibility(
                visible = hasBreach && !sosActive,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { navController.navigate(Screen.Tracking.route) },
                    color = Color(0xFFE28400),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.GppMaybe, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "CRITICAL GEOFENCE BREACH",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Rounded.ChevronRight, null, tint = Color.White)
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Dashboard.route) { DashboardScreen() }
                composable(Screen.Tracking.route) { MapScreen() }
                composable(Screen.Geofencing.route) { GeofencingScreen() }
                composable(Screen.SOS.route) { SOSScreen() }
            }
        }
    }
}
