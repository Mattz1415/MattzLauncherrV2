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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DefaultLauncherCatalog
import com.example.model.ToolCategory
import com.example.model.ToolDefinition
import com.example.ui.components.GlassCard
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkInk
import com.example.ui.theme.GlassSurfaceBottom
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.LauncherUiState
import com.example.viewmodel.LauncherViewModel

@Composable
fun ToolsCenterScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val filteredTools = remember(uiState.selectedToolCategory, uiState.toolSearchQuery) {
        DefaultLauncherCatalog.all52Tools.filter { tool ->
            val catMatch = uiState.selectedToolCategory == ToolCategory.ALL || tool.category == uiState.selectedToolCategory
            val queryMatch = uiState.toolSearchQuery.isBlank() ||
                tool.title.contains(uiState.toolSearchQuery, ignoreCase = true) ||
                tool.subtitle.contains(uiState.toolSearchQuery, ignoreCase = true) ||
                tool.id.toString() == uiState.toolSearchQuery.trim()
            catMatch && queryMatch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Top Filter & Search Bar Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search box for 52 tools
            Row(
                modifier = Modifier
                    .width(230.dp)
                    .height(34.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                        shape = CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Cari 50+ Alat",
                    tint = CyberCyan,
                    modifier = Modifier.size(15.dp)
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (uiState.toolSearchQuery.isEmpty()) {
                        Text(
                            text = "Cari 52 alat (Portal, FPS, Crosshair...)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            ),
                            maxLines = 1
                        )
                    }
                    BasicTextField(
                        value = uiState.toolSearchQuery,
                        onValueChange = { viewModel.updateToolSearchQuery(it) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall.copy(color = PureWhite),
                        cursorBrush = SolidColor(CyberCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tools_search_input")
                    )
                }
            }

            // Category Pills
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ToolCategory.entries.forEach { category ->
                    val selected = category == uiState.selectedToolCategory
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (selected) PureWhite else Color.White.copy(alpha = 0.06f),
                                CircleShape
                            )
                            .border(
                                1.dp,
                                if (selected) PureWhite else Color.White.copy(alpha = 0.10f),
                                CircleShape
                            )
                            .clickable { viewModel.selectToolCategory(category) }
                            .padding(horizontal = 11.dp, vertical = 6.dp)
                            .testTag("tool_cat_${category.name.lowercase()}")
                    ) {
                        Text(
                            text = category.label,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = if (selected) DarkInk else TextSecondary,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 10.5.sp
                            )
                        )
                    }
                }
            }
        }

        // 3-Column Bento Grid of all 52 interactive tools
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredTools, key = { it.id }) { tool ->
                ToolBentoCard(
                    tool = tool,
                    onClick = { viewModel.openToolModal(tool) }
                )
            }
        }
    }
}

@Composable
private fun ToolBentoCard(
    tool: ToolDefinition,
    onClick: () -> Unit
) {
    GlassCard(
        cornerRadius = 18.dp,
        borderColor = tool.accentColor.copy(alpha = 0.22f),
        onClick = onClick,
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier.testTag("tool_card_${tool.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(tool.accentColor.copy(alpha = 0.16f))
                            .border(1.dp, tool.accentColor.copy(alpha = 0.4f), RoundedCornerShape(7.dp))
                    ) {
                        Icon(
                            imageVector = iconForCategory(tool.category),
                            contentDescription = tool.title,
                            tint = tool.accentColor,
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Text(
                        text = "#${String.format("%02d", tool.id)}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(tool.accentColor.copy(alpha = 0.15f), CircleShape)
                        .border(1.dp, tool.accentColor.copy(alpha = 0.35f), CircleShape)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tool.badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = tool.accentColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp
                        )
                    )
                }
            }

            Text(
                text = tool.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = tool.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextPrimary.copy(alpha = 0.78f),
                    fontSize = 9.5.sp,
                    lineHeight = 12.5.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun iconForCategory(category: ToolCategory): ImageVector {
    return when (category) {
        ToolCategory.TELEMETRY_DISPLAY -> Icons.Filled.Speed
        ToolCategory.JVM_MEMORY -> Icons.Filled.Memory
        ToolCategory.GAME_OPTIONS -> Icons.Filled.Tune
        ToolCategory.SERVER_WORLD -> Icons.Filled.Dns
        ToolCategory.ADVANCED_SYSTEM -> Icons.Filled.SettingsSuggest
        ToolCategory.ALL -> Icons.Filled.Build
    }
}
