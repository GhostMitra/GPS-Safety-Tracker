package com.gpssafetytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import com.gpssafetytracker.ui.MainScreen
import com.gpssafetytracker.ui.components.PermissionHandler
import com.gpssafetytracker.ui.theme.GPSSafetyTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPSSafetyTrackerTheme {
                PermissionHandler {
                    MainScreen()
                }
            }
        }
    }
}
