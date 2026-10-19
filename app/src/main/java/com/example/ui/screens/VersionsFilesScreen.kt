package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MinecraftFileFolder
import com.example.ui.components.AmberGoldPillButton
import com.example.ui.components.DarkGlassPillButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassModalDialog
import com.example.ui.components.Minecraft2DAvatarHead
import com.example.ui.components.SolidWhitePillButton
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkInk
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PlatinumSilver
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.LauncherUiState
import com.example.viewmodel.LauncherViewModel
import kotlin.math.roundToInt

@Composable
fun VersionsFilesScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSectionIndex by remember { mutableIntStateOf(0) }
    val sectionTabs = listOf(
        "Minecraft Files (.minecraft Tree)",
        "Versi & Mod Loaders",
        "Rilis Renderer & Runtime Packages",
        "Game Paths, Akun & GitHub Actions APK"
    )

    var showAddPathModal by remember { mutableStateOf(false) }
    var newPathLabel by remember { mutableStateOf("External SD Card Vault") }
    var newPathValue by remember { mutableStateOf("/storage/emulated/0/MattzInstances/Modded") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Top Frosted Ash-Grey Section Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            sectionTabs.forEachIndexed { idx, label ->
                val isSelected = selectedSectionIndex == idx
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (isSelected) PureWhite else Color.White.copy(alpha = 0.07f),
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (isSelected) PureWhite else Color.White.copy(alpha = 0.14f),
                            CircleShape
                        )
                        .clickable { selectedSectionIndex = idx }
                        .padding(horizontal = 13.dp, vertical = 6.dp)
                        .testTag("files_section_tab_$idx")
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (isSelected) DarkInk else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Section Content
        when (selectedSectionIndex) {
            0 -> MinecraftFilesExplorerSection(uiState = uiState, viewModel = viewModel)
            1 -> VersionsAndModLoadersSection(uiState = uiState, viewModel = viewModel)
            2 -> ReleasePackagesAndPluginsSection(uiState = uiState, viewModel = viewModel)
            else -> PathsAccountsAndGithubCiSection(
                uiState = uiState,
                viewModel = viewModel,
                onOpenAddPathModal = { showAddPathModal = true }
            )
        }
    }

    // Add New Game Directory Path Modal
    GlassModalDialog(
        visible = showAddPathModal,
        title = "Tambah Game Directory Path Baru",
        subtitle = "Buat direktori isolasi untuk modpack dan aset versi tertentu",
        onDismiss = { showAddPathModal = false }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(10.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Label Direktori:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    BasicTextField(
                        value = newPathLabel,
                        onValueChange = { newPathLabel = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.titleMedium.copy(color = PureWhite),
                        cursorBrush = SolidColor(CyberCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.07f))
                            .padding(8.dp)
                    )

                    Text(
                        text = "Path Absolut Penyimpanan:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    BasicTextField(
                        value = newPathValue,
                        onValueChange = { newPathValue = it },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.labelMedium.copy(color = CyberCyan),
                        cursorBrush = SolidColor(CyberCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.07f))
                            .padding(8.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                DarkGlassPillButton(text = "Batal", onClick = { showAddPathModal = false })
                Spacer(modifier = Modifier.width(8.dp))
                SolidWhitePillButton(
                    text = "Simpan & Aktifkan Path",
                    onClick = {
                        viewModel.addGameDirectory(newPathLabel, newPathValue)
                        showAddPathModal = false
                    }
                )
            }
        }
    }
}

