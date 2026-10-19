package com.example.ui.modals

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmberGoldPillButton
import com.example.ui.components.CustomCrosshairCanvas
import com.example.ui.components.DarkGlassPillButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassModalDialog
import com.example.ui.components.Minecraft2DAvatarHead
import com.example.ui.components.PingRadarGraphCanvas
import com.example.ui.components.SolidWhitePillButton
import com.example.ui.components.VirtualMouseCursorCanvas
import com.example.ui.theme.AccentThemeOption
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
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
fun ToolInteractiveModal(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val tool = uiState.activeToolModal ?: return

    GlassModalDialog(
        visible = true,
        title = "#${String.format("%02d", tool.id)} • ${tool.title}",
        subtitle = tool.subtitle,
        onDismiss = { viewModel.closeToolModal() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            when (tool.id) {
                // 33: Nether-Overworld Portal Coordinate Calculator (interactive X/Y/Z converter)
                33 -> PortalCoordinateCalculatorTool(uiState, viewModel)

                // 34: Seed & Coordinate Vault (add/save notes)
                34 -> SeedAndCoordinateVaultTool(uiState, viewModel)

                // 38: Skin & Cape Previewer/Downloader by Username
                38 -> SkinAndCapePreviewerTool(uiState, viewModel)

                // 39: Custom Crosshair Overlay Editor (live interactive Canvas crosshair designer)
                39 -> CustomCrosshairDesignerTool(uiState, viewModel)

                // 40: Keybind Macro Creator
                40 -> KeybindMacroCreatorTool(uiState, viewModel)

                // 50: Custom Virtual Mouse Cursor Designer
                50 -> VirtualMouseDesignerTool(uiState, viewModel)

                // 52: Custom OLED Accent Color Theme Engine
                52 -> OledAccentThemeEngineTool(uiState, viewModel)

                // 31, 32, 6: Direct Server IP Quick-Connect / Ping Radar
                6, 31, 32 -> ServerQuickConnectAndPingTool(uiState, viewModel)

                // 8: GPU Renderer Switcher
                8 -> GpuRendererQuickSwitcherTool(uiState, viewModel)

                // Default interactive controls tailored to telemetry, JVM, game options, or world utilities
                else -> UniversalInteractiveHardwareAndGameTool(tool.id, uiState, viewModel)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                SolidWhitePillButton(
                    text = "Terapkan & Tutup",
                    onClick = { viewModel.closeToolModal() }
                )
            }
        }
    }
}

@Composable
private fun PortalCoordinateCalculatorTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Konverter Koordinat Portal Overworld <-> Nether (Rasio 8:1 Real-Time)",
                style = MaterialTheme.typography.titleMedium.copy(color = AmberGold, fontWeight = FontWeight.Bold)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Overworld Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "OVERWORLD (X / Y / Z)",
                        style = MaterialTheme.typography.labelMedium.copy(color = EmeraldGreen, fontWeight = FontWeight.Bold)
                    )
                    CoordInputBox(
                        label = "X:",
                        value = uiState.portalOverworldX,
                        onValueChange = { viewModel.updateOverworldCoords(it, uiState.portalOverworldY, uiState.portalOverworldZ) }
                    )
                    CoordInputBox(
                        label = "Y:",
                        value = uiState.portalOverworldY,
                        onValueChange = { viewModel.updateOverworldCoords(uiState.portalOverworldX, it, uiState.portalOverworldZ) }
                    )
                    CoordInputBox(
                        label = "Z:",
                        value = uiState.portalOverworldZ,
                        onValueChange = { viewModel.updateOverworldCoords(uiState.portalOverworldX, uiState.portalOverworldY, it) }
                    )
                }

                // Nether Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "NETHER DIMENSION (X / Y / Z)",
                        style = MaterialTheme.typography.labelMedium.copy(color = CrimsonRed, fontWeight = FontWeight.Bold)
                    )
                    CoordInputBox(
                        label = "X:",
                        value = uiState.portalNetherX,
                        onValueChange = { viewModel.updateNetherCoords(it, uiState.portalNetherY, uiState.portalNetherZ) }
                    )
                    CoordInputBox(
                        label = "Y:",
                        value = uiState.portalNetherY,
                        onValueChange = { viewModel.updateNetherCoords(uiState.portalNetherX, it, uiState.portalNetherZ) }
                    )
                    CoordInputBox(
                        label = "Z:",
                        value = uiState.portalNetherZ,
                        onValueChange = { viewModel.updateNetherCoords(uiState.portalNetherX, uiState.portalNetherY, it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CoordInputBox(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary, fontWeight = FontWeight.Bold)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold),
            cursorBrush = SolidColor(CyberCyan),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SeedAndCoordinateVaultTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    var titleInput by remember { mutableStateOf("Bastion Remnant Treasure") }
    var seedInput by remember { mutableStateOf("-418920491820411") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = titleInput,
                onValueChange = { titleInput = it },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = PureWhite),
                cursorBrush = SolidColor(CyberCyan),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .padding(8.dp)
            )
            AmberGoldPillButton(
                text = "+ Simpan Waypoint",
                onClick = {
                    viewModel.addSeedVaultEntry(titleInput, seedInput, "Overworld", 320, 64, -180)
                }
            )
        }

        uiState.seedVaultEntries.forEach { entry ->
            GlassCard(cornerRadius = 14.dp, contentPadding = PaddingValues(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${entry.title} (${entry.dimension})",
                            style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Seed: ${entry.seedCode}",
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberCyan)
                        )
                    }
                    Text(
                        text = "XYZ: ${entry.x}, ${entry.y}, ${entry.z}",
                        style = MaterialTheme.typography.labelMedium.copy(color = EmeraldGreen, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun SkinAndCapePreviewerTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Minecraft2DAvatarHead(
                primaryColor = uiState.accentTheme.primaryColor,
                accentColor = EmeraldGreen,
                size = 56.dp
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Username Target Skin & Cape:",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
                BasicTextField(
                    value = uiState.skinPreviewUsername,
                    onValueChange = { viewModel.updateSkinPreviewUsername(it) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold),
                    cursorBrush = SolidColor(CyberCyan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .padding(8.dp)
                )
                Text(
                    text = "Tekstur 64x64 Slim (Alex/Steve) + Mattz Nexus Cape Siap Diunduh",
                    style = MaterialTheme.typography.labelSmall.copy(color = EmeraldGreen)
                )
            }
        }
    }
}

