package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DashboardViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Initialize Google AdMob SDK properly on startup
    AdManager.initialize(this)
    
    enableEdgeToEdge()
    setContent {
      // Initialize our state-bearing viewmodel securely
      val viewModel = remember { DashboardViewModel(application) }
      val activeSector by viewModel.activeSector

      // Theme shifts dynamically based on active sector
      MyApplicationTheme(activeSector = activeSector) {
        DashboardScreen(viewModel = viewModel)
      }
    }
  }
}
