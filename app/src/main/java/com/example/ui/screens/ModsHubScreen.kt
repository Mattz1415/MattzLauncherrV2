package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ModItem
import com.example.model.ModPlatform
import com.example.model.ModsSubTab
import com.example.ui.components.AmberGoldPillButton
import com.example.ui.components.DarkGlassPillButton
import com.example.ui.components.DualRingLoadingSpinner
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassModalDialog
import com.example.ui.components.SolidWhitePillButton
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkInk
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassSurfaceBottom
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.LocalLauncherAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.LauncherUiState
import com.example.viewmodel.LauncherViewModel

@Composable
fun ModsHubScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    var showLocalInstallModal by remember { mutableStateOf(false) }
    var localFileNameInput by remember { mutableStateOf("sodium-extra-0.6.1+mc1.21.4.jar") }

    val versionFilters = listOf("Semua Versi", "1.21.4", "1.20.1", "1.16.5", "1.8.9")

    val filteredMods = remember(
        uiState.modsList,
        uiState.selectedModsSubTab,
        uiState.selectedModPlatform,
        uiState.selectedModMcVersion,
        uiState.modSearchQuery
    ) {
        uiState.modsList.filter { item ->
            val tabMatch = item.category == uiState.selectedModsSubTab
            val platformMatch = uiState.selectedModPlatform == ModPlatform.ALL || item.platform == uiState.selectedModPlatform
            val versionMatch = uiState.selectedModMcVersion == "Semua Versi" || item.mcVersion == uiState.selectedModMcVersion
            val queryMatch = uiState.modSearchQuery.isBlank() ||
                item.name.contains(uiState.modSearchQuery, ignoreCase = true) ||
                item.summary.contains(uiState.modSearchQuery, ignoreCase = true) ||
                item.author.contains(uiState.modSearchQuery, ignoreCase = true)
            tabMatch && platformMatch && versionMatch && queryMatch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1: Top Segmented Sub-Tabs ("Installed Mods (.jar Toggle)", "Download Mod", "Modpacks", "Resource Packs", "Shader Packs", "Worlds")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModsSubTab.entries.forEach { tab ->
                val isSelected = tab == uiState.selectedModsSubTab
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (isSelected) PureWhite else Color.White.copy(alpha = 0.06f),
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (isSelected) PureWhite else Color.White.copy(alpha = 0.10f),
                            CircleShape
                        )
                        .clickable { viewModel.selectModsSubTab(tab) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("mods_subtab_${tab.name.lowercase()}")
                ) {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (isSelected) DarkInk else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Row 2: Glass Search Bar + Platform Filter Pills (Modrinth / CurseForge) + Version Filter + "Install dari Penyimpanan Lokal"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Glass Search Bar
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Cari Mod atau Shader",
                    tint = CyberCyan,
                    modifier = Modifier.size(15.dp)
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (uiState.modSearchQuery.isEmpty()) {
                        Text(
                            text = "Cari Sodium, Iris, Modpack, Shader...",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                    BasicTextField(
                        value = uiState.modSearchQuery,
                        onValueChange = { viewModel.updateModSearchQuery(it) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = PureWhite),
                        cursorBrush = SolidColor(CyberCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mods_search_input")
                    )
                }
            }

            // Platform Filter Pills (Semua / Modrinth / CurseForge)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ModPlatform.entries.forEach { platform ->
                    val active = platform == uiState.selectedModPlatform
                    val pillColor = when (platform) {
                        ModPlatform.MODRINTH -> EmeraldGreen
                        ModPlatform.CURSEFORGE -> AmberGold
                        ModPlatform.ALL -> CyberCyan
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (active) pillColor.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.05f),
                                CircleShape
                            )
                            .border(
                                1.dp,
                                if (active) pillColor else Color.White.copy(alpha = 0.10f),
                                CircleShape
                            )
                            .clickable { viewModel.selectModPlatform(platform) }
                            .padding(horizontal = 9.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = platform.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (active) PureWhite else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp
                            )
                        )
                    }
                }
            }

            // Version cycle pill
            val nextVersionIdx = (versionFilters.indexOf(uiState.selectedModMcVersion) + 1) % versionFilters.size
            DarkGlassPillButton(
                text = uiState.selectedModMcVersion,
                onClick = { viewModel.selectModMcVersion(versionFilters[nextVersionIdx]) }
            )

            // "Install dari Penyimpanan Lokal" Glass Pill Button
            AmberGoldPillButton(
                text = "Install dari Penyimpanan Lokal",
                leadingIcon = Icons.Filled.FolderOpen,
                onClick = { showLocalInstallModal = true },
                modifier = Modifier.testTag("install_local_storage_button")
            )
        }

        // Row 3: Content Grid or Dual-Ring Loading Spinner when fetching/filtering
        if (uiState.isModsFetching) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 52.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DualRingLoadingSpinner(
                        size = 52.dp,
                        primaryColor = LocalLauncherAccent.current.primaryColor,
                        secondaryColor = AmberGold
                    )
                    Text(
                        text = "Menyinkronkan Indeks Repositori Modrinth & CurseForge...",
                        style = MaterialTheme.typography.titleMedium.copy(color = TextSecondary)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 56.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredMods, key = { it.id }) { mod ->
                    ModGlassCardItem(
                        mod = mod,
                        onToggleEnabled = { viewModel.toggleModEnabled(mod.id) },
                        onInstall = { viewModel.installModItem(mod.id) }
                    )
                }
            }
        }
    }

    // Local Storage Installer Modal
    GlassModalDialog(
        visible = showLocalInstallModal,
        title = "Install dari Penyimpanan Lokal",
        subtitle = "Impor file .jar, .mrpack, atau .zip langsung ke folder instans aktif",
        onDismiss = { showLocalInstallModal = false }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Path Direktori Target: /storage/emulated/0/games/com.mattz.launcher/.minecraft/mods",
                style = MaterialTheme.typography.labelMedium.copy(color = CyberCyan)
            )

            GlassCard(
                cornerRadius = 18.dp,
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Nama File Paket (.jar / .zip / .mrpack):",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    BasicTextField(
                        value = localFileNameInput,
                        onValueChange = { localFileNameInput = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleMedium.copy(color = PureWhite),
                        cursorBrush = SolidColor(CyberCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DarkGlassPillButton(
                    text = "Batal",
                    onClick = { showLocalInstallModal = false }
                )
                Spacer(modifier = Modifier.width(8.dp))
                SolidWhitePillButton(
                    text = "Pasang & Aktifkan .JAR",
                    onClick = {
                        viewModel.installFromLocalStorageSimulated(localFileNameInput)
                        showLocalInstallModal = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ModGlassCardItem(
    mod: ModItem,
    onToggleEnabled: () -> Unit,
    onInstall: () -> Unit
) {
    GlassCard(
        cornerRadius = 18.dp,
        borderColor = if (mod.isEnabled && mod.isInstalled) {
            mod.accentColor.copy(alpha = 0.28f)
        } else {
            Color.White.copy(alpha = 0.09f)
        },
        contentPadding = PaddingValues(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(mod.accentColor.copy(alpha = 0.16f))
                            .border(1.dp, mod.accentColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Extension,
                            contentDescription = mod.name,
                            tint = mod.accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mod.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "oleh ${mod.author} • ${mod.platform.label} • MC ${mod.mcVersion}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 9.5.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (mod.isInstalled) {
                    Switch(
                        checked = mod.isEnabled,
                        onCheckedChange = { onToggleEnabled() },
                        modifier = Modifier.height(22.dp),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = DarkInk,
                            checkedTrackColor = EmeraldGreen,
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                        )
                    )
                } else {
                    SolidWhitePillButton(
                        text = "Install",
                        leadingIcon = Icons.Filled.CloudDownload,
                        onClick = onInstall
                    )
                }
            }

            Text(
                text = mod.summary,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextPrimary.copy(alpha = 0.85f),
                    fontSize = 10.5.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = mod.fileNameJar,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyan,
                        fontSize = 9.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${mod.fileSize} • ${mod.downloads}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                    if (mod.isInstalled) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Terpasang",
                            tint = if (mod.isEnabled) EmeraldGreen else TextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
