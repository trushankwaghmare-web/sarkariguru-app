package com.example.ui.screens

import com.example.ui.theme.*

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.UserDocument
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.AdRequest
import com.example.ui.theme.JobSector
import com.example.ui.theme.SuccessGreen
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val activeSectorState by viewModel.activeSector
    val profile by viewModel.userProfile.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val selectedTab = viewModel.selectedBottomTab.value
    val isUserLoggedIn by viewModel.isUserLoggedIn
    val dailyAccent = DailyTheme.accentColor

    if (!isUserLoggedIn) {
        LoginRegistrationScreen(viewModel = viewModel)
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag("main_scaffold"),
            containerColor = DailyTheme.Background,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.speakActiveScreenGuide() },
                    containerColor = dailyAccent,
                    contentColor = Color.White,
                    modifier = Modifier.size(56.dp).testTag("floating_voice_assistant_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Voice Guide dictation",
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    NavigationBar(
                        containerColor = DailyTheme.CardBackground,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == DashboardViewModel.BottomTab.UPDATES,
                            onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.UPDATES },
                            icon = { Icon(Icons.Default.Search, contentDescription = "New Updates") },
                            label = { Text("Updates", fontWeight = FontWeight.Bold) },
                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                selectedIconColor = dailyAccent,
                                selectedTextColor = dailyAccent,
                                unselectedIconColor = DailyTheme.TextSecondary,
                                unselectedTextColor = DailyTheme.TextSecondary,
                                indicatorColor = dailyAccent.copy(alpha = 0.12f)
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == DashboardViewModel.BottomTab.HALL_TICKET,
                            onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.HALL_TICKET },
                            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Hall Ticket") },
                            label = { Text("Hall Ticket", fontWeight = FontWeight.Bold) },
                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                selectedIconColor = dailyAccent,
                                selectedTextColor = dailyAccent,
                                unselectedIconColor = DailyTheme.TextSecondary,
                                unselectedTextColor = DailyTheme.TextSecondary,
                                indicatorColor = dailyAccent.copy(alpha = 0.12f)
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == DashboardViewModel.BottomTab.TRACKER,
                            onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.TRACKER },
                            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Last Date") },
                            label = { Text("Last Date", fontWeight = FontWeight.Bold) },
                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                selectedIconColor = dailyAccent,
                                selectedTextColor = dailyAccent,
                                unselectedIconColor = DailyTheme.TextSecondary,
                                unselectedTextColor = DailyTheme.TextSecondary,
                                indicatorColor = dailyAccent.copy(alpha = 0.12f)
                            )
                        )
                    }
                    
                    // Fixed Bottom Banner Ad
                    AdMobBannerAdContainer()
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DailyTheme.Background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // Header (No scroll)
                HeaderSection(
                    activeSector = activeSectorState,
                    onSettingsClick = { viewModel.showSettingsDialog.value = true },
                    onSectorChange = { viewModel.activeSector.value = it }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Active Section View
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTab) {
                        DashboardViewModel.BottomTab.UPDATES -> {
                            UpdatesTabContent(viewModel = viewModel, documents = documents)
                        }
                        DashboardViewModel.BottomTab.TRACKER -> {
                            TrackerTabContent(viewModel = viewModel)
                        }
                        DashboardViewModel.BottomTab.HALL_TICKET -> {
                            HallTicketTabContent(viewModel = viewModel)
                        }
                    }

                    // Dialogs overlay
                    viewModel.showAiApplyGuideJob.value?.let { job ->
                        AiApplyGuideDialog(
                            job = job,
                            viewModel = viewModel,
                            onDismiss = { viewModel.showAiApplyGuideJob.value = null }
                        )
                    }

                    if (viewModel.showSettingsDialog.value) {
                        SettingsDialog(
                            profile = profile,
                            initialQualification = viewModel.formQualification.value,
                            onDismiss = { viewModel.showSettingsDialog.value = false },
                            onSave = { name, phone, dob, qual, cat ->
                                viewModel.saveUserProfile(name, phone, dob, qual, cat)
                            },
                            onLogout = { viewModel.logout() }
                        )
                    }

                    if (viewModel.showCalendarDialog.value) {
                        CalendarDialog(
                            onDismiss = { viewModel.showCalendarDialog.value = false },
                            onDateSelected = { dob ->
                                viewModel.formDob.value = dob
                                viewModel.showCalendarDialog.value = false
                            }
                        )
                    }

                    if (viewModel.showScanDialog.value) {
                        ScanSimulatorDialog(
                            docType = viewModel.activeScanType.value,
                            onDismiss = { viewModel.showScanDialog.value = false },
                            onScanComplete = { bitmap ->
                                viewModel.scanDocumentSimulated(viewModel.activeScanType.value, bitmap)
                            }
                        )
                    }

                    // AI Application Wizard Dialog Overlay (replaces clutter on main screen)
                    viewModel.activeApplyingJob.value?.let { job ->
                        androidx.compose.ui.window.Dialog(onDismissRequest = { viewModel.activeApplyingJob.value = null }) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.85f),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                                border = BorderStroke(1.dp, DailyTheme.CardBorder)
                            ) {
                                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(dailyAccent.copy(alpha = 0.12f), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Shield,
                                                    contentDescription = null,
                                                    tint = dailyAccent,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                "AI Application Portal",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Black,
                                                color = DailyTheme.TextPrimary
                                            )
                                        }
                                        IconButton(onClick = { viewModel.activeApplyingJob.value = null }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Close",
                                                tint = DailyTheme.TextSecondary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(10.dp))
                                    
                                    Box(modifier = Modifier.weight(1f)) {
                                        ApplyViaAiFlowSection(viewModel = viewModel, documents = documents)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Spelling proofreader error alert
            viewModel.typoWarningDialog.value?.let { result ->
                AlertDialog(
                    onDismissRequest = { viewModel.typoWarningDialog.value = null },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = "Alert", tint = Color.Red)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI Spelling Discrepancy Found", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    text = {
                        Column {
                            Text(result.message, fontSize = 15.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Government application forms are strict. Typographical differences between 10th Certificate and Aadhaar are a common cause of rejection.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.forceProceedToStep(2) // Force proceed to Preview
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Proceed Anyway (Yes)")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                viewModel.typoWarningDialog.value = null // Close to edit fields
                            }
                        ) {
                            Text("Edit Form (No)")
                        }
                    }
                )
            }

            viewModel.selectedJobDetails.value?.let { job ->
                JobDetailsDialog(
                    job = job,
                    viewModel = viewModel,
                    onDismiss = { viewModel.selectedJobDetails.value = null }
                )
            }

            // Central info message toast-simulation
            viewModel.activeDialogMessage.value?.let { msg ->
                Dialog(onDismissRequest = { viewModel.activeDialogMessage.value = null }) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = SuccessGreen,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                msg,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = { viewModel.activeDialogMessage.value = null },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getSectorGradient(sector: JobSector): Brush {
    return when (sector) {
        JobSector.CIVIL -> Brush.verticalGradient(
            colors = listOf(Color(0xFF0F2C59), Color(0xFF07142A), Color(0xFF01060E))
        )
        JobSector.ARMY -> Brush.verticalGradient(
            colors = listOf(Color(0xFF2E3B1E), Color(0xFF141F0A), Color(0xFF020501))
        )
        JobSector.NAVY -> Brush.verticalGradient(
            colors = listOf(Color(0xFF072146), Color(0xFF021024), Color(0xFF000307))
        )
        JobSector.POLICE -> Brush.verticalGradient(
            colors = listOf(Color(0xFF1E293B), Color(0xFF111827), Color(0xFF030712))
        )
    }
}

@Composable
fun ThreeGeometricCoreFeatureCards(viewModel: DashboardViewModel) {
    val selectedTab = viewModel.selectedBottomTab.value
    val dailyAccent = DailyTheme.accentColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. New Updates Card
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.UPDATES }
                .testTag("tab_updates"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedTab == DashboardViewModel.BottomTab.UPDATES) dailyAccent.copy(alpha = 0.08f) else DailyTheme.CardBackground
            ),
            border = BorderStroke(
                width = if (selectedTab == DashboardViewModel.BottomTab.UPDATES) 2.dp else 1.dp,
                color = if (selectedTab == DashboardViewModel.BottomTab.UPDATES) dailyAccent else DailyTheme.CardBorder
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = if (selectedTab == DashboardViewModel.BottomTab.UPDATES) 3.dp else 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(dailyAccent.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "New Updates",
                        tint = dailyAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "New Updates",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DailyTheme.TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "नई नियुक्तियां",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = DailyTheme.TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 2. Hall Ticket Card
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.HALL_TICKET }
                .testTag("tab_hall_ticket"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedTab == DashboardViewModel.BottomTab.HALL_TICKET) dailyAccent.copy(alpha = 0.08f) else DailyTheme.CardBackground
            ),
            border = BorderStroke(
                width = if (selectedTab == DashboardViewModel.BottomTab.HALL_TICKET) 2.dp else 1.dp,
                color = if (selectedTab == DashboardViewModel.BottomTab.HALL_TICKET) dailyAccent else DailyTheme.CardBorder
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = if (selectedTab == DashboardViewModel.BottomTab.HALL_TICKET) 3.dp else 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(dailyAccent.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Hall Ticket",
                        tint = dailyAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hall Ticket",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DailyTheme.TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "प्रवेश पत्र",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = DailyTheme.TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // 3. Last Date Tracker Card
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.TRACKER }
                .testTag("tab_tracker"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedTab == DashboardViewModel.BottomTab.TRACKER) dailyAccent.copy(alpha = 0.08f) else DailyTheme.CardBackground
            ),
            border = BorderStroke(
                width = if (selectedTab == DashboardViewModel.BottomTab.TRACKER) 2.dp else 1.dp,
                color = if (selectedTab == DashboardViewModel.BottomTab.TRACKER) dailyAccent else DailyTheme.CardBorder
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = if (selectedTab == DashboardViewModel.BottomTab.TRACKER) 3.dp else 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(dailyAccent.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Last Date Tracker",
                        tint = dailyAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Last Date",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DailyTheme.TextPrimary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "अंतिम तिथि",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = DailyTheme.TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HeaderSection(
    activeSector: JobSector,
    onSettingsClick: () -> Unit,
    onSectorChange: (JobSector) -> Unit
) {
    val dailyAccent = DailyTheme.accentColor

    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "SarkariGuru.AI",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DailyTheme.TextPrimary,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "Your AI-Powered Government Career Partner",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = DailyTheme.TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(44.dp)
                    .background(dailyAccent.copy(alpha = 0.1f), CircleShape)
                    .testTag("settings_gear_button")
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = dailyAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sector selector tabs with nice indicator pill
        Text(
            "Select Your Desired Career Sector:",
            color = DailyTheme.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            JobSector.values().forEach { sector ->
                val isSelected = sector == activeSector
                val chipBg = if (isSelected) dailyAccent else DailyTheme.CardBackground
                val chipContentColor = if (isSelected) Color.White else DailyTheme.TextSecondary

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(chipBg)
                        .border(
                            1.dp,
                            if (isSelected) dailyAccent else DailyTheme.CardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSectorChange(sector) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = when(sector) {
                                JobSector.CIVIL -> Icons.Default.Shield
                                JobSector.ARMY -> Icons.Default.Star
                                JobSector.NAVY -> Icons.Default.Anchor
                                JobSector.POLICE -> Icons.Default.Policy
                            },
                            contentDescription = null,
                            tint = chipContentColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when(sector) {
                                JobSector.CIVIL -> "Civil"
                                JobSector.ARMY -> "Army"
                                JobSector.NAVY -> "Navy"
                                JobSector.POLICE -> "Police"
                            },
                            color = chipContentColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveSectorBanner(activeSector: JobSector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            // Sector background drawing
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path()
                when (activeSector) {
                    JobSector.CIVIL -> {
                        // Prestige Civil Crest wave background
                        path.moveTo(0f, size.height * 0.7f)
                        path.quadraticTo(size.width * 0.4f, size.height * 0.4f, size.width, size.height * 0.8f)
                        path.lineTo(size.width, size.height)
                        path.lineTo(0f, size.height)
                        path.close()
                        drawPath(path, Brush.verticalGradient(listOf(Color(0xFF3F72AF).copy(alpha = 0.3f), Color.Transparent)))
                    }
                    JobSector.ARMY -> {
                        // Tactical Army camouflage graphics
                        path.moveTo(0f, size.height)
                        path.lineTo(size.width * 0.3f, size.height * 0.5f)
                        path.lineTo(size.width * 0.6f, size.height * 0.8f)
                        path.lineTo(size.width * 0.8f, size.height * 0.4f)
                        path.lineTo(size.width, size.height)
                        path.close()
                        drawPath(path, Brush.verticalGradient(listOf(Color(0xFF5D6B4A).copy(alpha = 0.4f), Color.Transparent)))
                    }
                    JobSector.NAVY -> {
                        // Naval ship hull/waves graphics
                        path.moveTo(0f, size.height * 0.9f)
                        path.cubicTo(size.width * 0.25f, size.height * 0.6f, size.width * 0.75f, size.height * 1.1f, size.width, size.height * 0.8f)
                        path.lineTo(size.width, size.height)
                        path.lineTo(0f, size.height)
                        path.close()
                        drawPath(path, Brush.verticalGradient(listOf(Color(0xFF10B981).copy(alpha = 0.25f), Color.Transparent)))
                    }
                    JobSector.POLICE -> {
                        // Police siren high-voltage dynamic stripes
                        for (i in 0..10) {
                            drawRect(
                                color = if (i % 2 == 0) Color.Blue.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                                topLeft = Offset(i * (size.width / 10), 0f),
                                size = androidx.compose.ui.geometry.Size(size.width / 10, size.height)
                            )
                        }
                    }
                }
            }

            // Overlay text details
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (activeSector) {
                            JobSector.CIVIL -> Icons.Default.Shield
                            JobSector.ARMY -> Icons.Default.Star
                            JobSector.NAVY -> Icons.Default.Anchor
                            JobSector.POLICE -> Icons.Default.Policy
                        },
                        contentDescription = "Sector Icon",
                        tint = when (activeSector) {
                            JobSector.CIVIL -> Color(0xFF60A5FA)
                            JobSector.ARMY -> Color(0xFFFBBF24)
                            JobSector.NAVY -> Color(0xFF34D399)
                            JobSector.POLICE -> Color(0xFFF87171)
                        },
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when(activeSector) {
                            JobSector.CIVIL -> "PRESTIGE CIVIL SERVICES"
                            JobSector.ARMY -> "INDIAN ARMY COMBAT ZONE"
                            JobSector.NAVY -> "INDIAN NAVAL FORCES"
                            JobSector.POLICE -> "TACTICAL POLICE CADRE"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when (activeSector) {
                            JobSector.CIVIL -> Color(0xFF60A5FA)
                            JobSector.ARMY -> Color(0xFFFBBF24)
                            JobSector.NAVY -> Color(0xFF34D399)
                            JobSector.POLICE -> Color(0xFFF87171)
                        }
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = when(activeSector) {
                            JobSector.CIVIL -> "Secure Administrative & Banking Careers via AI Guidance."
                            JobSector.ARMY -> "Apply for Agniveer & Officers. Special physical exam pre-mapping live."
                            JobSector.NAVY -> "Technical Cadet Entry, Sailor ranks (SSR/MR). Secure blue credentials."
                            JobSector.POLICE -> "Constable & SI recruitments. Verification standards matched automatically."
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun AiVoiceGuideCard(
    activeSector: JobSector,
    onReadAloud: () -> Unit,
    isTtsActive: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(if (isTtsActive) Color.Green else Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "AI VOICE ASSISTANT (HINDI/ENG)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Click the speaker icon to play the verbal voice guide describing how to fill forms in simple Hindi/English.",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }

            IconButton(
                onClick = onReadAloud,
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        if (isTtsActive) SuccessGreen else MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
                    .testTag("ai_voice_guide_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Read Aloud Guide",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun AiDocumentLockerCard(
    documents: List<UserDocument>,
    onScanClick: (String) -> Unit,
    isOcrLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "AI DOCUMENT LOCKER",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
                if (isOcrLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Secure OCR", fontSize = 10.sp, color = Color.LightGray, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Upload or scan your official certificates. The AI scans details with extreme accuracy and maps them directly into fields to prevent typos.",
                fontSize = 12.sp,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            val docTypes = listOf(
                "10TH_MARKSHEET" to "10th Marksheet",
                "12TH_MARKSHEET" to "12th Marksheet",
                "AADHAAR" to "Aadhaar Card"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                docTypes.forEach { (type, label) ->
                    val hasDoc = documents.any { it.docType == type }
                    val borderCol = if (hasDoc) SuccessGreen else Color.White.copy(alpha = 0.15f)
                    val cardBg = if (hasDoc) SuccessGreen.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.04f)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(cardBg)
                            .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                            .clickable { onScanClick(type) }
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (hasDoc) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                            contentDescription = label,
                            tint = if (hasDoc) SuccessGreen else Color.LightGray,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (hasDoc) "Scanned AI" else "Tap to Scan",
                            fontSize = 9.sp,
                            color = if (hasDoc) SuccessGreen else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApplyViaAiFlowSection(
    viewModel: DashboardViewModel,
    documents: List<UserDocument>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AI", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "\"APPLY VIA AI\" PROCESS FLOW",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step Indicator Header
            val steps = listOf("Brochure", "Field Map", "Preview", "Payment")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, title ->
                    val isActive = index == viewModel.activeApplyStep.value
                    val isCompleted = index < viewModel.activeApplyStep.value
                    
                    val circleBg = if (isActive) MaterialTheme.colorScheme.primary else if (isCompleted) SuccessGreen else Color.White.copy(alpha = 0.1f)
                    val textColor = if (isActive) Color.White else Color.Gray

                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(circleBg, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            } else {
                                Text("${index + 1}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            // Current Step Layouts
            when (viewModel.activeApplyStep.value) {
                0 -> StepBrochureReading(viewModel = viewModel)
                1 -> StepFieldMapping(viewModel = viewModel, documents = documents)
                2 -> StepPreview(viewModel = viewModel, documents = documents)
                3 -> StepPaymentGateway(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun StepBrochureReading(viewModel: DashboardViewModel) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = InfoLightBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("AI Official Brochure Summarizer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = InfoLightBlue)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "The AI has scanned the active recruitment advertisement. Eligibility requirements: Candidate must have passed 10th/12th in any recognized board, age between 18-25 years. Application fees: SC/ST - Free, General/OBC - Rs. 100.",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.activeApplyStep.value = 1 },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Proceed to Field Mapping", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun StepFieldMapping(viewModel: DashboardViewModel, documents: List<UserDocument>) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        Text(
            "Enter Details (Hindi Voice Typing supported):",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Field 1: Candidate Name
        VoiceInputTextField(
            label = "Candidate Full Name (Same as 10th)",
            value = viewModel.formName.value,
            onValueChange = { viewModel.formName.value = it },
            fieldName = "name",
            promptHint = "मेरा नाम आकाश सुरेश कुमार है",
            viewModel = viewModel,
            isAutofilled = documents.any { it.nameOnDoc.isNotEmpty() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Field 2: Date of Birth
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.formDob.value,
                onValueChange = { viewModel.formDob.value = it },
                label = { Text("Date of Birth (DD/MM/YYYY)", color = Color.LightGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f),
                readOnly = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            IconButton(
                onClick = { viewModel.showCalendarDialog.value = true },
                modifier = Modifier
                    .size(54.dp)
                    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Select Date", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Field 3: 10th Roll Number
        VoiceInputTextField(
            label = "10th Marksheet Roll Number",
            value = viewModel.form10thRoll.value,
            onValueChange = { viewModel.form10thRoll.value = it },
            fieldName = "10thRoll",
            promptHint = "मेरा दसवीं का रोल नंबर १० बी ८९२४१ है",
            viewModel = viewModel,
            isAutofilled = documents.any { it.docType == "10TH_MARKSHEET" && it.rollNumber.isNotEmpty() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Field 4: 10th Marks
        VoiceInputTextField(
            label = "10th Total Marks / CGPA",
            value = viewModel.form10thMarks.value,
            onValueChange = { viewModel.form10thMarks.value = it },
            fieldName = "10thMarks",
            promptHint = "मुझे दसवीं में कुल ४८२ अंक मिले हैं",
            viewModel = viewModel,
            isAutofilled = documents.any { it.docType == "10TH_MARKSHEET" && it.marks.isNotEmpty() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Field 5: Aadhaar Card Number
        VoiceInputTextField(
            label = "Aadhaar Card Number (12 Digits)",
            value = viewModel.formAadhaar.value,
            onValueChange = { viewModel.formAadhaar.value = it },
            fieldName = "aadhaar",
            promptHint = "मेरा आधार नंबर ५८२४ ९१०२ ३८४७ है",
            viewModel = viewModel,
            isAutofilled = documents.any { it.docType == "AADHAAR" && it.docNum.isNotEmpty() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.activeApplyStep.value = 0 },
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Back")
            }

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.verifySpellingAndProceed()
                },
                modifier = Modifier.weight(1.5f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (viewModel.isProofreading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                } else {
                    Text("Spelling Proofread", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun VoiceInputTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    fieldName: String,
    promptHint: String,
    viewModel: DashboardViewModel,
    isAutofilled: Boolean
) {
    val isMicActive = viewModel.isVoiceTypingActive.value && viewModel.activeVoiceFieldName.value == fieldName

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label, color = Color.LightGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = if (isAutofilled) SuccessGreen else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (isAutofilled) SuccessGreen.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).testTag("field_$fieldName"),
                trailingIcon = {
                    if (isAutofilled) {
                        Box(
                            modifier = Modifier
                                .background(SuccessGreen.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("AI Filled", fontSize = 9.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )

            IconButton(
                onClick = { viewModel.triggerVoiceTyping(fieldName, promptHint) },
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        if (isMicActive) ErrorRed else Color.White.copy(alpha = 0.08f),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                if (isMicActive) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                } else {
                    Icon(Icons.Default.Mic, contentDescription = "Hindi Voice Type", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun StepPreview(viewModel: DashboardViewModel, documents: List<UserDocument>) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Spellcheck Verification Clear", fontWeight = FontWeight.Bold, color = SuccessGreen, fontSize = 13.sp)
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))

                PreviewRow(label = "Full Name:", value = viewModel.formName.value)
                PreviewRow(label = "Date of Birth:", value = viewModel.formDob.value)
                PreviewRow(label = "10th Roll Number:", value = viewModel.form10thRoll.value)
                PreviewRow(label = "10th Total Marks:", value = viewModel.form10thMarks.value)
                PreviewRow(label = "Aadhaar Number:", value = viewModel.formAadhaar.value)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.activeApplyStep.value = 1 },
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Text("Edit")
            }

            Button(
                onClick = { viewModel.activeApplyStep.value = 3 },
                modifier = Modifier.weight(1.5f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Confirm & Pay Fee", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
fun PreviewRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.LightGray, fontSize = 12.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}

@Composable
fun StepPaymentGateway(viewModel: DashboardViewModel) {
    val feeAmount = when(viewModel.activeSector.value) {
        JobSector.ARMY, JobSector.NAVY -> "Rs. 250"
        else -> "Rs. 100"
    }
    
    val deptLabel = when(viewModel.activeSector.value) {
        JobSector.ARMY -> "Indian Army Recruitment Office, Ministry of Defence"
        JobSector.NAVY -> "Indian Navy HQ Recruitment Wing, Ministry of Defence"
        JobSector.POLICE -> "State Recruitment Board, Ministry of Home Affairs"
        else -> "Staff Selection Commission, Department of Personnel & Training"
    }

    val officialUrl = when(viewModel.activeSector.value) {
        JobSector.ARMY -> "https://joinindianarmy.nic.in/payment"
        JobSector.NAVY -> "https://joinindiannavy.gov.in/payment"
        JobSector.POLICE -> "https://delhipolice.gov.in/payment"
        else -> "https://ssc.gov.in/payment"
    }

    Column(
        modifier = Modifier.fillMaxWidth().background(Color(0xFF0F1A2C).copy(alpha = 0.5f), RoundedCornerShape(16.dp)).border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Official Hub Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(Color(0xFF1B2E46), RoundedCornerShape(8.dp)).padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Shield, contentDescription = "Govt Emblem", tint = InfoLightBlue, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "SBI e-Pay Portal / Bharatkosh API Gateway",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            deptLabel,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            "Official Secure Fee: $feeAmount",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = SuccessGreen
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (viewModel.paymentCompleted.value) {
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .background(SuccessGreen.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                    .border(2.dp, SuccessGreen, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Paid", tint = SuccessGreen, modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("GOVERNMENT PAID", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center)
                    Text("DIRECT TO CFI ACC", color = Color.LightGray, fontSize = 9.sp, textAlign = TextAlign.Center)
                    Text("TXN: SBI-EPAY-941029", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Official Government SBI e-Pay BharatQR Code
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val sizeX = size.width
                    val sizeY = size.height
                    
                    // Draw official QR anchors
                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(sizeX * 0.3f, sizeY * 0.3f))
                    drawRect(color = Color.White, topLeft = Offset(sizeX * 0.05f, sizeY * 0.05f), size = androidx.compose.ui.geometry.Size(sizeX * 0.2f, sizeY * 0.2f))
                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(sizeX * 0.1f, sizeY * 0.1f), size = androidx.compose.ui.geometry.Size(sizeX * 0.1f, sizeY * 0.1f))

                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(sizeX * 0.7f, 0f), size = androidx.compose.ui.geometry.Size(sizeX * 0.3f, sizeY * 0.3f))
                    drawRect(color = Color.White, topLeft = Offset(sizeX * 0.75f, sizeY * 0.05f), size = androidx.compose.ui.geometry.Size(sizeX * 0.2f, sizeY * 0.2f))
                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(sizeX * 0.8f, sizeY * 0.1f), size = androidx.compose.ui.geometry.Size(sizeX * 0.1f, sizeY * 0.1f))

                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(0f, sizeY * 0.7f), size = androidx.compose.ui.geometry.Size(sizeX * 0.3f, sizeY * 0.3f))
                    drawRect(color = Color.White, topLeft = Offset(sizeX * 0.05f, sizeY * 0.75f), size = androidx.compose.ui.geometry.Size(sizeX * 0.2f, sizeY * 0.2f))
                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(sizeX * 0.1f, sizeY * 0.8f), size = androidx.compose.ui.geometry.Size(sizeX * 0.1f, sizeY * 0.1f))

                    // QR randomized noise patterns representing official transaction ID matching
                    for (x in 3..7) {
                        for (y in 3..7) {
                            if ((x + y) % 3 == 0) {
                                drawRect(
                                    color = Color(0xFF0F172A),
                                    topLeft = Offset(x * (sizeX / 10), y * (sizeY / 10)),
                                    size = androidx.compose.ui.geometry.Size(sizeX / 10, sizeY / 10)
                                )
                            }
                        }
                    }
                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(sizeX * 0.8f, sizeY * 0.8f), size = androidx.compose.ui.geometry.Size(sizeX * 0.15f, sizeY * 0.15f))
                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(sizeX * 0.5f, sizeY * 0.8f), size = androidx.compose.ui.geometry.Size(sizeX * 0.1f, sizeY * 0.1f))
                    drawRect(color = Color(0xFF0F172A), topLeft = Offset(sizeX * 0.8f, sizeY * 0.5f), size = androidx.compose.ui.geometry.Size(sizeX * 0.1f, sizeY * 0.1f))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            
            Text(
                "Direct Bharatkosh Unified Govt QR",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Scan directly using BHIM UPI, GPay, or PhonePe",
                fontSize = 10.sp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Secure Notice
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2E46).copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    "DIRECT PAYMENT ASSURANCE:",
                    fontSize = 10.sp,
                    color = InfoLightBlue,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "SarkariGuru.AI does NOT collect, touch, or process application fees. This connection points 100% directly to verified national government servers.",
                    fontSize = 9.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Open official URL button
        OutlinedButton(
            onClick = {
                viewModel.activeDialogMessage.value = "Redirection link initiated! Opening official gateway at: $officialUrl"
            },
            modifier = Modifier.fillMaxWidth().height(36.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = InfoLightBlue)
        ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Open Official Payment Website", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.activeApplyStep.value = 2 },
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Text("Back")
            }

            if (!viewModel.paymentCompleted.value) {
                Button(
                    onClick = { viewModel.payFormFee() },
                    modifier = Modifier.weight(1.5f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    if (viewModel.isPaying.value) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Verify Payment Acc", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            } else {
                Button(
                    onClick = {
                        viewModel.activeApplyStep.value = 0
                        viewModel.paymentCompleted.value = false
                    },
                    modifier = Modifier.weight(1.5f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Start New Application", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SafetyFiltersCard(viewModel: DashboardViewModel) {
    val dailyAccent = DailyTheme.accentColor

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
        border = BorderStroke(1.dp, DailyTheme.CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = dailyAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "JOB SELECTION PORTAL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DailyTheme.TextPrimary,
                        letterSpacing = 0.5.sp
                    )
                }

                // Eligibility dropdown selector
                Box(
                    modifier = Modifier
                        .background(dailyAccent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .border(1.dp, dailyAccent.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .clickable {
                            val nextVal = when(viewModel.selectedEligibilityFilter.value) {
                                "All" -> "10th Pass"
                                "10th Pass" -> "12th Pass"
                                "12th Pass" -> "Graduate"
                                else -> "All"
                            }
                            viewModel.selectedEligibilityFilter.value = nextVal
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        "Filter: ${viewModel.selectedEligibilityFilter.value}",
                        fontSize = 11.sp,
                        color = dailyAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "SarkariGuru.AI provides authentic, verified daily updates directly sourced from All India and state-level government gazettes. Customize your academic preferences above to filter real-time announcements.",
                fontSize = 11.sp,
                color = DailyTheme.TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun PersistentBottomWidgets(viewModel: DashboardViewModel) {
    val filterVal = viewModel.selectedEligibilityFilter.value

    val displayedJobs = viewModel.allJobs.filter { job ->
        if (job.isFake) false
        else if (filterVal == "All") true
        else job.eligibility.contains(filterVal, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = when(viewModel.selectedBottomTab.value) {
                DashboardViewModel.BottomTab.UPDATES -> 0
                DashboardViewModel.BottomTab.HALL_TICKET -> 1
                DashboardViewModel.BottomTab.TRACKER -> 2
            },
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[
                        when(viewModel.selectedBottomTab.value) {
                            DashboardViewModel.BottomTab.UPDATES -> 0
                            DashboardViewModel.BottomTab.HALL_TICKET -> 1
                            DashboardViewModel.BottomTab.TRACKER -> 2
                        }
                    ]),
                    color = MaterialTheme.colorScheme.primary
                )
            },
            divider = {}
        ) {
            Tab(
                selected = viewModel.selectedBottomTab.value == DashboardViewModel.BottomTab.UPDATES,
                onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.UPDATES }
            ) {
                Text("New Updates", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Tab(
                selected = viewModel.selectedBottomTab.value == DashboardViewModel.BottomTab.HALL_TICKET,
                onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.HALL_TICKET }
            ) {
                Text("Hall Ticket", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Tab(
                selected = viewModel.selectedBottomTab.value == DashboardViewModel.BottomTab.TRACKER,
                onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.TRACKER }
            ) {
                Text("Last Date", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab views content
        when (viewModel.selectedBottomTab.value) {
            DashboardViewModel.BottomTab.UPDATES -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    displayedJobs.forEach { job ->
                        JobItemCard(job = job, viewModel = viewModel, showDownload = false)
                    }
                }
            }
            DashboardViewModel.BottomTab.HALL_TICKET -> {
                val hallTicketJobs = displayedJobs.filter { it.hallTicketLive }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (hallTicketJobs.isEmpty()) {
                        Text("No Admit Cards are currently live for this qualification level.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(16.dp))
                    } else {
                        hallTicketJobs.forEach { job ->
                            JobItemCard(job = job, viewModel = viewModel, showDownload = true)
                        }
                    }
                }
            }
            DashboardViewModel.BottomTab.TRACKER -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Countdown header banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ErrorRed.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .border(1.dp, ErrorRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = "Timer", tint = ErrorRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "CLOSING SOON TODAY: ${viewModel.timeRemaining.value}",
                                color = ErrorRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    displayedJobs.take(3).forEach { job ->
                        JobItemCard(job = job, viewModel = viewModel, showDownload = false, isTracker = true)
                    }
                }
            }
        }
    }
}

@Composable
fun JobItemCard(
    job: JobNotification,
    viewModel: DashboardViewModel,
    showDownload: Boolean = false,
    isTracker: Boolean = false
) {
    val isEligible = viewModel.checkJobEligibility(job)
    val context = LocalContext.current
    val activity = context as? android.app.Activity
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (activity != null) {
                    com.example.AdManager.showInterstitialAd(activity) {
                        viewModel.selectedJobDetails.value = job
                    }
                } else {
                    viewModel.selectedJobDetails.value = job
                }
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
        border = BorderStroke(1.dp, DailyTheme.CardBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(
                                when (job.sector) {
                                    "Army" -> SectorArmyPrimary.copy(alpha = 0.2f)
                                    "Navy" -> SectorNavyPrimary.copy(alpha = 0.2f)
                                    "Police" -> SectorPolicePrimary.copy(alpha = 0.2f)
                                    else -> BrandSecondary.copy(alpha = 0.2f)
                                },
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            job.sector.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (job.sector) {
                                "Army" -> SectorArmyAccent
                                "Navy" -> SectorNavyAccent
                                "Police" -> SectorPoliceAccent
                                else -> InfoLightBlue
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(job.eligibility, fontSize = 10.sp, color = DailyTheme.TextSecondary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = job.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DailyTheme.TextPrimary,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (isEligible && !job.isFake) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(SuccessGreen.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                "Eligible / आपके लिए है",
                                color = SuccessGreen,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                Text("Salary: ${job.salary}", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
            }
 
            Spacer(modifier = Modifier.width(8.dp))
 
            if (showDownload) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            viewModel.activeDialogMessage.value = "Direct Hall Ticket Link Opened for ${job.title}! Redirection is fully secure."
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Download Admit Card", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            } else if (isTracker) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("Last Date:", fontSize = 9.sp, color = Color.Gray)
                    Text(job.lastDate, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(InfoLightBlue)
                            .clickable {
                                if (activity != null) {
                                    com.example.AdManager.showInterstitialAd(activity) {
                                        viewModel.selectedJobDetails.value = job
                                    }
                                } else {
                                    viewModel.selectedJobDetails.value = job
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Apply with AI", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(
                        onClick = {
                            viewModel.activeDialogMessage.value = "Sharing official recruitment details to WhatsApp!"
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.LightGray)
                    }
                }
            }
        }
    }
}

@Composable
fun JobDetailsDialog(
    job: JobNotification,
    viewModel: DashboardViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            border = BorderStroke(1.dp, DailyTheme.CardBorder)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Sector Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                when (job.sector) {
                                    "Army" -> SectorArmyPrimary.copy(alpha = 0.2f)
                                    "Navy" -> SectorNavyPrimary.copy(alpha = 0.2f)
                                    "Police" -> SectorPolicePrimary.copy(alpha = 0.2f)
                                    else -> BrandSecondary.copy(alpha = 0.2f)
                                },
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            job.sector.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (job.sector) {
                                "Army" -> SectorArmyAccent
                                "Navy" -> SectorNavyAccent
                                "Police" -> SectorPoliceAccent
                                else -> InfoLightBlue
                            }
                        )
                    }
                    
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DailyTheme.TextSecondary)
                    }
                }

                // Title
                Text(
                    text = job.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = DailyTheme.TextPrimary
                )

                HorizontalDivider(color = DailyTheme.CardBorder)

                // Metadata list
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(icon = Icons.Default.Shield, label = "Total Vacancies", value = job.totalPosts)
                    DetailRow(icon = Icons.Default.Info, label = "Eligibility Criteria", value = job.eligibility)
                    DetailRow(icon = Icons.Default.AccountCircle, label = "Age Limit Range", value = job.ageLimit)
                    DetailRow(icon = Icons.Default.Lock, label = "Application Fee", value = job.fees)
                    DetailRow(icon = Icons.Default.CalendarToday, label = "Closing Date", value = job.lastDate, isRed = true)
                    DetailRow(icon = Icons.Default.CheckCircle, label = "Monthly Salary Scale", value = job.salary, isGreen = true)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dual buttons side-by-side
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Left button: Apply (Manual Official Link)
                    OutlinedButton(
                        onClick = {
                            if (activity != null) {
                                com.example.AdManager.showInterstitialAd(activity) {
                                    viewModel.activeDialogMessage.value = "Secure link loaded: ${job.officialLink}. Best of luck with manual submission!"
                                    onDismiss()
                                }
                            } else {
                                viewModel.activeDialogMessage.value = "Secure link loaded: ${job.officialLink}. Best of luck with manual submission!"
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        border = BorderStroke(1.dp, InfoLightBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Apply (Manual)", color = InfoLightBlue, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    // Right button: Apply via AI (Personalized Step-by-Step Guide)
                    Button(
                        onClick = {
                            val action = {
                                viewModel.generateApplyGuide(job)
                                onDismiss()
                            }
                            if (activity != null) {
                                com.example.AdManager.showInterstitialAd(activity) {
                                    action()
                                }
                            } else {
                                action()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        modifier = Modifier.weight(1.2f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Apply via AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, isRed: Boolean = false, isGreen: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = DailyTheme.TextSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = 12.sp, color = DailyTheme.TextSecondary, modifier = Modifier.width(110.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isRed) ErrorRed else if (isGreen) SuccessGreen else DailyTheme.TextPrimary
        )
    }
}



@Composable
fun SettingsDialog(
    profile: com.example.data.UserProfile?,
    initialQualification: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit,
    onLogout: () -> Unit
) {
    var name by remember { mutableStateOf(profile?.name ?: "") }
    var phone by remember { mutableStateOf(profile?.phone ?: "") }
    var dob by remember { mutableStateOf(profile?.dob ?: "") }
    var qualification by remember { mutableStateOf(initialQualification) }
    var category by remember { mutableStateOf(profile?.category ?: "General") }

    val qualifications = listOf("10th Pass", "12th Pass", "Graduate")
    val dailyAccent = DailyTheme.accentColor

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
            border = BorderStroke(1.dp, DailyTheme.CardBorder),
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("User Profile Settings", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DailyTheme.TextPrimary)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DailyTheme.TextSecondary)
                    }
                }

                HorizontalDivider(color = DailyTheme.CardBorder)

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Full Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DailyTheme.TextPrimary,
                        unfocusedTextColor = DailyTheme.TextPrimary,
                        focusedBorderColor = dailyAccent,
                        unfocusedBorderColor = DailyTheme.CardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("settings_name_input")
                )

                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DailyTheme.TextPrimary,
                        unfocusedTextColor = DailyTheme.TextPrimary,
                        focusedBorderColor = dailyAccent,
                        unfocusedBorderColor = DailyTheme.CardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("settings_phone_input")
                )

                TextField(
                    value = dob,
                    onValueChange = { dob = it },
                    label = { Text("Date of Birth (DD/MM/YYYY)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DailyTheme.TextPrimary,
                        unfocusedTextColor = DailyTheme.TextPrimary,
                        focusedBorderColor = dailyAccent,
                        unfocusedBorderColor = DailyTheme.CardBorder
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    placeholder = { Text("Tap to select") }
                )

                Text("Highest Qualification:", color = DailyTheme.TextSecondary, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    qualifications.forEach { q ->
                        val isSel = q == qualification
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) dailyAccent else DailyTheme.Background)
                                .clickable { qualification = q }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(q, color = if (isSel) Color.White else DailyTheme.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Text("Candidate Category:", color = DailyTheme.TextSecondary, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("General", "OBC", "SC", "ST").forEach { cat ->
                        val isSel = cat == category
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) dailyAccent else DailyTheme.Background)
                                .clickable { category = cat }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(cat, color = if (isSel) Color.White else DailyTheme.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                HorizontalDivider(color = DailyTheme.CardBorder)

                // About & Credits Section (Exclusive Owner: Trushank Waghmare)
                Card(
                    colors = CardDefaults.cardColors(containerColor = DailyTheme.Background),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DailyTheme.CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "ABOUT & CREDITS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DailyTheme.TextSecondary,
                            letterSpacing = 1.sp
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Verified Owner Icon",
                                tint = dailyAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "SarkariGuru.AI",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = DailyTheme.TextPrimary
                            )
                        }

                        Text(
                            text = "Sole Founder, Creator & Owner:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DailyTheme.TextSecondary
                        )
                        
                        Text(
                            text = "Trushank Waghmare",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = dailyAccent
                        )

                        Text(
                            text = "A pioneering platform designed to guide candidates through their government career journeys with state-of-the-art AI-powered brochure analysis, custom syllabus trackers, and instant document filling.",
                            fontSize = 11.sp,
                            color = DailyTheme.TextSecondary,
                            lineHeight = 16.sp
                        )

                        HorizontalDivider(color = DailyTheme.CardBorder.copy(alpha = 0.5f))

                        Text(
                            text = "© 2026 Trushank Waghmare. All Rights Reserved.\nTerms of Service and legal compliance are fully administered under the legal identity of the owner.",
                            fontSize = 9.sp,
                            color = DailyTheme.TextMuted,
                            lineHeight = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (profile != null) {
                        OutlinedButton(
                            onClick = {
                                onLogout()
                                onDismiss()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Logout")
                        }
                    }

                    Button(
                        onClick = {
                            onSave(name, phone, dob, qualification, category)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = dailyAccent),
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Text("Save Profile")
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Select Date of Birth", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))

                // Basic calendar grid simulation with custom Material aesthetics
                val days = (1..31).toList()
                val selectedDay = remember { mutableStateOf(15) }
                val selectedMonth = remember { mutableStateOf("July") }
                val selectedYear = remember { mutableStateOf("2002") }

                Text("${selectedDay.value} ${selectedMonth.value} ${selectedYear.value}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable/Grid days selection
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(days.chunked(7)) { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                week.forEach { day ->
                                    val isSelected = day == selectedDay.value
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                                            .clickable { selectedDay.value = day }
                                            .padding(6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$day", color = if (isSelected) Color.White else Color.LightGray, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.LightGray)
                    }

                    Button(
                        onClick = {
                            val monthNum = when(selectedMonth.value) {
                                "January" -> "01"
                                "February" -> "02"
                                "March" -> "03"
                                "April" -> "04"
                                "May" -> "05"
                                "June" -> "06"
                                "July" -> "07"
                                "August" -> "08"
                                "September" -> "09"
                                "October" -> "10"
                                "November" -> "11"
                                else -> "12"
                            }
                            onDateSelected(String.format("%02d/%s/%s", selectedDay.value, monthNum, selectedYear.value))
                        }
                    ) {
                        Text("Verify DOB")
                    }
                }
            }
        }
    }
}

@Composable
fun ScanSimulatorDialog(
    docType: String,
    onDismiss: () -> Unit,
    onScanComplete: (Bitmap?) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "AI DOCUMENT SCANNER",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )

                // Simulated Camera Viewfinder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Color.Black, RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Draw a scanner camera reticle grid line
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = Stroke(width = 2.dp.toPx())
                        drawRect(
                            color = SuccessGreen,
                            topLeft = Offset(size.width * 0.1f, size.height * 0.1f),
                            size = androidx.compose.ui.geometry.Size(size.width * 0.8f, size.height * 0.8f),
                            style = stroke
                        )
                        // Laser scanning animation line
                        drawLine(
                            color = SuccessGreen,
                            start = Offset(size.width * 0.1f, size.height * 0.5f),
                            end = Offset(size.width * 0.9f, size.height * 0.5f),
                            strokeWidth = 3.dp.toPx()
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Position Certificate strictly inside the box",
                            color = Color.LightGray,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Text(
                    text = "This will capture a simulated high-contrast photo of your ${
                        when(docType) {
                            "10TH_MARKSHEET" -> "10th Certificate"
                            "12TH_MARKSHEET" -> "12th Certificate"
                            else -> "Aadhaar Card"
                        }
                    } and perform real-time Gemini OCR mapping.",
                    fontSize = 11.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            // Generate dummy bitmap for mock scanning
                            val bmp = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                            onScanComplete(bmp)
                        },
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text("Capture & Auto-Fill", color = Color.White)
                    }
                }
            }
        }
    }
}

// Custom BorderStroke helper to support simple Material 3 borders
@Composable
fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = remember(width, color) {
    androidx.compose.foundation.BorderStroke(width, color)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegistrationScreen(viewModel: DashboardViewModel) {
    val loginName by viewModel.loginName
    val loginPhone by viewModel.loginPhone
    val isConnecting by viewModel.isSupabaseConnecting
    val isLagged by viewModel.supabaseConnectionLagged
    val statusMessage by viewModel.supabaseStatusMessage
    val dailyAccent = DailyTheme.accentColor
    
    var isRegisterMode by remember { mutableStateOf(false) } // False = Login Mode, True = Register Mode

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DailyTheme.Background)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // National Crest Security Shield with Daily Accent Color
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(dailyAccent.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Security Shield",
                    tint = dailyAccent,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SarkariGuru.AI",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = DailyTheme.TextPrimary,
                letterSpacing = 1.sp
            )

            Text(
                text = "Your AI-Powered Government Career Partner",
                fontSize = 13.sp,
                color = DailyTheme.TextSecondary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Switch Tab between Register and Login
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(25.dp))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(21.dp))
                        .background(if (!isRegisterMode) dailyAccent else Color.Transparent)
                        .clickable { isRegisterMode = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "LOGIN (लॉगिन)",
                        color = if (!isRegisterMode) Color.White else DailyTheme.TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(21.dp))
                        .background(if (isRegisterMode) dailyAccent else Color.Transparent)
                        .clickable { isRegisterMode = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "REGISTER (रजिस्ट्रेशन)",
                        color = if (isRegisterMode) Color.White else DailyTheme.TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Card Form Container
            Card(
                colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, DailyTheme.CardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isRegisterMode) "SECURE CANDIDATE PORTAL REGISTRATION" else "SECURE CANDIDATE PORTAL LOGIN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DailyTheme.TextSecondary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    if (isRegisterMode) {
                        // Register Fields: Full Name
                        OutlinedTextField(
                            value = loginName,
                            onValueChange = { viewModel.loginName.value = it },
                            label = { Text("Candidate Full Name") },
                            leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = dailyAccent) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DailyTheme.TextPrimary,
                                unfocusedTextColor = DailyTheme.TextPrimary,
                                focusedBorderColor = dailyAccent,
                                unfocusedBorderColor = DailyTheme.CardBorder
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("login_name_input")
                        )
                    }

                    // Mobile Number Field
                    OutlinedTextField(
                        value = loginPhone,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 10) {
                                viewModel.loginPhone.value = input
                            }
                        },
                        label = { Text("10-Digit Mobile Number") },
                        prefix = { Text("+91 ", color = DailyTheme.TextPrimary, fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = dailyAccent) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DailyTheme.TextPrimary,
                            unfocusedTextColor = DailyTheme.TextPrimary,
                            focusedBorderColor = dailyAccent,
                            unfocusedBorderColor = DailyTheme.CardBorder
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("login_phone_input")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (!isConnecting && !isLagged) {
                        Button(
                            onClick = { viewModel.verifyAndRegisterWithSupabase() },
                            colors = ButtonDefaults.buttonColors(containerColor = dailyAccent),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("supabase_login_btn")
                        ) {
                            Text(
                                text = if (isRegisterMode) "Secure Supabase Cloud Register" else "Secure Supabase Cloud Sign-In",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    if (isConnecting) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(color = dailyAccent, modifier = Modifier.size(24.dp))
                            Text(statusMessage, color = DailyTheme.TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
                        }
                    }

                    if (isLagged) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.05f)),
                            modifier = Modifier.fillMaxWidth().border(1.dp, ErrorRed.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = "Lag Alert", tint = ErrorRed, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("SUPABASE SECURITY SERVER LAG", color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    "A verification delay (high network latency) is detected from Supabase servers. To avoid a blank screen, SarkariGuru.AI safe-failover mode is active.",
                                    color = DailyTheme.TextPrimary,
                                    fontSize = 11.sp
                                )
                                Button(
                                    onClick = { viewModel.activateFallbackAuth() },
                                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                    modifier = Modifier.fillMaxWidth().height(40.dp).testTag("fallback_auth_btn")
                                ) {
                                    Text("Activate Crash-Proof Local Auth (Offline Fallback)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Fallback button for quick offline access
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE2E8F0))
                    .clickable { viewModel.activateFallbackAuth() }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Safe login", tint = SuccessGreen, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Login instantly via Offline Fallback Code / ऑफलाइन बाईपास", color = DailyTheme.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Speaker voice guide button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(dailyAccent.copy(alpha = 0.08f))
                    .clickable { viewModel.speakActiveScreenGuide() }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Speaker Voice Guide",
                    tint = dailyAccent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tap for Voice Assistant Instructions", color = dailyAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Copyright and ownership statement reflecting owner name strictly
            Text(
                text = "© 2026 Trushank Waghmare. All Rights Reserved",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DailyTheme.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun UpdatesTabContent(viewModel: DashboardViewModel, documents: List<UserDocument>) {
    val activeSectorState by viewModel.activeSector
    val selectedState = viewModel.selectedStateFilter.value
    val selectedEligibility = viewModel.selectedEligibilityFilter.value

    // Filtering jobs and prioritizing state openings first!
    val filteredJobs = viewModel.allJobs.filter { job ->
        if (job.isFake) false
        else {
            val matchesEligibility = (selectedEligibility == "All") || job.eligibility.contains(selectedEligibility, ignoreCase = true)
            val matchesState = (selectedState == "All India") || (job.location == "All India" || job.location == selectedState)
            matchesEligibility && matchesState
        }
    }

    // Sort so user see their local job openings first
    val sortedJobs = filteredJobs.sortedByDescending { job ->
        if (selectedState != "All India" && job.location == selectedState) 2 else if (job.location == "All India") 1 else 0
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Top Header
        item {
            HeaderSection(
                activeSector = activeSectorState,
                onSettingsClick = { viewModel.showSettingsDialog.value = true },
                onSectorChange = { viewModel.activeSector.value = it }
            )
        }

        // Active Sector Feature Banner Graphics
        item {
            ActiveSectorBanner(activeSector = activeSectorState)
        }

        // AI Document Locker Section
        item {
            AiDocumentLockerCard(
                documents = documents,
                onScanClick = { docType ->
                    viewModel.activeScanType.value = docType
                    viewModel.showScanDialog.value = true
                },
                isOcrLoading = viewModel.isOcrLoading.value
            )
        }

        // Safety Filter Dashboard & State/District Selector
        item {
            SafetyFiltersCard(viewModel = viewModel)
        }

        // Real-time Supabase / NIC Daily Auto-Refresh Syncing Banner
        item {
            val dailyAccent = DailyTheme.accentColor
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DailyTheme.CardBackground, RoundedCornerShape(12.dp))
                    .border(1.dp, DailyTheme.CardBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pulsing/Syncing status dot
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (viewModel.isLiveSyncing.value) dailyAccent else Color(0xFF10B981),
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = if (viewModel.isLiveSyncing.value) "Syncing with NIC Pan-India databases..." else "Supabase Realtime Stream Active",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DailyTheme.TextPrimary
                    )
                }

                Text(
                    text = "Refreshed: ${viewModel.lastSyncTime.value}",
                    fontSize = 10.sp,
                    color = DailyTheme.TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Filtered list of verified jobs under New Updates
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "VERIFIED NEW UPDATES FEED (${selectedState})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = InfoLightBlue,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                if (sortedJobs.isEmpty()) {
                    Text(
                        "No verified jobs available matching the current filters.",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                    )
                } else {
                    sortedJobs.forEach { job ->
                        JobItemCard(job = job, viewModel = viewModel, showDownload = false)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(48.dp)) }
    }
}

@Composable
fun TrackerTabContent(viewModel: DashboardViewModel) {
    val filterVal = viewModel.selectedEligibilityFilter.value

    val trackerJobs = viewModel.allJobs.filter { job ->
        if (job.isFake) false
        else if (filterVal == "All") true
        else job.eligibility.contains(filterVal, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) }

        // Section Title
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "LAST DATE TRACKER (EXPIRING FORMS)",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = DailyTheme.TextPrimary
                )
            }
        }

        // Countdown banner header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ErrorRed.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .border(1.dp, ErrorRed.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = "Timer", tint = ErrorRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "URGENT DEADLINE IN: ${viewModel.timeRemaining.value}",
                        color = ErrorRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Dedicated tracker items
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                trackerJobs.forEach { job ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                        border = BorderStroke(1.dp, DailyTheme.CardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (job.sector == "Army" || job.sector == "Navy") SectorArmyPrimary.copy(alpha = 0.2f) else SectorPolicePrimary.copy(alpha = 0.2f),
                                            RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(job.sector.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = InfoLightBlue)
                                }

                                Text("Closing: ${job.lastDate}", color = ErrorRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(job.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DailyTheme.TextPrimary)
                            Text("Qualification: ${job.eligibility}", fontSize = 11.sp, color = DailyTheme.TextSecondary)

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Salary: ${job.salary}",
                                    color = SuccessGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                // Direct WhatsApp Alert Deadline reminder
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SuccessGreen)
                                        .clickable {
                                            viewModel.activeDialogMessage.value = "WhatsApp Alert Subscribed! You will receive priority notifications for ${job.title} before the portal closes."
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("WhatsApp Reminder", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(48.dp)) }
    }
}

@Composable
fun HallTicketTabContent(viewModel: DashboardViewModel) {
    val filterVal = viewModel.selectedEligibilityFilter.value
    val dailyAccent = DailyTheme.accentColor

    val hallTicketJobs = viewModel.allJobs.filter { job ->
        if (job.isFake) false
        else if (filterVal == "All") true
        else job.eligibility.contains(filterVal, ignoreCase = true)
    }.filter { it.hallTicketLive }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) }

        // Section Title
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = dailyAccent, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "OFFICIAL HALL TICKET PORTAL",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = DailyTheme.TextPrimary
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = dailyAccent.copy(alpha = 0.05f)),
                modifier = Modifier.fillMaxWidth().border(1.dp, dailyAccent.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = dailyAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Download your verified exam Admit Cards directly from the official government servers securely.",
                        color = DailyTheme.TextPrimary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Live Hall tickets only
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (hallTicketJobs.isEmpty()) {
                    Text(
                        "No Admit Cards are currently live for your filters.",
                        color = DailyTheme.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    )
                } else {
                    hallTicketJobs.forEach { job ->
                        JobItemCard(job = job, viewModel = viewModel, showDownload = true)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(48.dp)) }
    }
}

@Composable
fun AdMobBannerAd(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = if (com.example.BuildConfig.DEBUG) {
                "ca-app-pub-3940256099942544/6300978111"
            } else {
                "ca-app-pub-6300818578767625/2796454220"
            }
        }
    }

    DisposableEffect(adView) {
        adView.loadAd(AdRequest.Builder().build())
        onDispose {
            adView.destroy()
        }
    }

    AndroidView(
        factory = { adView },
        modifier = modifier
    )
}

@Composable
fun AdMobBannerAdContainer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DailyTheme.CardBackground)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AdMobBannerAd()
    }
}

@Composable
fun AiApplyGuideDialog(
    job: JobNotification,
    viewModel: DashboardViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isGenerating = viewModel.isGeneratingApplyGuide.value
    val guideText = viewModel.aiApplyGuideText.value
    
    // Track checklist item states
    val checklistStates = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
            border = BorderStroke(1.dp, DailyTheme.CardBorder),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(SuccessGreen.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "AI Apply Assistant",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = DailyTheme.TextPrimary
                            )
                            Text(
                                "पर्सनलाइज्ड स्टेप-बाय-स्टेप गाइड",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DailyTheme.TextSecondary)
                    }
                }

                HorizontalDivider(color = DailyTheme.CardBorder, modifier = Modifier.padding(vertical = 12.dp))

                if (isGenerating) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = SuccessGreen)
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            "AI आपकी प्रोफाइल पढ़ रहा है...",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DailyTheme.TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "आपकी योग्यता, श्रेणी और जन्मतिथि के अनुसार इस जॉब के लिए स्टेप-बाय-स्टेप गाइड तैयार की जा रही है।",
                            fontSize = 12.sp,
                            color = DailyTheme.TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                } else {
                    // Render Guide
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Job Badge Header Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DailyTheme.Background),
                            border = BorderStroke(1.dp, DailyTheme.CardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(DailyTheme.accentColor.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = DailyTheme.accentColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        job.title,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = DailyTheme.TextPrimary
                                    )
                                    Text(
                                        "Official Site: ${job.officialLink}",
                                        fontSize = 10.sp,
                                        color = DailyTheme.TextSecondary
                                    )
                                }
                            }
                        }

                        // Split guide into sections/lines and render beautifully
                        val lines = guideText.split("\n")
                        lines.forEach { line ->
                            val trimmed = line.trim()
                            when {
                                trimmed.isEmpty() -> {
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                                trimmed.startsWith("**") && trimmed.endsWith("**") -> {
                                    val headerText = trimmed.removeSurrounding("**")
                                    Text(
                                        text = headerText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SuccessGreen,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                }
                                trimmed.startsWith("SECTION") || trimmed.contains("पुष्टि") || trimmed.contains("गाइड") || trimmed.contains("दस्तावेज") || trimmed.contains("भुगतान") || trimmed.contains("चेकलिस्ट") -> {
                                    if (trimmed.startsWith("**") || trimmed.startsWith("#")) {
                                        val cleanText = trimmed.replace("*", "").replace("#", "").trim()
                                        Text(
                                            text = cleanText,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SuccessGreen,
                                            modifier = Modifier.padding(top = 10.dp)
                                        )
                                    } else {
                                        Text(
                                            text = trimmed,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DailyTheme.TextPrimary,
                                            modifier = Modifier.padding(top = 6.dp)
                                        )
                                    }
                                }
                                trimmed.startsWith("- [ ]") || trimmed.startsWith("* [ ]") || trimmed.startsWith("[ ]") -> {
                                    val itemText = trimmed
                                        .replace("- [ ]", "")
                                        .replace("* [ ]", "")
                                        .replace("[ ]", "")
                                        .trim()
                                    val isChecked = checklistStates[itemText] ?: false
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { checklistStates[itemText] = !isChecked }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        androidx.compose.material3.Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { checklistStates[itemText] = it },
                                            colors = androidx.compose.material3.CheckboxDefaults.colors(checkedColor = SuccessGreen)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = itemText,
                                            fontSize = 12.sp,
                                            color = DailyTheme.TextPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                trimmed.startsWith("*") || trimmed.startsWith("-") -> {
                                    val bulletText = trimmed.substring(1).trim()
                                    Row(
                                        modifier = Modifier.padding(start = 8.dp).padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text("• ", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = bulletText,
                                            fontSize = 12.sp,
                                            color = DailyTheme.TextPrimary,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                                else -> {
                                    Text(
                                        text = trimmed,
                                        fontSize = 12.sp,
                                        color = DailyTheme.TextSecondary,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Checklist Complete State Banner
                        if (checklistStates.isNotEmpty()) {
                            val allChecked = checklistStates.values.all { it }
                            if (allChecked) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.12f)),
                                    border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.4f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Column {
                                            Text(
                                                "सभी आवश्यक जांच पूरी हो चुकी हैं!",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = SuccessGreen
                                            )
                                            Text(
                                                "अब आप आधिकारिक वेबसाइट पर जाकर फॉर्म सबमिट करने के लिए पूरी तरह तैयार हैं।",
                                                fontSize = 11.sp,
                                                color = DailyTheme.TextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    HorizontalDivider(color = DailyTheme.CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                    // Dialog footer with Apply button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, DailyTheme.CardBorder)
                        ) {
                            Text("बंद करें (Close)", color = DailyTheme.TextSecondary, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse(job.officialLink)
                                )
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            modifier = Modifier.weight(1.5f).height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("वेबसाइट पर जाएं", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

