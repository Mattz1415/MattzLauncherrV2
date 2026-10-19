package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BottomNavTab
import com.example.model.LauncherAccount
import com.example.model.TopBarSegment
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkInk
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassIslandBg
import com.example.ui.theme.GlassSurfaceBottom
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.LocalLauncherAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CompactTopNavigationBar(
    selectedSegment: TopBarSegment,
    onSelectSegment: (TopBarSegment) -> Unit,
    liveFps: Int,
    livePingMs: Int,
    activeAccount: LauncherAccount,
    onMonogramClick: () -> Unit,
    onSearchLogClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Side: 34.dp Monogram + Segmented Pill Switcher
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MattzMonogramButton(
                onClick = onMonogramClick,
                modifier = Modifier.testTag("top_monogram_button")
            )

            // Sleek Glass Segmented Pill Switcher ("Instans", "Library", "Komunitas", "Marketplace")
            Row(
                modifier = Modifier
                    .height(34.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.09f), CircleShape)
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                TopBarSegment.entries.forEach { segment ->
                    val isSelected = segment == selectedSegment
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) PureWhite else Color.Transparent,
                        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
                        label = "top_seg_bg_${segment.name}"
                    )
                    val txtColor by animateColorAsState(
                        targetValue = if (isSelected) DarkInk else TextSecondary,
                        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
                        label = "top_seg_txt_${segment.name}"
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(bgColor, CircleShape)
                            .clickable { onSelectSegment(segment) }
                            .padding(horizontal = 11.dp, vertical = 4.dp)
                            .testTag("top_segment_${segment.name.lowercase()}")
                    ) {
                        Text(
                            text = segment.label,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = txtColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Right Side: Live FPS/Ping telemetry mini-pill + Search/Log + Mandatory Settings Gear + Profile Avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // Live FPS / Ping Telemetry Mini-Pill
            Row(
                modifier = Modifier
                    .height(32.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.09f), CircleShape)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen)
                )
                Text(
                    text = "$liveFps FPS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                )
                Text(
                    text = "•",
                    style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                )
                Text(
                    text = "${livePingMs}ms",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                )
            }

            // Circular Glass Search / Log Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.09f), CircleShape)
                    .clickable(onClick = onSearchLogClick)
                    .testTag("top_search_log_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Pencarian Cepat & Logcat",
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            // MANDATORY Circular Glass Settings Button (Gear icon that opens Full Settings Hub)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.14f), CircleShape)
                    .clickable(onClick = onSettingsClick)
                    .testTag("top_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Buka Pengaturan Penuh",
                    tint = PureWhite,
                    modifier = Modifier.size(16.dp)
                )
            }

            // 34.dp Circular Profile Avatar with anchored 8.dp Emerald Online Dot
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clickable(onClick = onAccountClick)
                    .testTag("top_account_avatar_button")
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                            shape = CircleShape
                        )
                        .border(1.dp, activeAccount.primarySkinColor.copy(alpha = 0.55f), CircleShape)
                ) {
                    Minecraft2DAvatarHead(
                        primaryColor = activeAccount.primarySkinColor,
                        accentColor = activeAccount.accentSkinColor,
                        size = 22.dp
                    )
                }

                // Anchored 8.dp Emerald Online Dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(EmeraldGreen, CircleShape)
                        .border(1.dp, Color(0xFF030407), CircleShape)
                )
            }
        }
    }
}

@Composable
fun FloatingBottomNavigationIsland(
    selectedTab: BottomNavTab,
    onSelectTab: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = LocalLauncherAccent.current
    val islandShape = RoundedCornerShape(36.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .height(46.dp)
                .clip(islandShape)
                .background(GlassIslandBg, islandShape)
                .border(1.dp, Color(0x1FFFFFFF), islandShape)
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BottomNavTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                val icon = bottomTabIcon(tab)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            color = if (isSelected) PureWhite else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable { onSelectTab(tab) }
                        .animateContentSize(
                            animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f)
                        )
                        .padding(
                            horizontal = if (isSelected) 12.dp else 10.dp,
                            vertical = 6.dp
                        )
                        .testTag("bottom_nav_tab_${tab.index}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = tab.label,
                            tint = if (isSelected) DarkInk else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        if (isSelected) {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = DarkInk,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (accent.primaryColor == PureWhite) CyberCyan else accent.primaryColor,
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun bottomTabIcon(tab: BottomNavTab): ImageVector {
    return when (tab) {
        BottomNavTab.JELAJAH -> Icons.Filled.Explore
        BottomNavTab.MODS_SHADERS -> Icons.Filled.Extension
        BottomNavTab.BOOST_JVM -> Icons.Filled.Speed
        BottomNavTab.PUSAT_ALAT -> Icons.Filled.Build
        BottomNavTab.VERSI_FILES -> Icons.Filled.FolderSpecial
        BottomNavTab.PENGATURAN -> Icons.Filled.Settings
    }
}