@Composable
private fun CustomCrosshairDesignerTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val cfg = uiState.crosshairConfig
    GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF05070C))
                    .border(1.dp, CyberCyan.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            ) {
                CustomCrosshairCanvas(
                    config = cfg,
                    modifier = Modifier.size(76.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Cyber Plus", "Dot Ring", "Chevron").forEachIndexed { idx, name ->
                        val active = cfg.styleIndex == idx
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (active) PureWhite else Color.White.copy(alpha = 0.07f), CircleShape)
                                .clickable { viewModel.updateCrosshairConfig(cfg.copy(styleIndex = idx)) }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (active) DarkInk else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Text(
                    text = "Ukuran Crosshair: ${cfg.sizePx.roundToInt()}px • Gap: ${cfg.gapPx.roundToInt()}px",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
                Slider(
                    value = cfg.sizePx,
                    onValueChange = { viewModel.updateCrosshairConfig(cfg.copy(sizePx = it)) },
                    valueRange = 6f..28f,
                    colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = CyberCyan)
                )
            }
        }
    }
}

@Composable
private fun KeybindMacroCreatorTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    var macroName by remember { mutableStateOf("Quick Pearl Clutch") }
    var macroCmd by remember { mutableStateOf("/gamemode survival") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = macroName,
                onValueChange = { macroName = it },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(color = PureWhite),
                cursorBrush = SolidColor(CyberCyan),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.06f))
                    .padding(8.dp)
            )
            AmberGoldPillButton(
                text = "+ Tambah Macro",
                onClick = { viewModel.addKeybindMacro(macroName, "KEY_V", macroCmd) }
            )
        }

        uiState.keybindMacros.forEach { macro ->
            GlassCard(cornerRadius = 14.dp, contentPadding = PaddingValues(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${macro.name} [${macro.triggerKey}]",
                            style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Action: ${macro.commandOrAction} (${macro.delayMs}ms delay)",
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberCyan)
                        )
                    }
                    Switch(
                        checked = macro.isEnabled,
                        onCheckedChange = { viewModel.toggleMacro(macro.id) },
                        colors = SwitchDefaults.colors(checkedThumbColor = DarkInk, checkedTrackColor = EmeraldGreen)
                    )
                }
            }
        }
    }
}

