package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SettingsCategory
import com.example.ui.components.AmberGoldPillButton
import com.example.ui.components.DarkGlassPillButton
import com.example.ui.components.GlassCard
import com.example.ui.components.SolidWhitePillButton
import com.example.ui.components.VirtualMouseCursorCanvas
import com.example.ui.theme.AccentThemeOption
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkInk
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.LocalLauncherAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.LauncherUiState
import com.example.viewmodel.LauncherViewModel
import kotlin.math.roundToInt

@Composable
fun SettingsHubScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val railScroll = rememberScrollState()
    val panelScroll = rememberScrollState()
    val accent = LocalLauncherAccent.current

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // LEFT CATEGORY RAIL (34% width)
        Column(
            modifier = Modifier
                .weight(0.34f)
                .fillMaxHeight()
                .verticalScroll(railScroll)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "PUSAT KONFIGURASI SISTEM",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = accent.primaryColor,
                    fontSize = 10.sp
                )
            )

            SettingsCategory.entries.forEach { category ->
                val isSelected = category == uiState.selectedSettingsCategory
                GlassCard(
                    cornerRadius = 18.dp,
                    borderColor = if (isSelected) PureWhite else Color.White.copy(alpha = 0.09f),
                    onClick = { viewModel.selectSettingsCategory(category) },
                    contentPadding = PaddingValues(horizontal = 11.dp, vertical = 9.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_cat_${category.index}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) PureWhite else Color.White.copy(alpha = 0.07f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = categoryIcon(category),
                                contentDescription = category.title,
                                tint = if (isSelected) DarkInk else accent.primaryColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = category.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (isSelected) PureWhite else TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = category.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 9.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // RIGHT GLASS CONFIGURATION PANEL (66% width)
        Column(
            modifier = Modifier
                .weight(0.66f)
                .fillMaxHeight()
                .verticalScroll(panelScroll)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            when (uiState.selectedSettingsCategory) {
                SettingsCategory.RENDERER_GRAFIK -> RendererGraphicsPanel(uiState, viewModel)
                SettingsCategory.JAVA_MEMORI -> JavaMemoryPanel(uiState, viewModel)
                SettingsCategory.KONTROL_MOUSE -> ControlVirtualMousePanel(uiState, viewModel)
                SettingsCategory.ANTARMUKA_ANIMASI -> InterfaceThemeAnimationPanel(uiState, viewModel)
                SettingsCategory.SISTEM_DIAGNOSTIK -> SystemDiagnosticsPanel(uiState, viewModel)
                SettingsCategory.TENTANG -> AboutMattzLauncherPanel(uiState)
            }
        }
    }
}

@Composable
private fun RendererGraphicsPanel(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val renderers = listOf(
        "MobileGlues (Vulkan 1.3)" to "Pipeline OpenGL 4.0 ke Vulkan 1.3 generasi baru untuk Sodium 0.6+ & Iris Shaders",
        "HolyGL4ES (OpenGL 2.1 Fast)" to "Kompatibilitas tertinggi (libgl4es_114.so) & FPS stabil untuk Snapdragon/Mediatek",
        "LTW (LightWeightTranslation GLES 3.2)" to "Wrapper OpenGL ES 3.2 native hemat daya dengan perbaikan mapping LWJGL 3.3.6",
        "Zink (Mesa Vulkan 1.3 + Turnip)" to "Konversi OpenGL 4.6 ke Vulkan modern untuk modpack berat & shader",
        "VirGL (Virtual OpenGL 4.3)" to "Pipeline grafis terakselerasi untuk tekstur modpack kompleks",
        "Freedreno Turnip (Adreno)" to "Driver Vulkan native latensi rendah khusus GPU Qualcomm Adreno"
    )

    GlassCard(cornerRadius = 24.dp, contentPadding = PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Backend GPU Renderer",
                style = MaterialTheme.typography.headlineMedium.copy(color = PureWhite)
            )
            renderers.forEach { (name, desc) ->
                val active = uiState.gpuRenderer == name
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.04f))
                        .border(1.dp, if (active) CyberCyan else Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                        .clickable { viewModel.setGpuRenderer(name) }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = if (active) CyberCyan else PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 9.5.sp)
                        )
                    }
                    if (active) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Aktif",
                            tint = EmeraldGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }

    GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Skala Resolusi Internal: ${uiState.resolutionScale}%",
                    style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = if (uiState.resolutionScale <= 85) "Mode Boost FPS" else "Native Crisp",
                    style = MaterialTheme.typography.labelMedium.copy(color = EmeraldGreen)
                )
            }
            Slider(
                value = uiState.resolutionScale.toFloat(),
                onValueChange = { viewModel.setResolutionScale(it.roundToInt()) },
                valueRange = 25f..200f,
                colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = CyberCyan)
            )

            SettingSwitchRow(
                title = "Unlock 120Hz / 144Hz High Refresh Rate",
                subtitle = "Nonaktifkan pembatas frame rate sistem Android pada layar gaming",
                checked = uiState.unlock120Hz,
                onCheckedChange = { viewModel.setUnlock120Hz(it) }
            )
            SettingSwitchRow(
                title = "VSync (Sinkronisasi Vertikal)",
                subtitle = "Matikan untuk respons input mouse/sentuh tercepat",
                checked = uiState.vsyncEnabled,
                onCheckedChange = { viewModel.setVsync(it) }
            )
        }
    }
}

