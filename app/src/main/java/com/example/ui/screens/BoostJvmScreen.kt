package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AmberGoldPillButton
import com.example.ui.components.DualRingLoadingSpinner
import com.example.ui.components.GlassCard
import com.example.ui.components.SolidWhitePillButton
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
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
fun BoostJvmScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val accent = LocalLauncherAccent.current
    val leftScroll = rememberScrollState()
    val rightScroll = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // LEFT COLUMN (54% width): RAM Allocation Slider (1024..16384 MB) + 4 JVM Preset Cards + Live Flag Editor
        Column(
            modifier = Modifier
                .weight(0.54f)
                .fillMaxHeight()
                .verticalScroll(leftScroll)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Card 1: Interactive RAM Allocation Slider (1024 MB to 16384 MB)
            GlassCard(
                cornerRadius = 18.dp,
                borderColor = accent.primaryColor.copy(alpha = 0.28f),
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
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Memory,
                                contentDescription = "RAM Allocation",
                                tint = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Alokasi Heap RAM (-Xmx / -Xms)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.16f), CircleShape)
                                .border(1.dp, CyberCyan.copy(alpha = 0.45f), CircleShape)
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${uiState.ramAllocationMb} MB (${String.format("%.1f", uiState.ramAllocationMb / 1024f)} GB)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Slider(
                        value = uiState.ramAllocationMb.toFloat(),
                        onValueChange = { viewModel.updateRamAllocation(it.roundToInt()) },
                        valueRange = 1024f..16384f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .testTag("ram_allocation_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = PureWhite,
                            activeTrackColor = CyberCyan,
                            inactiveTrackColor = Color.White.copy(alpha = 0.12f)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Min: 1024 MB (Low-End)",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                        Text(
                            text = "Optimal: 4096 - 6144 MB",
                            style = MaterialTheme.typography.labelSmall.copy(color = EmeraldGreen)
                        )
                        Text(
                            text = "Max: 16384 MB (Extreme)",
                            style = MaterialTheme.typography.labelSmall.copy(color = AmberGold)
                        )
                    }
                }
            }

            // Card 2: Selectable Glass Cards for JVM Arguments Presets
            Text(
                text = "JVM ARGUMENTS PRESETS (PILIH PROFIL GARBAGE COLLECTOR)",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                uiState.jvmPresets.chunked(2).forEach { rowPresets ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowPresets.forEach { preset ->
                            val selected = preset.id == uiState.selectedJvmPresetId
                            GlassCard(
                                modifier = Modifier.weight(1f),
                                cornerRadius = 18.dp,
                                borderColor = if (selected) PureWhite else Color.White.copy(alpha = 0.09f),
                                onClick = { viewModel.selectJvmPreset(preset) },
                                contentPadding = PaddingValues(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = preset.title,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = if (selected) PureWhite else TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(
                                                    if (selected) PureWhite else AmberGold.copy(alpha = 0.16f),
                                                    CircleShape
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = preset.badge,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = if (selected) DarkInk else AmberGold,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 8.sp
                                                )
                                            )
                                        }
                                    }

                                    Text(
                                        text = preset.subtitle,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 9.5.sp
                                        ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = "${preset.recommendedRamMb} MB • ${preset.estFpsBoost}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = EmeraldGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Card 3: Live Flag Preview Editor
            GlassCard(
                cornerRadius = 18.dp,
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
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Code,
                                contentDescription = "Live JVM Flag Editor",
                                tint = AmberGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Live JVM Arguments Preview & Editor",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "-Xmx${uiState.ramAllocationMb}M",
                            style = MaterialTheme.typography.labelMedium.copy(color = CyberCyan)
                        )
                    }

                    BasicTextField(
                        value = uiState.customJvmFlagsText,
                        onValueChange = { viewModel.updateCustomJvmFlags(it) },
                        textStyle = MaterialTheme.typography.labelMedium.copy(
                            color = EmeraldGreen,
                            fontSize = 10.sp
                        ),
                        cursorBrush = SolidColor(PureWhite),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF05070C))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                            .testTag("jvm_flags_editor")
                    )
                }
            }
        }

        // RIGHT COLUMN (46% width): Java Runtime (JRE 8 / 17 / 21) Switcher + JRE Integrity Benchmark + One-Click RAM GC & Thermal Cooler Radar
        Column(
            modifier = Modifier
                .weight(0.46f)
                .fillMaxHeight()
                .verticalScroll(rightScroll)
                .padding(bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Java Runtime Switcher (JRE 8 / JRE 17 / JRE 21)
            GlassCard(
                cornerRadius = 18.dp,
                contentPadding = PaddingValues(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeveloperBoard,
                            contentDescription = "Java Runtime Switcher",
                            tint = CyberCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Java Runtime Environment (JRE ARM64)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    uiState.jreRuntimes.forEach { jre ->
                        val active = jre.id == uiState.selectedJreId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (active) Color.White.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.04f)
                                )
                                .border(
                                    1.dp,
                                    if (active) CyberCyan else Color.White.copy(alpha = 0.08f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.selectJreRuntime(jre.id) }
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = jre.id,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            color = if (active) CyberCyan else PureWhite,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = jre.name,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text(
                                    text = "${jre.architecture} • ${jre.compatibleMc}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary,
                                        fontSize = 9.sp
                                    )
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = jre.id,
                                tint = if (active) EmeraldGreen else Color.White.copy(alpha = 0.18f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Built-in JRE Integrity Benchmark Runner with Animated Circular Score Gauge
            GlassCard(
                cornerRadius = 18.dp,
                contentPadding = PaddingValues(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(64.dp)
                    ) {
                        if (uiState.isBenchmarkRunning) {
                            DualRingLoadingSpinner(
                                size = 58.dp,
                                primaryColor = AmberGold,
                                secondaryColor = CyberCyan
                            )
                        } else {
                            val scoreRatio by animateFloatAsState(
                                targetValue = (uiState.jreBenchmarkScore / 10500f).coerceIn(0.2f, 1f),
                                animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
                                label = "bench_gauge"
                            )
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val sw = 5.dp.toPx()
                                drawArc(
                                    color = Color.White.copy(alpha = 0.09f),
                                    startAngle = 135f,
                                    sweepAngle = 270f,
                                    useCenter = false,
                                    topLeft = Offset(sw / 2f, sw / 2f),
                                    size = Size(size.width - sw, size.height - sw),
                                    style = Stroke(width = sw, cap = StrokeCap.Round)
                                )
                                drawArc(
                                    brush = Brush.sweepGradient(listOf(CyberCyan, EmeraldGreen, AmberGold)),
                                    startAngle = 135f,
                                    sweepAngle = 270f * scoreRatio,
                                    useCenter = false,
                                    topLeft = Offset(sw / 2f, sw / 2f),
                                    size = Size(size.width - sw, size.height - sw),
                                    style = Stroke(width = sw, cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${uiState.jreBenchmarkScore}",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = PureWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = "PTS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldGreen,
                                        fontSize = 8.sp
                                    )
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "JRE Integrity & JIT Benchmark",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = if (uiState.isBenchmarkRunning) {
                                "Menguji kompilasi JIT ${(uiState.benchmarkProgress * 100).roundToInt()}%..."
                            } else {
                                "Runtime ${uiState.selectedJreId} Terverifikasi • 0 Memory Leak"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                        SolidWhitePillButton(
                            text = if (uiState.isBenchmarkRunning) "Menguji JRE..." else "Jalankan Benchmark JRE",
                            leadingIcon = Icons.Filled.Speed,
                            onClick = { viewModel.runJreBenchmark() },
                            modifier = Modifier.testTag("run_jre_benchmark_button")
                        )
                    }
                }
            }

            // One-Click RAM Garbage Collector & Thermal Cooler Radar
            GlassCard(
                cornerRadius = 18.dp,
                contentPadding = PaddingValues(12.dp)
            ) {
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
                                imageVector = Icons.Filled.Thermostat,
                                contentDescription = "Thermal Cooler Radar",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "RAM Garbage Collector & Thermal Cooler",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        Text(
                            text = "-${uiState.lastGcFreedMb} MB Freed",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = "Suhu Saat Ini: ${uiState.batteryTempCelsius}°C • Beban CPU: ${uiState.cpuLoadPercent}% • Heap Aktif: ${uiState.ramUsedGb} GB",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    )

                    AmberGoldPillButton(
                        text = "One-Click RAM GC & Thermal Cooldown",
                        leadingIcon = Icons.Filled.Bolt,
                        onClick = { viewModel.triggerAutoOptimizeAndBoost() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("one_click_gc_cooler_button")
                    )
                }
            }
        }
    }
}
