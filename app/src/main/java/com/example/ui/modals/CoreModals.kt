package com.example.ui.modals

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmberGoldPillButton
import com.example.ui.components.DarkGlassPillButton
import com.example.ui.components.DualRingLoadingSpinner
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassModalDialog
import com.example.ui.components.Minecraft2DAvatarHead
import com.example.ui.components.SolidWhitePillButton
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkInk
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.LauncherUiState
import com.example.viewmodel.LauncherViewModel
import kotlin.math.roundToInt

@Composable
fun PreLaunchSequenceModal(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val progressAnim by animateFloatAsState(
        targetValue = uiState.preLaunchProgress,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
        label = "pre_launch_progress"
    )

    GlassModalDialog(
        visible = uiState.showPreLaunchDialog,
        title = if (uiState.isGameRunningSimulated) {
            "Minecraft: Java Edition Sedang Berjalan"
        } else {
            "Pre-Launch Sequence • ${uiState.currentInstance.title}"
        },
        subtitle = "Runtime ${uiState.selectedJreId} • ${uiState.ramAllocationMb} MB Heap • ${uiState.gpuRenderer}",
        onDismiss = { viewModel.dismissPreLaunchDialog() }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DualRingLoadingSpinner(
                    size = 58.dp,
                    primaryColor = if (uiState.isGameRunningSimulated) EmeraldGreen else CyberCyan,
                    secondaryColor = AmberGold
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = uiState.preLaunchStageText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Progress bar track
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressAnim.coerceIn(0.05f, 1f))
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(if (uiState.isGameRunningSimulated) EmeraldGreen else CyberCyan)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "RAM Purge: Aktif (${uiState.ramUsedGb} GB)",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "${(progressAnim * 100).roundToInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            if (uiState.isGameRunningSimulated) {
                GlassCard(
                    cornerRadius = 18.dp,
                    borderColor = EmeraldGreen.copy(alpha = 0.35f),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TELEMETRI IN-GAME LIVE HUD",
                                style = MaterialTheme.typography.labelSmall.copy(color = EmeraldGreen)
                            )
                            Text(
                                text = "${uiState.liveFps} FPS • Frame Time 6.8ms • Ping ${uiState.livePingMs}ms (${uiState.selectedServerHost})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DarkGlassPillButton(
                    text = "Force-Kill JVM (SIGKILL)",
                    leadingIcon = Icons.Filled.PowerSettingsNew,
                    textColor = CrimsonRed,
                    borderColor = CrimsonRed.copy(alpha = 0.35f),
                    onClick = { viewModel.emergencyForceKillGame() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                SolidWhitePillButton(
                    text = "Kembali ke Command Hub",
                    onClick = { viewModel.dismissPreLaunchDialog() }
                )
            }
        }
    }
}