@Composable
private fun JavaMemoryPanel(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassCard(cornerRadius = 24.dp, contentPadding = PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Konfigurasi Java Runtime & Alokasi Heap",
                style = MaterialTheme.typography.headlineMedium.copy(color = PureWhite)
            )
            Text(
                text = "Alokasi Aktif: ${uiState.ramAllocationMb} MB • Runtime: ${uiState.selectedJreId} • Worker Threads: ${uiState.cpuThreadsCount}",
                style = MaterialTheme.typography.labelMedium.copy(color = CyberCyan)
            )
            Slider(
                value = uiState.ramAllocationMb.toFloat(),
                onValueChange = { viewModel.updateRamAllocation(it.roundToInt()) },
                valueRange = 1024f..16384f,
                colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = AmberGold)
            )
            SettingSwitchRow(
                title = "Pre-Launch RAM Purge Otomatis",
                subtitle = "Bersihkan memori sistem sebelum mem-boot proses JVM",
                checked = uiState.preLaunchRamPurge,
                onCheckedChange = { viewModel.setPreLaunchRamPurge(it) }
            )
        }
    }
}

@Composable
private fun ControlVirtualMousePanel(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val vm = uiState.virtualMouseConfig
    GlassCard(cornerRadius = 24.dp, contentPadding = PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Kontrol Sentuh, Giroskop & Kursor Virtual Mouse",
                style = MaterialTheme.typography.headlineMedium.copy(color = PureWhite)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF05070C))
                        .border(1.dp, CyberCyan.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                ) {
                    VirtualMouseCursorCanvas(
                        config = vm,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Gaya Vektor Kursor Virtual",
                        style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Precision Spear", "Neon Diamond", "Minimal Cross").forEachIndexed { idx, label ->
                            val selected = vm.cursorStyle == idx
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (selected) PureWhite else Color.White.copy(alpha = 0.07f), CircleShape)
                                    .clickable {
                                        viewModel.updateVirtualMouseConfig(vm.copy(cursorStyle = idx))
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (selected) DarkInk else TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = "Sensitivitas Kecepatan Pointer: ${String.format("%.2f", vm.speedSensitivity)}x",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
            Slider(
                value = vm.speedSensitivity,
                onValueChange = { viewModel.updateVirtualMouseConfig(vm.copy(speedSensitivity = it)) },
                valueRange = 0.5f..3.0f,
                colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = CyberCyan)
            )

            Text(
                text = "Sensitivitas Gyroscope Aiming: ${String.format("%.2f", vm.gyroSensitivity)}x",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
            Slider(
                value = vm.gyroSensitivity,
                onValueChange = { viewModel.updateVirtualMouseConfig(vm.copy(gyroSensitivity = it)) },
                valueRange = 0.1f..2.5f,
                colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = EmeraldGreen)
            )
        }
    }
}