@Composable
private fun MinecraftFilesExplorerSection(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    val filteredNodes = remember(uiState.minecraftFileNodes, uiState.selectedFileFolder) {
        uiState.minecraftFileNodes.filter {
            uiState.selectedFileFolder == MinecraftFileFolder.ALL || it.folderCategory == uiState.selectedFileFolder
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Folder Filter Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MinecraftFileFolder.entries.forEach { folder ->
                val active = folder == uiState.selectedFileFolder
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (active) PlatinumSilver.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.05f),
                            CircleShape
                        )
                        .border(
                            1.dp,
                            if (active) PureWhite else Color.White.copy(alpha = 0.12f),
                            CircleShape
                        )
                        .clickable { viewModel.selectMinecraftFileFolder(folder) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = folder.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (active) PureWhite else TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // 2-Column Split: Left File Tree List (54%) + Right File Inspector & Manifest Preview (46%)
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.54f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 56.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                filteredNodes.forEach { node ->
                    val selected = node.id == uiState.selectedFileNode.id
                    GlassCard(
                        cornerRadius = 16.dp,
                        borderColor = if (selected) PureWhite else Color.White.copy(alpha = 0.14f),
                        onClick = { viewModel.selectMinecraftFileNode(node) },
                        contentPadding = PaddingValues(10.dp)
                    ) {
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
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Description,
                                        contentDescription = node.fileName,
                                        tint = if (selected) PureWhite else CyberCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = node.fileName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = PureWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = node.relativePath,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextSecondary,
                                            fontSize = 8.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.08f), CircleShape)
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = node.fileTypeBadge,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AmberGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp
                                        )
                                    )
                                }
                                Text(
                                    text = node.sizeLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldGreen,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Right Column: File Inspector & Content / SHA-1 Viewer
            val activeNode = uiState.selectedFileNode
            Column(
                modifier = Modifier
                    .weight(0.46f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 56.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassCard(
                    cornerRadius = 20.dp,
                    borderColor = PlatinumSilver.copy(alpha = 0.35f),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = activeNode.fileName,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = PureWhite,
                                    fontSize = 15.sp
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Verified,
                                    contentDescription = "SHA-1 Verified",
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "SHA-1: ${activeNode.sha1Short}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = EmeraldGreen)
                                )
                            }
                        }

                        Text(
                            text = activeNode.relativePath,
                            style = MaterialTheme.typography.labelSmall.copy(color = CyberCyan)
                        )

                        Text(
                            text = activeNode.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary.copy(alpha = 0.88f),
                                fontSize = 10.5.sp
                            )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0A0D13))
                                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = activeNode.previewContent,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = PlatinumSilver,
                                    fontSize = 9.5.sp,
                                    lineHeight = 14.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VersionsAndModLoadersSection(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 56.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        uiState.installedVersions.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { ver ->
                    val isDownloading = uiState.downloadingVersionId == ver.id
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        cornerRadius = 18.dp,
                        borderColor = if (ver.isInstalled) EmeraldGreen.copy(alpha = 0.32f) else Color.White.copy(alpha = 0.14f),
                        contentPadding = PaddingValues(11.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(9.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    val progress = when {
                                        isDownloading -> uiState.downloadVersionProgress
                                        ver.isInstalled -> 1f
                                        else -> 0.25f
                                    }
                                    val ringColor = when {
                                        isDownloading -> AmberGold
                                        ver.isInstalled -> EmeraldGreen
                                        else -> CyberCyan
                                    }
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val sw = 3.dp.toPx()
                                        drawArc(
                                            color = Color.White.copy(alpha = 0.10f),
                                            startAngle = 0f,
                                            sweepAngle = 360f,
                                            useCenter = false,
                                            topLeft = Offset(sw / 2f, sw / 2f),
                                            size = Size(size.width - sw, size.height - sw),
                                            style = Stroke(width = sw)
                                        )
                                        drawArc(
                                            color = ringColor,
                                            startAngle = -90f,
                                            sweepAngle = 360f * progress,
                                            useCenter = false,
                                            topLeft = Offset(sw / 2f, sw / 2f),
                                            size = Size(size.width - sw, size.height - sw),
                                            style = Stroke(width = sw, cap = StrokeCap.Round)
                                        )
                                    }
                                    Text(
                                        text = if (isDownloading) "${(progress * 100).roundToInt()}%" else ver.versionNumber,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = PureWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp
                                        )
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${ver.versionNumber} • ${ver.loader}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = PureWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${ver.releaseChannel} • ${ver.sizeMb} MB • ${ver.jreRequired}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 9.5.sp
                                        )
                                    )
                                }
                            }

                            if (ver.isInstalled) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(EmeraldGreen.copy(alpha = 0.16f), CircleShape)
                                        .border(1.dp, EmeraldGreen.copy(alpha = 0.4f), CircleShape)
                                        .padding(horizontal = 9.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Terpasang",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = EmeraldGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            } else {
                                SolidWhitePillButton(
                                    text = if (isDownloading) "..." else "Pasang",
                                    leadingIcon = Icons.Filled.CloudDownload,
                                    onClick = { viewModel.installMinecraftVersion(ver.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReleasePackagesAndPluginsSection(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 56.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        uiState.zalithReleaseAssets.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pair.forEach { asset ->
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        cornerRadius = 18.dp,
                        borderColor = if (asset.isInstalled) PlatinumSilver.copy(alpha = 0.32f) else Color.White.copy(alpha = 0.14f),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.DeveloperBoard,
                                        contentDescription = asset.assetName,
                                        tint = CyberCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = asset.assetName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = PureWhite,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(AmberGold.copy(alpha = 0.16f), CircleShape)
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = asset.versionTag,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AmberGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Text(
                                text = asset.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary.copy(alpha = 0.85f),
                                    fontSize = 10.sp
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${asset.category} • ${asset.arch} • ${asset.fileSize}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )

                                if (asset.isInstalled) {
                                    Text(
                                        text = "AKTIF DI RUNTIMES/",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = EmeraldGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                } else {
                                    SolidWhitePillButton(
                                        text = "Ekstrak Paket",
                                        onClick = { viewModel.installZalithReleaseAsset(asset.id) }
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

@Composable
private fun PathsAccountsAndGithubCiSection(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    onOpenAddPathModal: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left: Game Directories + Accounts
        Column(
            modifier = Modifier
                .weight(0.52f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Folder,
                                contentDescription = "Direktori Game",
                                tint = AmberGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Game Directories & Paths",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        AmberGoldPillButton(
                            text = "+ Path Baru",
                            leadingIcon = Icons.Filled.CreateNewFolder,
                            onClick = onOpenAddPathModal,
                            modifier = Modifier.testTag("add_new_path_button")
                        )
                    }

                    uiState.gameDirectories.forEach { dir ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (dir.isActive) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.04f)
                                )
                                .border(
                                    1.dp,
                                    if (dir.isActive) AmberGold else Color.White.copy(alpha = 0.10f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.selectGameDirectory(dir.id) }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = dir.label,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = PureWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp
                                    )
                                )
                                Text(
                                    text = dir.path,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyberCyan,
                                        fontSize = 9.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = dir.label,
                                tint = if (dir.isActive) EmeraldGreen else Color.White.copy(alpha = 0.16f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            GlassCard(cornerRadius = 18.dp, contentPadding = PaddingValues(12.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Manajer Akun",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Akun Microsoft OAuth & Offline",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        DarkGlassPillButton(
                            text = "+ Tambah Akun",
                            leadingIcon = Icons.Filled.Add,
                            onClick = { viewModel.setShowAccountModal(true) }
                        )
                    }

                    uiState.accounts.forEach { acc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (acc.isActive) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.04f)
                                )
                                .border(
                                    1.dp,
                                    if (acc.isActive) EmeraldGreen else Color.White.copy(alpha = 0.10f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.selectAccount(acc.id) }
                                .padding(9.dp),
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
                                    size = 28.dp
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
                                        text = acc.accountType,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 9.5.sp
                                        )
                                    )
                                }
                            }
                            if (acc.isActive) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Aktif",
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Right: GitHub Actions Build APK Workflow Inspector (.github/workflows/build-apk.yml)
        Column(
            modifier = Modifier
                .weight(0.48f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GlassCard(
                cornerRadius = 20.dp,
                borderColor = PlatinumSilver.copy(alpha = 0.35f),
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Code,
                                contentDescription = "GitHub Actions APK CI/CD",
                                tint = PlatinumSilver,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "GitHub Actions APK Build Workflow",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "TERPASANG",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = "Path: /.github/workflows/build-apk.yml",
                        style = MaterialTheme.typography.labelMedium.copy(color = CyberCyan)
                    )

                    Text(
                        text = "Otomatis mem-build Debug & Release APK (JDK 21 Temurin + Gradle 9.3.1 + Auto Keystore Signing) setiap kali Push atau Pull Request ke GitHub.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0A0D13))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "name: Build MattzLauncher Android APK\non: [push, pull_request, workflow_dispatch]\njobs:\n  build-apk:\n    runs-on: ubuntu-latest\n    steps:\n      - uses: actions/checkout@v4\n      - uses: actions/setup-java@v4 (temurin 21)\n      - name: Configure Android SDK & Accept All Licenses\n      - uses: gradle/actions/setup-gradle@v4 (9.3.1)\n      - run: gradle :app:assembleDebug :app:assembleRelease\n      - uses: actions/upload-artifact@v4",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PlatinumSilver,
                                fontSize = 9.sp,
                                lineHeight = 13.5.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
