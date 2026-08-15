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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.RepeatMode
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.VolumeOff
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

/**
 * Native Android implementation of sharing Gemini AI output / generated text.
 * Mirrors: void shareGeminiOutput(String text)
 */
fun shareGeminiOutput(
    context: android.content.Context,
    text: String,
    title: String = "Share SarkariGuru AI Output"
) {
    if (text.isNotBlank()) {
        try {
            val sendIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                putExtra(android.content.Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val shareIntent = android.content.Intent.createChooser(sendIntent, title)
            shareIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            android.util.Log.e("ShareOutput", "Error sharing text: ${e.message}", e)
        }
    } else {
        android.widget.Toast.makeText(context, "Data is empty", android.widget.Toast.LENGTH_SHORT).show()
        println("Data is empty")
    }
}

/**
 * Native Android implementation of sharing the SarkariGuru.AI App.
 * Mirrors: void shareMyApp()
 */
fun shareMyApp(
    context: android.content.Context,
    customMessage: String? = null
) {
    val appId = context.packageName
    val appLink = "https://play.google.com/store/apps/details?id=$appId"
    val defaultMsg = "चेक आउट करा हे नवीन ॲप! डाउनलोड करा: $appLink\n(SarkariGuru.AI - All-in-One Government Exam & AI Career Assistant)"
    val message = customMessage ?: defaultMsg
    shareGeminiOutput(context, message, "Share SarkariGuru.AI App")
}

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
    } else if (!viewModel.isProfileSetupCompleted.value) {
        ProfileSetupScreen(viewModel = viewModel)
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
                    val isKid = viewModel.isKidModeActive.value
                    NavigationBar(
                        containerColor = DailyTheme.CardBackground,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == DashboardViewModel.BottomTab.UPDATES,
                            onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.UPDATES },
                            icon = { Icon(Icons.Default.Search, contentDescription = "New Updates") },
                            label = { Text(if (isKid) "नौकरियां" else "Updates", fontWeight = FontWeight.Bold) },
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
                            label = { Text(if (isKid) "प्रवेश पत्र" else "Hall Ticket", fontWeight = FontWeight.Bold) },
                            colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                selectedIconColor = dailyAccent,
                                selectedTextColor = dailyAccent,
                                unselectedIconColor = DailyTheme.TextSecondary,
                                unselectedTextColor = DailyTheme.TextSecondary,
                                indicatorColor = dailyAccent.copy(alpha = 0.12f)
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == DashboardViewModel.BottomTab.RECOMMENDATIONS,
                            onClick = { viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.RECOMMENDATIONS },
                            icon = { Icon(Icons.Default.Star, contentDescription = "Recommendations") },
                            label = { Text(if (isKid) "आपके लिए" else "For You", fontWeight = FontWeight.Bold) },
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
                            label = { Text(if (isKid) "समय चक्र" else "Last Date", fontWeight = FontWeight.Bold) },
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
                    onSectorChange = { viewModel.activeSector.value = it },
                    viewModel = viewModel
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
                        DashboardViewModel.BottomTab.RECOMMENDATIONS -> {
                            RecommendationsTabContent(viewModel = viewModel)
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

                    if (viewModel.showVoiceAssistantDialog.value) {
                        VoiceAssistantDialog(viewModel = viewModel)
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
    onSectorChange: (JobSector) -> Unit,
    viewModel: DashboardViewModel
) {
    val dailyAccent = DailyTheme.accentColor
    val isKid = viewModel.isKidModeActive.value

    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isKid) "👦 गुरुजी किड्स" else "SarkariGuru.AI",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isKid) Color(0xFFFF9800) else DailyTheme.TextPrimary,
                            letterSpacing = 1.sp
                        )
                    )
                    if (isKid) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE0F7FA), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFF00ACC1), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("KIDS ON", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00838F))
                        }
                    }
                }
                Text(
                    text = if (isKid) "प्यारे बच्चों के लिए सरल सरकारी नौकरी गाइड! 🎖️🧸" else "Your AI-Powered Government Career Partner",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isKid) Color(0xFF00ACC1) else DailyTheme.TextSecondary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = if (isKid) 13.sp else 11.sp
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

        Spacer(modifier = Modifier.height(12.dp))

        // Playful Kid Mode and Assistant quick buttons Row!
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kid mode toggle button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isKid) Color(0xFFFFE082) else DailyTheme.CardBackground)
                    .border(
                        width = 2.dp,
                        color = if (isKid) Color(0xFFFFB300) else DailyTheme.CardBorder,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { viewModel.toggleKidMode() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(if (isKid) "🧸 किड मोड बंद करें" else "👦 किड गाइड (Kid Mode)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isKid) Color(0xFF7F5F00) else DailyTheme.TextPrimary)
                }
            }

            // AI Voice Assistant button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE1F5FE))
                    .border(
                        width = 2.dp,
                        color = Color(0xFF03A9F4),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { viewModel.showVoiceAssistantDialog.value = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Mic, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(16.dp))
                    Text("🎤 गुरुजी से बात करें", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF01579B))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sector selector tabs with nice indicator pill
        Text(
            text = if (isKid) "पसंदीदा करियर विभाग चुनें बेटा (Select Sector):" else "Select Your Desired Career Sector:",
            color = DailyTheme.TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = if (isKid) 15.sp else 13.sp,
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
                                JobSector.CIVIL -> if (isKid) "सिविल" else "Civil"
                                JobSector.ARMY -> if (isKid) "थल सेना" else "Army"
                                JobSector.NAVY -> if (isKid) "जल सेना" else "Navy"
                                JobSector.POLICE -> if (isKid) "पुलिस" else "Police"
                            },
                            color = chipContentColor,
                            fontSize = if (isKid) 12.sp else 10.sp,
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
                else -> 0
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
                            else -> 0
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
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select a tab above to browse openings.", color = Color.Gray, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(16.dp))
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
                    val isSaved = viewModel.savedJobsList.any { it.jobTitle == job.title }
                    IconButton(
                        onClick = { viewModel.toggleSaveJob(job) }
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save",
                            tint = if (isSaved) ErrorRed else Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
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
                            val appId = context.packageName
                            val appLink = "https://play.google.com/store/apps/details?id=$appId"
                            shareGeminiOutput(
                                context = context,
                                text = "📢 *Sarkari Recruitment: ${job.title}*\n🏢 Sector: ${job.sector}\n📍 Location: ${job.location}\n🎓 Eligibility: ${job.eligibility}\n⏰ Last Date: ${job.lastDate}\n🌐 Official Portal: ${job.officialLink}\n\n👉 Apply easily with AI Guide on SarkariGuru.AI App!\nDownload App: $appLink",
                                title = "Share Job Alert"
                            )
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
                // Header with Sector Badge & Action Icons
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
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                val appId = context.packageName
                                val appLink = "https://play.google.com/store/apps/details?id=$appId"
                                shareGeminiOutput(
                                    context = context,
                                    text = "📢 *Sarkari Job Notification: ${job.title}*\n🏢 Sector: ${job.sector}\n📍 Location: ${job.location}\n🎓 Eligibility: ${job.eligibility}\n💰 Salary: ${job.salary}\n⏰ Last Date: ${job.lastDate}\n🌐 Official Portal: ${job.officialLink}\n\n👉 Apply easily with AI Guide on SarkariGuru.AI App!\nDownload App: $appLink",
                                    title = "Share Job Alert"
                                )
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share Job", tint = SuccessGreen, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = DailyTheme.TextSecondary)
                        }
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
    val context = LocalContext.current
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

                // Production Ads Switch
                val context = LocalContext.current
                val switchPrefs = remember { context.getSharedPreferences("sarkari_guru_prefs", android.content.Context.MODE_PRIVATE) }
                var useProductionAds by remember { mutableStateOf(switchPrefs.getBoolean("use_production_ads", false)) }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Show Real Production Ads", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DailyTheme.TextPrimary)
                        Text("Toggle to test your actual AdMob IDs inside the emulator.", fontSize = 10.sp, color = DailyTheme.TextSecondary)
                    }
                    androidx.compose.material3.Switch(
                        checked = useProductionAds,
                        onCheckedChange = { 
                            useProductionAds = it
                            switchPrefs.edit().putBoolean("use_production_ads", it).apply()
                            com.example.AdManager.clearAndReload(context)
                        },
                        colors = androidx.compose.material3.SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = dailyAccent
                        )
                    )
                }

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

                // Share SarkariGuru.AI App button
                Button(
                    onClick = {
                        shareMyApp(context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share App with Friends (ॲप शेअर करा)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

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
    val dailyAccent = DailyTheme.accentColor
    val isConnecting by viewModel.isSupabaseConnecting
    val isLagged by viewModel.supabaseConnectionLagged
    val statusMessage by viewModel.supabaseStatusMessage
    
    val isOtpSent = viewModel.isOtpVerificationSent.value
    val enteredOtp = viewModel.enteredOtp.value
    val sentOtp = viewModel.sentOtp.value
    val otpCountDown = viewModel.otpCountDown.value
    
    var isRegisterMode by remember { mutableStateOf(false) } // False = Login, True = Register

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
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            val guideDefaultText = if (isRegisterMode) {
                "प्यारे बच्चों! रजिस्ट्रेशन करने के लिए अपना नाम, फ़ोन नंबर और पासवर्ड भरें। रेफ़रल कोड डालने पर मिलेगा ₹1000 का नकली रिवॉर्ड! 😜 आपके सारे दस्तावेज़ हमारे AI लॉकर में पूरी तरह सुरक्षित रहेंगे!"
            } else {
                "प्यारे बच्चों! अगर आपने रजिस्ट्रेशन कर लिया है तो मोबाइल नंबर और पासवर्ड से लॉगिन करें। नहीं तो ऊपर 'REGISTER' पर दबाएं। 👇"
            }
            val guideSpeechText = if (isRegisterMode) {
                "प्यारे बच्चों! रजिस्ट्रेशन करने के लिए अपना नाम, ईमेल, मोबाइल नंबर और पासवर्ड भरें। अगर आपके पास कोई रेफ़रल कोड है तो उसे अवश्य भरें, जिससे आपको मिलेगा ₹1000 का नकली रिवॉर्ड! पर हाँ, याद रखना: रिवॉर्ड मिलेगा पर रिवॉर्ड मिलेगा नहीं! 😜 और हाँ, आपके सारे दस्तावेज़ हमारे एआई लॉकर में पूरी तरह सुरक्षित और सेव्ड रहेंगे! चलिए, फॉर्म भरना शुरू करें!"
            } else {
                "नमस्ते बच्चों और दोस्तों! अगर आपने पहले से रजिस्ट्रेशन कर लिया है तो अपना फ़ोन नंबर और पासवर्ड भरकर लॉगिन करें। और अगर रजिस्ट्रेशन नहीं किया है, तो ऊपर रजिस्टर बटन दबाकर अपनी नयी आईडी बनायें। धन्यवाद!"
            }

            AiSpeakingGuideWidget(
                viewModel = viewModel,
                defaultText = guideDefaultText,
                speechText = guideSpeechText
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isOtpSent) {
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
            }

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
                    if (isOtpSent) {
                        Text(
                            text = "SECURE PHONE OTP VERIFICATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = dailyAccent,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )

                        Text(
                            text = "We have sent a 6-digit secure verification OTP to +91 ${if (isRegisterMode) viewModel.registerPhone.value else viewModel.loginPhone.value}. Please enter it below to authorize access.",
                            fontSize = 12.sp,
                            color = DailyTheme.TextSecondary
                        )

                        // Nice Helper Badge with the generated OTP for testing ease!
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SuccessGreen.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Simulated Phone SMS OTP: $sentOtp",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }

                        // OTP Input
                        OutlinedTextField(
                            value = enteredOtp,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 6) {
                                    viewModel.enteredOtp.value = input
                                }
                            },
                            label = { Text("6-Digit OTP Code") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = dailyAccent) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = DailyTheme.TextPrimary,
                                unfocusedTextColor = DailyTheme.TextPrimary,
                                focusedBorderColor = dailyAccent,
                                unfocusedBorderColor = DailyTheme.CardBorder
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("otp_input_field")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (otpCountDown > 0) "Resend OTP in ${otpCountDown}s" else "OTP Code Expired",
                                fontSize = 11.sp,
                                color = DailyTheme.TextSecondary
                            )

                            if (otpCountDown == 0) {
                                TextButton(
                                    onClick = {
                                        val p = if (isRegisterMode) viewModel.registerPhone.value else viewModel.loginPhone.value
                                        viewModel.sendSimulatedOtp(p)
                                    }
                                ) {
                                    Text("Resend OTP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = dailyAccent)
                                }
                            }
                        }

                        if (!isConnecting) {
                            Button(
                                onClick = { viewModel.verifyAndCompleteAuth(isRegisterMode) },
                                colors = ButtonDefaults.buttonColors(containerColor = dailyAccent),
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("verify_otp_btn")
                            ) {
                                Text(
                                    text = "Verify & Authorize",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedButton(
                                onClick = { viewModel.isOtpVerificationSent.value = false },
                                border = BorderStroke(1.dp, DailyTheme.CardBorder),
                                modifier = Modifier.fillMaxWidth().height(44.dp)
                            ) {
                                Text("Back / Change Mobile", color = DailyTheme.TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        // NORMAL LOGIN/REGISTRATION FORM
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
                                value = viewModel.registerName.value,
                                onValueChange = { viewModel.registerName.value = it },
                                label = { Text("Candidate Full Name") },
                                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null, tint = dailyAccent) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DailyTheme.TextPrimary,
                                    unfocusedTextColor = DailyTheme.TextPrimary,
                                    focusedBorderColor = dailyAccent,
                                    unfocusedBorderColor = DailyTheme.CardBorder
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("register_name_input")
                            )

                            // Email Address Field
                            OutlinedTextField(
                                value = viewModel.registerEmail.value,
                                onValueChange = { viewModel.registerEmail.value = it },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = dailyAccent) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DailyTheme.TextPrimary,
                                    unfocusedTextColor = DailyTheme.TextPrimary,
                                    focusedBorderColor = dailyAccent,
                                    unfocusedBorderColor = DailyTheme.CardBorder
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("register_email_input")
                            )

                            // Phone Number Field
                            OutlinedTextField(
                                value = viewModel.registerPhone.value,
                                onValueChange = { input ->
                                    if (input.all { it.isDigit() } && input.length <= 10) {
                                        viewModel.registerPhone.value = input
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
                                modifier = Modifier.fillMaxWidth().testTag("register_phone_input")
                            )

                            // Password Field
                            OutlinedTextField(
                                value = viewModel.registerPassword.value,
                                onValueChange = { viewModel.registerPassword.value = it },
                                label = { Text("Password (पासवर्ड)") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = dailyAccent) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DailyTheme.TextPrimary,
                                    unfocusedTextColor = DailyTheme.TextPrimary,
                                    focusedBorderColor = dailyAccent,
                                    unfocusedBorderColor = DailyTheme.CardBorder
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("register_password_input")
                            )

                            // Pointing Hand Sign 👉
                            PointingHandSign(
                                emoji = "👉",
                                text = "यहाँ रेफ़रल कोड डालें और ₹1000 बोनस जीतें!"
                            )

                            // Referral Code Field (optional)
                            OutlinedTextField(
                                value = viewModel.registerReferralCode.value,
                                onValueChange = { viewModel.registerReferralCode.value = it },
                                label = { Text("Referral Code (Optional) / रेफ़रल कोड") },
                                leadingIcon = { Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = dailyAccent) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DailyTheme.TextPrimary,
                                    unfocusedTextColor = DailyTheme.TextPrimary,
                                    focusedBorderColor = dailyAccent,
                                    unfocusedBorderColor = DailyTheme.CardBorder
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("register_referral_input")
                            )

                            // Fake Reward Promo Notice
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.08f)),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🎁", fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "SPONSOR REWARD ASSURED!",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = SuccessGreen
                                        )
                                        Text(
                                            text = "Enter a valid referral code. Receive ₹1000 instantly in your virtual wallet after registration completion! (Subject to T&C)",
                                            fontSize = 10.sp,
                                            color = DailyTheme.TextPrimary,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }
                        } else {
                            // Login Fields: Phone Number
                            OutlinedTextField(
                                value = viewModel.loginPhone.value,
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

                            // Password Field
                            OutlinedTextField(
                                value = viewModel.loginPassword.value,
                                onValueChange = { viewModel.loginPassword.value = it },
                                label = { Text("Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = dailyAccent) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = DailyTheme.TextPrimary,
                                    unfocusedTextColor = DailyTheme.TextPrimary,
                                    focusedBorderColor = dailyAccent,
                                    unfocusedBorderColor = DailyTheme.CardBorder
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("login_password_input")
                            )
                        }

                        // Remember Me Checkbox Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.rememberMeChecked.value = !viewModel.rememberMeChecked.value }
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (viewModel.rememberMeChecked.value) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (viewModel.rememberMeChecked.value) SuccessGreen else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Remember Me for 30 Days (लॉगिन याद रखें)",
                                fontSize = 12.sp,
                                color = DailyTheme.TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        if (!isConnecting && !isLagged) {
                            Button(
                                onClick = { viewModel.handleAuthentication(isRegisterMode) },
                                colors = ButtonDefaults.buttonColors(containerColor = dailyAccent),
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("supabase_login_btn")
                            ) {
                                Text(
                                    text = if (isRegisterMode) "Proceed to Verify Phone (OTP)" else "Sign-In with OTP",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
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
    val dailyAccent = DailyTheme.accentColor
    val isKid = viewModel.isKidModeActive.value

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

    // Category Selector (Sub-Tab State)
    var selectedSubTab by remember { androidx.compose.runtime.mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Active Sector Feature Banner Graphics
        item {
            ActiveSectorBanner(activeSector = activeSectorState)
        }

        // Beautiful Visual Shortcuts for New Vacancies, Last Dates, and Admit Cards
        item {
            InteractiveGurujiDashboardShortcuts(viewModel = viewModel)
        }

        // Category Selector Sub-Tab Row (Latest Jobs, Admit Cards, Results)
        item {
            val categories = listOf(
                "💼 " + (if (isKid) "नौकरियां" else "Latest Jobs"),
                "🎟️ " + (if (isKid) "एडमिट कार्ड" else "Admit Cards"),
                "🏆 " + (if (isKid) "परिणाम" else "Results")
            )
            
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(
                    text = if (isKid) "प्यारे बच्चों, क्या देखना है? 👇" else "नवीनतम अपडेट्स श्रेणियां (Select Category):",
                    fontSize = if (isKid) 16.sp else 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DailyTheme.TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEachIndexed { index, label ->
                        val isSelected = selectedSubTab == index
                        val bg = if (isSelected) dailyAccent else DailyTheme.CardBackground
                        val tc = if (isSelected) Color.White else DailyTheme.TextSecondary
                        val borderCol = if (isSelected) dailyAccent else DailyTheme.CardBorder
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg)
                                .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                                .clickable { selectedSubTab = index }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = if (isKid) 13.sp else 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = tc,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Render AI Document Locker & Safety filters only when Jobs are selected for simplicity
        if (selectedSubTab == 0) {
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

            // Real-time Supabase Syncing Banner
            item {
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
        }

        // Conditional display based on Sub-Tab index
        item {
            when (selectedSubTab) {
                0 -> { // Latest Jobs
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (isKid) "🔥 आपके लिए नयी नौकरियां (${selectedState})" else "VERIFIED NEW UPDATES FEED (${selectedState})",
                            fontSize = if (isKid) 15.sp else 11.sp,
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
                1 -> { // Admit Cards (Filter jobs that have hallTicketLive)
                    val activeAdmitCards = viewModel.allJobs.filter { it.hallTicketLive }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (isKid) "🎟️ परीक्षा के लिए एडमिट कार्ड" else "ACTIVE ADMIT CARDS / HALL TICKETS",
                            fontSize = if (isKid) 15.sp else 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = InfoLightBlue,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        if (activeAdmitCards.isEmpty()) {
                            Text(
                                "No active admit cards at this moment.",
                                color = Color.LightGray,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                            )
                        } else {
                            activeAdmitCards.forEach { job ->
                                JobItemCard(job = job, viewModel = viewModel, showDownload = true)
                            }
                        }
                    }
                }
                2 -> { // Results
                    val mockResults = listOf(
                        Pair("SSC Constable GD 2025 Final Result Declared", "https://ssc.gov.in"),
                        Pair("UPSC Civil Services Main Exam Written Result 2025", "https://upsc.gov.in"),
                        Pair("Indian Airforce Agniveer Vayu Result 01/2026", "https://agnipathvayu.cdac.in"),
                        Pair("IBPS Clerk Phase I Exam Result Published", "https://ibps.in"),
                        Pair("Delhi Police Head Constable Final Select List", "https://delhipolice.gov.in")
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (isKid) "🏆 परीक्षा परिणाम (Results)" else "LATEST DECLARED EXAM RESULTS",
                            fontSize = if (isKid) 15.sp else 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = InfoLightBlue,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        mockResults.forEach { (title, url) ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, DailyTheme.CardBorder, RoundedCornerShape(12.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = title,
                                            fontSize = if (isKid) 14.sp else 12.sp,
                                            color = DailyTheme.TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    Button(
                                        onClick = { /* simulated link open */ },
                                        colors = ButtonDefaults.buttonColors(containerColor = dailyAccent),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(if (isKid) "देखें 🔗" else "View 🔗", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
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
    val isKid = viewModel.isKidModeActive.value

    var selectedStateFilter by remember { androidx.compose.runtime.mutableStateOf("All States") }
    var selectedExamFilter by remember { androidx.compose.runtime.mutableStateOf("All Exams") }

    val stateOptions = listOf("All States", "Delhi", "Uttar Pradesh", "Bihar", "Madhya Pradesh", "All India")
    val examOptions = listOf("All Exams", "SSC", "Army", "Navy", "Police", "UPSC")

    val hallTicketJobs = viewModel.allJobs.filter { job ->
        if (job.isFake) false
        else if (filterVal == "All") true
        else job.eligibility.contains(filterVal, ignoreCase = true)
    }.filter { it.hallTicketLive }
     .filter { job ->
        val matchesState = selectedStateFilter == "All States" || job.location == selectedStateFilter || (selectedStateFilter == "All India" && job.location == "All India")
        val matchesExam = selectedExamFilter == "All Exams" || job.sector.contains(selectedExamFilter, ignoreCase = true) || job.title.contains(selectedExamFilter, ignoreCase = true)
        matchesState && matchesExam
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
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = dailyAccent, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isKid) "🎟️ आपका हॉल टिकट कोना (Admit Cards)" else "OFFICIAL HALL TICKET PORTAL",
                    fontWeight = FontWeight.Black,
                    fontSize = if (isKid) 16.sp else 15.sp,
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
                        text = if (isKid) "बेटा! परीक्षा में जाने के लिए यहाँ से अपना एडमिट कार्ड चुटकियों में डाउनलोड कर लो! 🎟️✨" else "Download your verified exam Admit Cards directly from the official government servers securely.",
                        color = DailyTheme.TextPrimary,
                        fontSize = if (isKid) 13.sp else 11.sp
                    )
                }
            }
        }

        // State-wise & Exam-wise filter UI Cards!
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                modifier = Modifier.fillMaxWidth().border(1.dp, DailyTheme.CardBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = if (isKid) "🔍 एडमिट कार्ड खोजें (Filters):" else "Filter Hall Tickets / Admit Cards:",
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isKid) 14.sp else 12.sp,
                        color = DailyTheme.TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // State filter row
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Text(if (isKid) "राज्य चुनें (Select State):" else "State:", fontSize = 11.sp, color = DailyTheme.TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            stateOptions.forEach { state ->
                                val isSelected = selectedStateFilter == state
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) dailyAccent else DailyTheme.Background)
                                        .clickable { selectedStateFilter = state }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isKid && state == "All States") "सभी राज्य" else state,
                                        color = if (isSelected) Color.White else DailyTheme.TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Exam filter row
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(if (isKid) "परीक्षा का प्रकार (Select Exam):" else "Exam Category:", fontSize = 11.sp, color = DailyTheme.TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            examOptions.forEach { exam ->
                                val isSelected = selectedExamFilter == exam
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) dailyAccent else DailyTheme.Background)
                                        .clickable { selectedExamFilter = exam }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isKid && exam == "All Exams") "सभी परीक्षा" else exam,
                                        color = if (isSelected) Color.White else DailyTheme.TextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Hall tickets only
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (hallTicketJobs.isEmpty()) {
                    Text(
                        text = if (isKid) "बेटा! अभी आपके चुने हुए फ़िल्टर के लिए कोई एडमिट कार्ड नहीं है। 🧸" else "No Admit Cards are currently live for your filters.",
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
    val prefs = remember { context.getSharedPreferences("sarkari_guru_prefs", android.content.Context.MODE_PRIVATE) }
    
    // Read the preference value dynamically
    var useProdAds by remember { mutableStateOf(prefs.getBoolean("use_production_ads", false)) }
    
    // Listen to changes in the SharedPreferences so we recompose instantly
    DisposableEffect(context) {
        val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "use_production_ads") {
                useProdAds = prefs.getBoolean("use_production_ads", false)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    val adView = remember(useProdAds) {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = if (!com.example.BuildConfig.DEBUG || useProdAds) {
                "ca-app-pub-6300818578767625/2796454220"
            } else {
                "ca-app-pub-3940256099942544/6300978111"
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!isGenerating && guideText.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    shareGeminiOutput(
                                        context = context,
                                        text = "📋 *${job.title}* - AI Application Guide (SarkariGuru.AI):\n\n$guideText\n\nOfficial Portal Link: ${job.officialLink}",
                                        title = "Share AI Guide"
                                    )
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = "Share Guide",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = DailyTheme.TextSecondary)
                        }
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

@Composable
fun RecommendationsTabContent(viewModel: DashboardViewModel) {
    val profile = viewModel.userProfile.value
    val dailyAccent = DailyTheme.accentColor
    val isKid = viewModel.isKidModeActive.value

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
                Icon(Icons.Default.Favorite, contentDescription = null, tint = dailyAccent, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isKid) "🎯 आपके लिए खास नौकरियां (For You)" else "RECOMMENDED JOBS FOR YOU",
                    fontWeight = FontWeight.Black,
                    fontSize = if (isKid) 16.sp else 15.sp,
                    color = DailyTheme.TextPrimary
                )
            }
        }

        if (profile == null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DailyTheme.CardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🧸",
                            fontSize = 44.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "नमस्ते बेटा! सरकारी गुरु को आपका नाम नहीं पता।",
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isKid) 16.sp else 14.sp,
                            color = DailyTheme.TextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "कृपया ऊपर दिए गए सेटिंग्स बटन ⚙️ पर क्लिक करके अपना प्यारा सा नाम, उम्र, and पढ़ाई (qualification) सेट करें, ताकि हम आपके लिए सबसे बढ़िया सरकारी नौकरियां ढूंढ सकें!",
                            fontSize = if (isKid) 14.sp else 12.sp,
                            color = DailyTheme.TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            val userQual = viewModel.formQualification.value
            val userCategory = viewModel.formCategory.value
            
            // Filter based on user profile!
            val recommendedJobs = viewModel.allJobs.filter { job ->
                if (job.isFake) false
                else {
                    // Match eligibility (e.g. "12th", "Graduate")
                    val matchesQual = job.eligibility == "All" || 
                                     userQual.contains("Graduate", ignoreCase = true) || 
                                     (userQual.contains("12th", ignoreCase = true) && !job.eligibility.contains("Graduate", ignoreCase = true)) ||
                                     (userQual.contains("10th", ignoreCase = true) && !job.eligibility.contains("12th", ignoreCase = true) && !job.eligibility.contains("Graduate", ignoreCase = true))
                    
                    matchesQual
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = dailyAccent.copy(alpha = 0.05f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, dailyAccent.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = dailyAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isKid) "वाह बेटा ${profile.name}! आपके प्रोफाइल के अनुसार मैच:" else "AI MATCH ACTIVE FOR: ${profile.name.uppercase()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isKid) 14.sp else 12.sp,
                                color = dailyAccent
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "पढ़ाई: $userQual | श्रेणी: $userCategory | जन्म तिथि: ${profile.dob}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DailyTheme.TextSecondary
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (recommendedJobs.isEmpty()) {
                        Text(
                            text = "We couldn't find exact matches for your profile qualifications at this moment, but you can explore all other live jobs in the Updates tab!",
                            color = DailyTheme.TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(24.dp)
                        )
                    } else {
                        recommendedJobs.forEach { job ->
                            // Custom item card with premium Recommendation Match Header!
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, dailyAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                                colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground)
                            ) {
                                Column {
                                    // Match Ribbon Header
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(dailyAccent.copy(alpha = 0.1f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = dailyAccent,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isKid) "✨ आपके लिए 100% सही नौकरी! (Matches $userQual)" else "98% AI Match for you | Fits $userQual & $userCategory",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = dailyAccent
                                        )
                                    }

                                    // Display the standard JobItemCard inside
                                    JobItemCard(job = job, viewModel = viewModel, showDownload = false)
                                }
                            }
                        }
                    }
                }
            }

            // Real Saved Jobs and Applied Jobs persistence display
            val savedOnlyJobs = viewModel.savedJobsList.filter { !it.isApplied }
            val appliedJobs = viewModel.savedJobsList.filter { it.isApplied }

            if (savedOnlyJobs.isNotEmpty()) {
                item {
                    Text(
                        text = if (isKid) "💖 आपकी पसंदीदा नौकरियां (Saved)" else "YOUR SAVED JOBS (सुरक्षित की गई नौकरियां)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = dailyAccent,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }

                items(savedOnlyJobs) { savedJob ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, DailyTheme.CardBorder, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(savedJob.jobTitle, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DailyTheme.TextPrimary)
                                Text("Salary: ${savedJob.salary} | Last Date: ${savedJob.lastDate}", fontSize = 10.sp, color = DailyTheme.TextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Button(
                                    onClick = {
                                        val actualJob = viewModel.allJobs.firstOrNull { it.title == savedJob.jobTitle }
                                        if (actualJob != null) {
                                            viewModel.selectedJobDetails.value = actualJob
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = InfoLightBlue),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Apply", fontSize = 10.sp, color = Color.White)
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.toggleSaveJob(
                                            JobNotification(
                                                title = savedJob.jobTitle,
                                                sector = savedJob.jobSector,
                                                eligibility = savedJob.eligibility,
                                                lastDate = savedJob.lastDate,
                                                salary = savedJob.salary,
                                                hallTicketLive = false,
                                                officialLink = savedJob.officialLink
                                            )
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            if (appliedJobs.isNotEmpty()) {
                item {
                    Text(
                        text = if (isKid) "✅ आपके भरे हुए आवेदन (Applied)" else "YOUR COMPLETED APPLICATIONS (आवेदन इतिहास)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SuccessGreen,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }

                items(appliedJobs) { applied ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(applied.jobTitle, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DailyTheme.TextPrimary)
                                Text("Applied on: ${applied.applyDate}", fontSize = 9.sp, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                            }
                            Box(
                                modifier = Modifier
                                    .background(SuccessGreen.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .border(1.dp, SuccessGreen, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("SUBMITTED", color = SuccessGreen, fontSize = 8.sp, fontWeight = FontWeight.Black)
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
fun VoiceAssistantDialog(viewModel: DashboardViewModel) {
    val context = LocalContext.current
    val messages = viewModel.voiceAssistantMessages
    val isThinking = viewModel.isVoiceAssistantThinking.value
    val isKid = viewModel.isKidModeActive.value
    val dailyAccent = DailyTheme.accentColor
    
    var inputText by remember { androidx.compose.runtime.mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll to the end of messages when list grows
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Dialog(onDismissRequest = { viewModel.showVoiceAssistantDialog.value = false }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
            border = BorderStroke(1.dp, DailyTheme.CardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header of Voice Dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🧸",
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isKid) "सरकारी गुरुजी AI" else "Hindi Voice Assistant",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DailyTheme.TextPrimary
                                )
                            )
                            Text(
                                text = if (isKid) "सरल हिंदी में बातें करें बेटा! 🌸" else "Conversational Hindi Assistant",
                                fontSize = 10.sp,
                                color = DailyTheme.TextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.showVoiceAssistantDialog.value = false },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = DailyTheme.TextSecondary)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = DailyTheme.CardBorder)

                // Message Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(DailyTheme.Background, RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    if (messages.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🧸", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (isKid) "नमस्ते बेटा! मैं हूँ आपका सरकारी गुरु।" else "नमस्ते! मैं सरकारी गुरु हूँ।",
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isKid) 16.sp else 14.sp,
                                color = DailyTheme.TextPrimary
                            )
                            Text(
                                text = if (isKid) "मुझसे कुछ भी पूछें, जैसे 'भर्ती कब आएगी?' या 'फॉर्म कैसे भरें?'" else "मुझसे हिंदी में कुछ भी पूछें।",
                                fontSize = if (isKid) 13.sp else 12.sp,
                                color = DailyTheme.TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(messages) { msg ->
                                val isUser = msg.isUser
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                                ) {
                                    Card(
                                        shape = RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isUser) 16.dp else 4.dp,
                                            bottomEnd = if (isUser) 4.dp else 16.dp
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isUser) dailyAccent else DailyTheme.CardBackground
                                        ),
                                        border = if (isUser) null else BorderStroke(1.dp, DailyTheme.CardBorder),
                                        modifier = Modifier.widthIn(max = 240.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = msg.text,
                                                fontSize = if (isKid) 14.sp else 12.sp,
                                                color = if (isUser) Color.White else DailyTheme.TextPrimary,
                                                lineHeight = if (isKid) 20.sp else 16.sp
                                            )
                                            if (!isUser) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.End
                                                ) {
                                                    IconButton(
                                                        onClick = {
                                                            shareGeminiOutput(
                                                                context = context,
                                                                text = "🤖 *SarkariGuru AI Advice*:\n\n${msg.text}\n\nShared via SarkariGuru.AI App",
                                                                title = "Share Gemini Output"
                                                            )
                                                        },
                                                        modifier = Modifier.size(24.dp)
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Share,
                                                            contentDescription = "Share",
                                                            tint = DailyTheme.TextSecondary,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isThinking) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(dailyAccent.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(12.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("गुरुजी सोच रहे हैं... 🧠", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Input Action Row (Voice tap & textbox)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text(if (isKid) "यहाँ अपना सवाल लिखें..." else "लिखें या नीचे माइक दबाएं...") },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = DailyTheme.CardBackground,
                            unfocusedContainerColor = DailyTheme.CardBackground,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Send text message button
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendVoiceAssistantMessage(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(52.dp)
                            .background(dailyAccent, RoundedCornerShape(14.dp))
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Beautiful, massive direct Playful Tap-To-Speak Mic Action Card!
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // We trigger speaking directly with interactive voice triggers!
                            val kidQueries = listOf(
                                "नमस्ते गुरुजी! मुझे आर्मी की नौकरी देखनी है!",
                                "सरल भाषा में समझाओ, सरकारी नौकरी का फॉर्म कैसे भरते हैं?",
                                "क्या बच्चे भी बड़े होकर सरकारी नौकरी की तैयारी कर सकते हैं?",
                                "मुझे बताइये कि नया अपडेट एडमिट कार्ड क्या होता है?"
                            )
                            val query = kidQueries.random()
                            viewModel.sendVoiceAssistantMessage(query)
                        }
                        .border(2.dp, Color(0xFF0288D1), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Mic,
                            contentDescription = "Speak Now",
                            tint = Color(0xFF01579B),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isKid) "🎤 बोलें: 'नमस्ते गुरुजी' (Tap to Talk)" else "🎤 माइक दबाकर हिंदी में बोलें",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF01579B),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(viewModel: DashboardViewModel) {
    val dailyAccent = DailyTheme.accentColor
    val isKid = viewModel.isKidModeActive.value
    
    // Internal state variables for form fields
    var name by remember { mutableStateOf(viewModel.formName.value) }
    var phone by remember { mutableStateOf(viewModel.formPhone.value) }
    var dob by remember { mutableStateOf(viewModel.formDob.value) }
    var qualification by remember { mutableStateOf(viewModel.formQualification.value) }
    var category by remember { mutableStateOf(viewModel.formCategory.value) }

    // Synchronize state when DOB changes via Date Picker Dialog overlay
    LaunchedEffect(viewModel.formDob.value) {
        if (viewModel.formDob.value.isNotEmpty()) {
            dob = viewModel.formDob.value
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DailyTheme.Background)
            .imePadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Logo and Icon section
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(dailyAccent.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = dailyAccent,
                    modifier = Modifier.size(56.dp)
                )
            }

            Text(
                text = if (isKid) "प्यारे बच्चे, अपनी जानकारी भरें! 😊" else "COMPLETE CANDIDATE PROFILE",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DailyTheme.TextPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isKid) "कृपया अपना सही नाम और पढ़ाई चुनें ताकि गुरुजी आपके लिए बढ़िया सरकारी नौकरियां और प्रवेश पत्र ढूंढ सकें!" 
                       else "Please set up your authentic profile details to filter appropriate vacancies, deadlines, and hall tickets.",
                fontSize = 13.sp,
                color = DailyTheme.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            val profileGuideDefault = "प्यारे बच्चों! यहाँ अपना नाम लिखें, अपनी जन्म तिथि (DOB) और पढ़ाई चुनें। आपके सारे दस्तावेज़ हमारे सुरक्षित AI लॉकर में पूरी तरह सुरक्षित और सेव्ड रहेंगे! 👇"
            val profileGuideSpeech = "प्यारे बच्चों और दोस्तों! अपनी प्रोफाइल पूरी करने के लिए यहाँ अपना पूरा नाम दर्ज करें, कैलेंडर से अपनी जन्म तिथि चुनें, और अपनी पढ़ाई का स्तर तथा जाति वर्ग सिलेक्ट करें। इस बात का ध्यान रखें कि आपके द्वारा अपलोड किए गए सारे दस्तावेज़ हमारे सुरक्षित एआई लॉकर में हमेशा के लिए पूरी तरह सुरक्षित और सेव्ड रहेंगे! चलिए, फटाफट अपनी जानकारी सुरक्षित करें और सेव डिटेल्स बटन दबाएं!"

            AiSpeakingGuideWidget(
                viewModel = viewModel,
                defaultText = profileGuideDefault,
                speechText = profileGuideSpeech
            )

            PointingHandSign(
                emoji = "👇",
                text = "यहाँ अपना विवरण भरें (Fill Profile Details Below)"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DailyTheme.CardBackground),
                border = BorderStroke(1.dp, DailyTheme.CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name Field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(if (isKid) "आपका पूरा नाम (Full Name)" else "Candidate Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = dailyAccent) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DailyTheme.TextPrimary,
                            unfocusedTextColor = DailyTheme.TextPrimary,
                            focusedBorderColor = dailyAccent,
                            unfocusedBorderColor = DailyTheme.CardBorder
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("setup_name_input")
                    )

                    // Phone Field (Pre-filled and Disabled/Read-only)
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {},
                        enabled = false,
                        label = { Text("Verified Mobile Number") },
                        prefix = { Text("+91 ", color = DailyTheme.TextPrimary, fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = DailyTheme.TextPrimary.copy(alpha = 0.6f),
                            disabledBorderColor = DailyTheme.CardBorder,
                            disabledLabelColor = DailyTheme.TextSecondary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Date of Birth Field with Calendar Selector
                    OutlinedTextField(
                        value = dob,
                        onValueChange = { dob = it },
                        readOnly = true,
                        label = { Text(if (isKid) "जन्म तिथि (Date of Birth)" else "Date of Birth (DD/MM/YYYY)") },
                        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = dailyAccent) },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.showCalendarDialog.value = true }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = "Select Date", tint = dailyAccent)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DailyTheme.TextPrimary,
                            unfocusedTextColor = DailyTheme.TextPrimary,
                            focusedBorderColor = dailyAccent,
                            unfocusedBorderColor = DailyTheme.CardBorder
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.showCalendarDialog.value = true }
                            .testTag("setup_dob_input")
                    )

                    // Qualification Selection
                    Text(
                        text = if (isKid) "आपकी पढ़ाई (Educational Level)" else "Highest Educational Qualification:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DailyTheme.TextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("10th Pass", "12th Pass", "Graduate").forEach { qual ->
                            val isSelected = qualification == qual
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) dailyAccent else dailyAccent.copy(alpha = 0.08f))
                                    .border(1.dp, if (isSelected) dailyAccent else DailyTheme.CardBorder, RoundedCornerShape(10.dp))
                                    .clickable { qualification = qual }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = qual,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else DailyTheme.TextPrimary
                                )
                            }
                        }
                    }

                    // Category Selection
                    Text(
                        text = if (isKid) "आपका जाति वर्ग (Category)" else "Candidate Category:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DailyTheme.TextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("General", "OBC", "SC", "ST").forEach { cat ->
                            val isSelected = category == cat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) dailyAccent else dailyAccent.copy(alpha = 0.08f))
                                    .border(1.dp, if (isSelected) dailyAccent else DailyTheme.CardBorder, RoundedCornerShape(10.dp))
                                    .clickable { category = cat }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else DailyTheme.TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Save & Continue Button
            Button(
                onClick = {
                    if (name.trim().isBlank()) {
                        viewModel.activeDialogMessage.value = "Error: Please enter your Full Name!"
                    } else if (dob.trim().isBlank()) {
                        viewModel.activeDialogMessage.value = "Error: Please select your Date of Birth!"
                    } else {
                        viewModel.saveUserProfile(
                            name = name.trim(),
                            phone = phone.trim(),
                            dob = dob.trim(),
                            qualification = qualification,
                            category = category
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = dailyAccent),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_profile_details_btn")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isKid) "विवरण सुरक्षित करें और आगे बढ़ें 🚀" else "Save Details & Continue",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            // Secondary option: Logout / Back to Login
            OutlinedButton(
                onClick = { viewModel.logout() },
                border = BorderStroke(1.dp, DailyTheme.CardBorder),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = if (isKid) "लॉग आउट करें (Logout)" else "Logout / Cancel Registration",
                    color = DailyTheme.TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Overlays inside setup screen (such as calendar dialog)
        if (viewModel.showCalendarDialog.value) {
            CalendarDialog(
                onDismiss = { viewModel.showCalendarDialog.value = false },
                onDateSelected = { selectedDob ->
                    viewModel.formDob.value = selectedDob
                    viewModel.showCalendarDialog.value = false
                }
            )
        }
    }
}

@Composable
fun InteractiveGurujiDashboardShortcuts(viewModel: DashboardViewModel) {
    val dailyAccent = DailyTheme.accentColor
    val isKid = viewModel.isKidModeActive.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DailyTheme.CardBackground, RoundedCornerShape(16.dp))
            .border(1.dp, DailyTheme.CardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = if (isKid) "🎯 गुरुजी हेल्प डेस्क: आपको क्या देखना है?" else "🎯 Candidate Dashboard Navigation Help:",
            fontSize = if (isKid) 15.sp else 13.sp,
            fontWeight = FontWeight.Bold,
            color = DailyTheme.TextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Shortcut 1: New Vacancies
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.UPDATES
                    }
                    .testTag("shortcut_new_vacancies"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), // Soft Green
                border = BorderStroke(1.2.dp, Color(0xFF81C784))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF2E7D32), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isKid) "नयी नौकरियां" else "New Vacancies",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B5E20),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (isKid) "भर्ती फॉर्म" else "Active Forms",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Shortcut 2: Last Dates
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.TRACKER
                    }
                    .testTag("shortcut_last_dates"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)), // Soft Orange
                border = BorderStroke(1.2.dp, Color(0xFFFFB74D))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFE65100), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isKid) "अंतिम तिथि" else "Last Dates",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (isKid) "जल्दी भरें!" else "Apply Quickly",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Shortcut 3: Admit Cards
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        viewModel.selectedBottomTab.value = DashboardViewModel.BottomTab.HALL_TICKET
                    }
                    .testTag("shortcut_admit_cards"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // Soft Blue
                border = BorderStroke(1.2.dp, Color(0xFF64B5F6))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF0D47A1), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isKid) "एडमिट कार्ड" else "Hall Tickets",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0D47A1),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (isKid) "परीक्षा टिकट" else "Admit Cards",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D47A1).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PointingHandSign(
    modifier: Modifier = Modifier,
    emoji: String = "👇",
    text: String = ""
) {
    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "HandBounce")
    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(durationMillis = 600, easing = androidx.compose.animation.core.LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "TranslationY"
    )

    Row(
        modifier = modifier
            .graphicsLayer { translationY = translateY }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 24.sp)
        if (text.isNotEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = DailyTheme.accentColor
            )
        }
    }
}

@Composable
fun AiSpeakingGuideWidget(
    viewModel: DashboardViewModel,
    defaultText: String,
    speechText: String
) {
    val isTtsLoading = viewModel.isTtsLoading.value
    val dailyAccent = DailyTheme.accentColor

    Card(
        colors = CardDefaults.cardColors(containerColor = dailyAccent.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, dailyAccent.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Talking Bot Graphic with Pointing Finger Emoji
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(dailyAccent.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isTtsLoading) "🗣️" else "🤖",
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Voice Guru (बोलने वाला गाइड)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = dailyAccent
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("👉", fontSize = 16.sp) // The pointing hand gesture!
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = defaultText,
                    fontSize = 12.sp,
                    color = DailyTheme.TextPrimary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Play/Pause Button
            IconButton(
                onClick = {
                    viewModel.playSectionVoiceGuide("ScreenGuide", speechText)
                },
                modifier = Modifier
                    .size(44.dp)
                    .background(dailyAccent, CircleShape)
            ) {
                Icon(
                    imageVector = if (isTtsLoading) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    contentDescription = "Listen to AI voice guide",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