@Composable
private fun InterfaceThemeAnimationPanel(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassCard(cornerRadius = 24.dp, contentPadding = PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Palette,
                    contentDescription = "OLED Accent Color Theme Engine",
                    tint = uiState.accentTheme.primaryColor,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = "Custom OLED Accent Color Theme Engine",
                        style = MaterialTheme.typography.headlineMedium.copy(color = PureWhite)
                    )
                    Text(
                        text = "Tersimpan otomatis di DataStore • Pembaruan warna dinamis real-time",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            // 6 Selectable Theme Accent Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AccentThemeOption.entries.forEach { option ->
                    val isSelected = option == uiState.accentTheme
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) option.primaryColor.copy(alpha = 0.18f)
                                else Color.White.copy(alpha = 0.04f)
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) option.primaryColor else Color.White.copy(alpha = 0.10f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { viewModel.setAccentTheme(option) }
                            .padding(vertical = 10.dp, horizontal = 4.dp)
                            .testTag("theme_accent_${option.id.lowercase()}")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(option.primaryColor, CircleShape)
                                .border(1.dp, PureWhite.copy(alpha = 0.5f), CircleShape)
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = option.label,
                                    tint = DarkInk,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) PureWhite else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            maxLines = 1
                        )
                    }
                }
            }

            SettingSwitchRow(
                title = "Live FPS & Telemetry HUD Overlay",
                subtitle = "Tampilkan penghitung FPS, ping, dan memori di atas layar permainan",
                checked = uiState.fpsHudOverlay,
                onCheckedChange = { viewModel.setFpsHudOverlay(it) }
            )
        }
    }
}

@Composable
private fun SystemDiagnosticsPanel(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassCard(cornerRadius = 24.dp, contentPadding = PaddingValues(14.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Sistem, Logcat & Crash Doctor",
                style = MaterialTheme.typography.headlineMedium.copy(color = PureWhite)
            )
            Text(
                text = uiState.crashDoctorStatus,
                style = MaterialTheme.typography.labelMedium.copy(color = EmeraldGreen)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF05070C))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                uiState.logcatLines.takeLast(6).forEach { line ->
                    Text(
                        text = line,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyberCyan,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SolidWhitePillButton(
                    text = "Jalankan Crash Doctor & Bersihkan Cache",
                    onClick = { viewModel.runCleanCacheAndCrashDoctor() }
                )
                AmberGoldPillButton(
                    text = "Emergency Force-Kill JVM",
                    onClick = { viewModel.emergencyForceKillGame() }
                )
            }
        }
    }
}

@Composable
private fun AboutMattzLauncherPanel(uiState: LauncherUiState) {
    GlassCard(
        cornerRadius = 24.dp,
        borderColor = uiState.accentTheme.primaryColor.copy(alpha = 0.35f),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Verified,
                    contentDescription = "MattzLauncher Verified",
                    tint = uiState.accentTheme.primaryColor,
                    modifier = Modifier.size(26.dp)
                )
                Column {
                    Text(
                        text = "MattzLauncher • Nexus Discovery Edition",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Version 1.0.0 Stable (Build 2026.10 • Package: com.mattz.launcher)",
                        style = MaterialTheme.typography.labelMedium.copy(color = CyberCyan)
                    )
                }
            }

            Text(
                text = "Next-Generation Minecraft: Java Edition Launcher & Performance Command Center dengan arsitektur Dark OLED Glassmorphism, Generational ZGC Tuning, dan 52 Utilitas Performa Terintegrasi.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary.copy(alpha = 0.88f))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AboutMetaBadge(title = "Kreator Utama", value = "Mattz / Mattz1415", color = AmberGold, modifier = Modifier.weight(1f))
                AboutMetaBadge(title = "GitHub Repository", value = "github.com/Mattz1415/MattzLauncher", color = CyberCyan, modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AboutMetaBadge(title = "Komunitas Discord", value = "discord.gg/mattzlauncher", color = EmeraldGreen, modifier = Modifier.weight(1f))
                AboutMetaBadge(title = "Lisensi Open Source", value = "GNU General Public License v3.0 (GPLv3)", color = PureWhite, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun AboutMetaBadge(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        cornerRadius = 14.dp,
        contentPadding = PaddingValues(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PureWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 9.5.sp
                )
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = DarkInk,
                checkedTrackColor = EmeraldGreen,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color.White.copy(alpha = 0.10f)
            )
        )
    }
}

private fun categoryIcon(category: SettingsCategory): ImageVector {
    return when (category) {
        SettingsCategory.RENDERER_GRAFIK -> Icons.Filled.DeveloperBoard
        SettingsCategory.JAVA_MEMORI -> Icons.Filled.Memory
        SettingsCategory.KONTROL_MOUSE -> Icons.Filled.Gamepad
        SettingsCategory.ANTARMUKA_ANIMASI -> Icons.Filled.Animation
        SettingsCategory.SISTEM_DIAGNOSTIK -> Icons.Filled.BugReport
        SettingsCategory.TENTANG -> Icons.Filled.Info
    }
}
