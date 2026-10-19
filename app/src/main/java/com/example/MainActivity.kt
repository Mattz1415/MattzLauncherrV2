package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.BottomNavTab
import com.example.model.SettingsCategory
import com.example.ui.components.AnimatedCyberMatrixBackground
import com.example.ui.components.CompactTopNavigationBar
import com.example.ui.components.FloatingBottomNavigationIsland
import com.example.ui.modals.AccountManagerModal
import com.example.ui.modals.CrashDoctorModal
import com.example.ui.modals.InstanceConfigModal
import com.example.ui.modals.PreLaunchSequenceModal
import com.example.ui.modals.SearchAndLogModal
import com.example.ui.modals.ToolInteractiveModal
import com.example.ui.screens.BoostJvmScreen
import com.example.ui.screens.MainHubScreen
import com.example.ui.screens.ModsHubScreen
import com.example.ui.screens.SettingsHubScreen
import com.example.ui.screens.ToolsCenterScreen
import com.example.ui.screens.VersionsFilesScreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassIslandBg
import com.example.ui.theme.MattzLauncherTheme
import com.example.ui.theme.PureWhite
import com.example.viewmodel.LauncherViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MattzLauncherRoot()
        }
    }
}

@Composable
fun MattzLauncherRoot(
    launcherViewModel: LauncherViewModel = viewModel()
) {
    val uiState by launcherViewModel.uiState.collectAsStateWithLifecycle()

    // Auto-dismiss status banner after 3.5 seconds
    LaunchedEffect(uiState.statusBannerMessage) {
        if (uiState.statusBannerMessage != null) {
            delay(3500L)
            launcherViewModel.clearStatusBanner()
        }
    }

    // BackHandler for secondary screens or open modals
    BackHandler(
        enabled = uiState.activeToolModal != null ||
            uiState.showPreLaunchDialog ||
            uiState.showAccountModal ||
            uiState.showInstanceConfigModal ||
            uiState.showSearchAndLogModal ||
            uiState.showCrashDoctorModal ||
            uiState.selectedBottomTab != BottomNavTab.JELAJAH
    ) {
        when {
            uiState.activeToolModal != null -> launcherViewModel.closeToolModal()
            uiState.showPreLaunchDialog -> launcherViewModel.dismissPreLaunchDialog()
            uiState.showAccountModal -> launcherViewModel.setShowAccountModal(false)
            uiState.showInstanceConfigModal -> launcherViewModel.setShowInstanceConfigModal(false)
            uiState.showSearchAndLogModal -> launcherViewModel.setShowSearchAndLogModal(false)
            uiState.showCrashDoctorModal -> launcherViewModel.setShowCrashDoctorModal(false)
            uiState.selectedBottomTab != BottomNavTab.JELAJAH -> {
                launcherViewModel.selectBottomTab(BottomNavTab.JELAJAH)
            }
        }
    }

    MattzLauncherTheme(accentOption = uiState.accentTheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. ULTRA-DARK OLED GLASS PALETTE & ANIMATED CANVAS BACKGROUND
            AnimatedCyberMatrixBackground()

            // 2. MAIN LANDSCAPE VIEWPORT CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                // COMPACT TOP NAVIGATION BAR (44.dp HEIGHT) WITH MANDATORY SETTINGS BUTTON
                CompactTopNavigationBar(
                    selectedSegment = uiState.selectedTopSegment,
                    onSelectSegment = { launcherViewModel.selectTopSegment(it) },
                    liveFps = uiState.liveFps,
                    livePingMs = uiState.livePingMs,
                    activeAccount = uiState.activeAccount,
                    onMonogramClick = {
                        launcherViewModel.openFullSettingsHub(SettingsCategory.TENTANG)
                    },
                    onSearchLogClick = {
                        launcherViewModel.setShowSearchAndLogModal(true)
                    },
                    onSettingsClick = {
                        launcherViewModel.openFullSettingsHub(SettingsCategory.RENDERER_GRAFIK)
                    },
                    onAccountClick = {
                        launcherViewModel.setShowAccountModal(true)
                    }
                )

                // SCREEN CONTENT AREA (6 SCREENS)
                Box(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = uiState.selectedBottomTab,
                        transitionSpec = {
                            fadeIn(tween(180)) togetherWith fadeOut(tween(140))
                        },
                        label = "main_screen_switcher"
                    ) { currentTab ->
                        when (currentTab) {
                            BottomNavTab.JELAJAH -> MainHubScreen(
                                uiState = uiState,
                                viewModel = launcherViewModel
                            )
                            BottomNavTab.MODS_SHADERS -> ModsHubScreen(
                                uiState = uiState,
                                viewModel = launcherViewModel
                            )
                            BottomNavTab.BOOST_JVM -> BoostJvmScreen(
                                uiState = uiState,
                                viewModel = launcherViewModel
                            )
                            BottomNavTab.PUSAT_ALAT -> ToolsCenterScreen(
                                uiState = uiState,
                                viewModel = launcherViewModel
                            )
                            BottomNavTab.VERSI_FILES -> VersionsFilesScreen(
                                uiState = uiState,
                                viewModel = launcherViewModel
                            )
                            BottomNavTab.PENGATURAN -> SettingsHubScreen(
                                uiState = uiState,
                                viewModel = launcherViewModel
                            )
                        }
                    }
                }
            }

            // 3. FLOATING BOTTOM NAVIGATION ISLAND (46.dp HEIGHT)
            FloatingBottomNavigationIsland(
                selectedTab = uiState.selectedBottomTab,
                onSelectTab = { launcherViewModel.selectBottomTab(it) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = 6.dp)
            )

            // 4. FLOATING STATUS TELEMETRY BANNER (Non-intrusive Glass Capsule)
            AnimatedVisibility(
                visible = uiState.statusBannerMessage != null,
                enter = fadeIn(tween(160)) + slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f)
                ),
                exit = fadeOut(tween(140)) + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 50.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(GlassIslandBg, CircleShape)
                        .border(1.dp, EmeraldGreen.copy(alpha = 0.55f), CircleShape)
                        .clickable { launcherViewModel.clearStatusBanner() }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Status",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = uiState.statusBannerMessage.orEmpty(),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Tutup",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // 5. CUSTOM GLASSMORPHIC MODALS (Zero legacy AlertDialogs)
            PreLaunchSequenceModal(uiState = uiState, viewModel = launcherViewModel)
            AccountManagerModal(uiState = uiState, viewModel = launcherViewModel)
            InstanceConfigModal(uiState = uiState, viewModel = launcherViewModel)
            SearchAndLogModal(uiState = uiState, viewModel = launcherViewModel)
            CrashDoctorModal(uiState = uiState, viewModel = launcherViewModel)
            ToolInteractiveModal(uiState = uiState, viewModel = launcherViewModel)
        }
    }
}