@Composable
fun AccountManagerModal(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    var newUsername by remember { mutableStateOf("") }
    var isMicrosoftMode by remember { mutableStateOf(true) }

    GlassModalDialog(
        visible = uiState.showAccountModal,
        title = "Account Manager • Microsoft OAuth & Offline",
        subtitle = "Kelola sesi autentikasi Xbox Live / Microsoft OAuth2 dan profil Offline lokal",
        onDismiss = { viewModel.setShowAccountModal(false) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.accounts.forEach { acc ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (acc.isActive) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.04f))
                        .border(
                            1.dp,
                            if (acc.isActive) EmeraldGreen else Color.White.copy(alpha = 0.09f),
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { viewModel.selectAccount(acc.id) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Minecraft2DAvatarHead(
                            primaryColor = acc.primarySkinColor,
                            accentColor = acc.accentSkinColor,
                            size = 32.dp
                        )
                        Column {
                            Text(
                                text = acc.username,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${acc.accountType} • UUID: ${acc.uuid.take(18)}...",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    if (acc.isActive) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Aktif",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(10.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isMicrosoftMode) PureWhite else Color.White.copy(alpha = 0.06f), CircleShape)
                                .clickable { isMicrosoftMode = true }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Microsoft OAuth 2.0",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isMicrosoftMode) DarkInk else TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (!isMicrosoftMode) PureWhite else Color.White.copy(alpha = 0.06f), CircleShape)
                                .clickable { isMicrosoftMode = false }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Offline Account",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (!isMicrosoftMode) DarkInk else TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.06f))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            if (newUsername.isEmpty()) {
                                Text(
                                    text = if (isMicrosoftMode) "Gamertag Microsoft..." else "Username Offline...",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                            BasicTextField(
                                value = newUsername,
                                onValueChange = { newUsername = it },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.titleMedium.copy(color = PureWhite),
                                cursorBrush = SolidColor(CyberCyan),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        SolidWhitePillButton(
                            text = "Tambah Akun",
                            leadingIcon = Icons.Filled.Add,
                            onClick = {
                                viewModel.addAccount(newUsername, isMicrosoftMode)
                                newUsername = ""
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InstanceConfigModal(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    var instanceTitle by remember { mutableStateOf("Minecraft 1.21.4 Competitive") }
    var versionBadge by remember { mutableStateOf("1.21.4 Fabric • Sodium + Iris") }
    var selectedLoader by remember { mutableStateOf("Fabric 0.16.9") }
    var customRamMb by remember { mutableIntStateOf(uiState.ramAllocationMb) }
    var selectedJre by remember { mutableStateOf(uiState.selectedJreId) }

    val loaders = listOf("Fabric 0.16.9", "Forge 47.3.0", "NeoForge 21.4", "Quilt 0.27.0", "Vanilla")

    GlassModalDialog(
        visible = uiState.showInstanceConfigModal,
        title = "+ Konfigurasi & Buat Instans Baru",
        subtitle = "Kustomisasi Mod Loader, JRE Runtime, dan alokasi RAM khusus per instans",
        onDismiss = { viewModel.setShowInstanceConfigModal(false) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(10.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Nama Instans:",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                    BasicTextField(
                        value = instanceTitle,
                        onValueChange = { instanceTitle = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleMedium.copy(color = PureWhite),
                        cursorBrush = SolidColor(CyberCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .padding(8.dp)
                    )

                    Text(
                        text = "Pilih Mod Loader:",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        loaders.forEach { loader ->
                            val active = selectedLoader == loader
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (active) PureWhite else Color.White.copy(alpha = 0.06f), CircleShape)
                                    .clickable {
                                        selectedLoader = loader
                                        versionBadge = "1.21.4 ${loader.substringBefore(" ")} • Custom"
                                    }
                                    .padding(horizontal = 9.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = loader,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (active) DarkInk else TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Text(
                        text = "Alokasi RAM Instans: $customRamMb MB",
                        style = MaterialTheme.typography.labelSmall.copy(color = CyberCyan)
                    )
                    Slider(
                        value = customRamMb.toFloat(),
                        onValueChange = { customRamMb = ((it.roundToInt() / 128) * 128) },
                        valueRange = 1024f..16384f,
                        colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = CyberCyan)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DarkGlassPillButton(
                    text = "Batal",
                    onClick = { viewModel.setShowInstanceConfigModal(false) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                SolidWhitePillButton(
                    text = "Simpan & Aktifkan Instans",
                    onClick = {
                        viewModel.addOrUpdateInstance(
                            title = instanceTitle,
                            versionBadge = versionBadge,
                            loaderType = selectedLoader,
                            ramMb = customRamMb,
                            jre = selectedJre
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SearchAndLogModal(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassModalDialog(
        visible = uiState.showSearchAndLogModal,
        title = "Live JVM Logcat & Telemetry Terminal",
        subtitle = "Streaming stdout/stderr proses native LWJGL3, Vulkan Loader, dan Mod Engine",
        onDismiss = { viewModel.setShowSearchAndLogModal(false) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF05070C))
                    .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(14.dp))
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                uiState.logcatLines.forEach { line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (line.contains("WARN")) AmberGold else CyberCyan,
                            fontSize = 9.5.sp
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AmberGoldPillButton(
                    text = "Trigger GC & Dump Heap Info",
                    leadingIcon = Icons.Filled.Terminal,
                    onClick = {
                        viewModel.appendLog("[ManualGC/INFO]: System.gc() invoked; freed 412MB unreachable objects")
                    }
                )
                SolidWhitePillButton(
                    text = "Tutup Terminal",
                    onClick = { viewModel.setShowSearchAndLogModal(false) }
                )
            }
        }
    }
}

@Composable
fun CrashDoctorModal(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassModalDialog(
        visible = uiState.showCrashDoctorModal,
        title = "Pembersih Cache & Crash Log Doctor",
        subtitle = "Inspeksi heuristik kompatibilitas Mixin, konflik .jar, dan alokasi VRAM",
        onDismiss = { viewModel.setShowCrashDoctorModal(false) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GlassCard(
                cornerRadius = 18.dp,
                borderColor = EmeraldGreen.copy(alpha = 0.35f),
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = uiState.crashDoctorStatus,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "• Semua ${uiState.modsList.count { it.isInstalled && it.isEnabled }} mod aktif kompatibel dengan ${uiState.selectedJreId}\n• Pipeline ${uiState.gpuRenderer} bebas error shader compilation\n• Total Cache Dibersihkan Sesi Ini: ${uiState.cleanedCacheMb} MB",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AmberGoldPillButton(
                    text = "Bersihkan Shader & Mixin Cache Sekarang",
                    leadingIcon = Icons.Filled.CleaningServices,
                    onClick = { viewModel.runCleanCacheAndCrashDoctor() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                SolidWhitePillButton(
                    text = "Selesai",
                    onClick = { viewModel.setShowCrashDoctorModal(false) }
                )
            }
        }
    }
}