@Composable
private fun VirtualMouseDesignerTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val vm = uiState.virtualMouseConfig
    GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(78.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF05070C))
                    .border(1.dp, AmethystPurple.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            ) {
                VirtualMouseCursorCanvas(
                    config = vm,
                    modifier = Modifier.size(68.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Skala Kursor Virtual: ${String.format("%.2f", vm.cursorScale)}x",
                    style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                )
                Slider(
                    value = vm.cursorScale,
                    onValueChange = { viewModel.updateVirtualMouseConfig(vm.copy(cursorScale = it)) },
                    valueRange = 0.7f..2.0f,
                    colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = AmethystPurple)
                )
            }
        }
    }
}

@Composable
private fun OledAccentThemeEngineTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AccentThemeOption.entries.forEach { option ->
            val selected = option == uiState.accentTheme
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selected) option.primaryColor.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f))
                    .border(1.dp, if (selected) option.primaryColor else Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                    .clickable { viewModel.setAccentTheme(option) }
                    .padding(vertical = 10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(option.primaryColor, CircleShape)
                ) {
                    if (selected) {
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
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 8.5.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun ServerQuickConnectAndPingTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    var serverHostInput by remember { mutableStateOf(uiState.selectedServerHost) }
    GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = serverHostInput,
                    onValueChange = { serverHostInput = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium.copy(color = CyberCyan, fontWeight = FontWeight.Bold),
                    cursorBrush = SolidColor(PureWhite),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.06f))
                        .padding(8.dp)
                )
                AmberGoldPillButton(
                    text = "Ping Server",
                    onClick = { viewModel.updateSelectedServerHost(serverHostInput) }
                )
                SolidWhitePillButton(
                    text = "Direct Connect",
                    leadingIcon = Icons.Filled.PlayArrow,
                    onClick = {
                        viewModel.closeToolModal()
                        viewModel.startPreLaunchSequence(serverHostInput)
                    }
                )
            }

            PingRadarGraphCanvas(
                pingHistory = uiState.hypixelPingHistory,
                accentColor = EmeraldGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
        }
    }
}

@Composable
private fun GpuRendererQuickSwitcherTool(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val options = listOf(
        "MobileGlues (Vulkan 1.3)",
        "HolyGL4ES (OpenGL 2.1 Fast)",
        "LTW (LightWeightTranslation GLES 3.2)",
        "Zink (Mesa Vulkan 1.3 + Turnip)",
        "VirGL (Virtual OpenGL 4.3)",
        "Freedreno Turnip (Adreno)"
    )
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEach { renderer ->
            val selected = uiState.gpuRenderer == renderer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.05f))
                    .border(1.dp, if (selected) AmberGold else Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .clickable { viewModel.setGpuRenderer(renderer) }
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = renderer,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (selected) AmberGold else PureWhite,
                        fontWeight = FontWeight.Bold
                    )
                )
                if (selected) {
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

@Composable
private fun UniversalInteractiveHardwareAndGameTool(
    toolId: Int,
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kontrol Parameter Langsung (Real-Time Engine)",
                    style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                )
                AmberGoldPillButton(
                    text = "Boost Seketika",
                    leadingIcon = Icons.Filled.Bolt,
                    onClick = { viewModel.triggerAutoOptimizeAndBoost() }
                )
            }

            // Interactive Sliders & Toggles linked to LauncherViewModel
            Text(
                text = "Render Distance: ${uiState.renderDistanceChunks} Chunks • FOV: ${uiState.fovValue.roundToInt()} • Resolution: ${uiState.resolutionScale}%",
                style = MaterialTheme.typography.labelMedium.copy(color = CyberCyan)
            )
            Slider(
                value = uiState.renderDistanceChunks.toFloat(),
                onValueChange = { viewModel.setRenderDistance(it.roundToInt()) },
                valueRange = 2f..32f,
                colors = SliderDefaults.colors(thumbColor = PureWhite, activeTrackColor = EmeraldGreen)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fullbright Gamma 1000% (options.txt)",
                    style = MaterialTheme.typography.bodyMedium.copy(color = PureWhite)
                )
                Switch(
                    checked = uiState.fullbrightEnabled,
                    onCheckedChange = { viewModel.setFullbright(it) },
                    colors = SwitchDefaults.colors(checkedThumbColor = DarkInk, checkedTrackColor = AmberGold)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Unlock 120Hz Refresh Rate & Live FPS HUD",
                    style = MaterialTheme.typography.bodyMedium.copy(color = PureWhite)
                )
                Switch(
                    checked = uiState.unlock120Hz,
                    onCheckedChange = {
                        viewModel.setUnlock120Hz(it)
                        viewModel.setFpsHudOverlay(it)
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = DarkInk, checkedTrackColor = CyberCyan)
                )
            }
        }
    }
}
