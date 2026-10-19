package com.example.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BottomNavTab
import com.example.model.DefaultLauncherCatalog
import com.example.ui.components.AmberGoldPillButton
import com.example.ui.components.AnimatedCircularRingGauge
import com.example.ui.components.DarkGlassPillButton
import com.example.ui.components.GlassCard
import com.example.ui.components.PingRadarGraphCanvas
import com.example.ui.components.SolidWhitePillButton
import com.example.ui.components.VectorSparkleIcon
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CrimsonRed
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
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun MainHubScreen(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // LEFT COLUMN (42% width) - HERO INSTANCE CARD
        HeroInstanceCard(
            uiState = uiState,
            onPrevInstance = { viewModel.switchInstance(-1) },
            onNextInstance = { viewModel.switchInstance(1) },
            onSelectInstanceDot = { viewModel.selectInstanceIndex(it) },
            onPlayNowClick = { viewModel.startPreLaunchSequence() },
            onConfigureInstanceClick = { viewModel.setShowInstanceConfigModal(true) },
            modifier = Modifier
                .weight(0.42f)
                .fillMaxHeight()
        )

        // RIGHT COLUMN (58% width) - SCROLLABLE BENTO COMMAND DASHBOARD
        RightBentoCommandDashboard(
            uiState = uiState,
            viewModel = viewModel,
            modifier = Modifier
                .weight(0.58f)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun HeroInstanceCard(
    uiState: LauncherUiState,
    onPrevInstance: () -> Unit,
    onNextInstance: () -> Unit,
    onSelectInstanceDot: (Int) -> Unit,
    onPlayNowClick: () -> Unit,
    onConfigureInstanceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val instance = uiState.currentInstance
    val accent = LocalLauncherAccent.current
    val infiniteTransition = rememberInfiniteTransition(label = "hero_shader_wave")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_phase"
    )

    val heroShape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .clip(heroShape)
            .background(
                brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                shape = heroShape
            )
            .border(1.dp, Color.White.copy(alpha = 0.09f), heroShape)
    ) {
        // Animated Canvas wave/shader backdrop
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Radial cyber glow from instance color
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        instance.primaryColor.copy(alpha = 0.24f),
                        instance.secondaryColor.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.78f, h * 0.25f),
                    radius = w * 0.75f
                ),
                radius = w * 0.75f,
                center = Offset(w * 0.78f, h * 0.25f)
            )

            // Flowing futuristic shader wave lines
            for (layer in 0..2) {
                val wavePath = Path()
                val baseY = h * (0.44f + layer * 0.14f)
                val amp = h * (0.045f - layer * 0.008f)
                val steps = 32
                for (i in 0..steps) {
                    val x = (w / steps) * i
                    val angle = (i.toFloat() / steps) * 4.2f + wavePhase + layer * 1.1f
                    val y = baseY + sin(angle) * amp
                    if (i == 0) wavePath.moveTo(x, y) else wavePath.lineTo(x, y)
                }
                drawPath(
                    path = wavePath,
                    color = instance.primaryColor.copy(alpha = 0.18f - layer * 0.04f),
                    style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Glass Badge ("SIAP DIMAINKAN" + Vector Sparkle) + Version Pill
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.07f), CircleShape)
                            .border(1.dp, EmeraldGreen.copy(alpha = 0.35f), CircleShape)
                            .padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        VectorSparkleIcon(color = EmeraldGreen, size = 11.dp)
                        Text(
                            text = "SIAP DIMAINKAN",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(instance.primaryColor.copy(alpha = 0.15f), CircleShape)
                            .border(1.dp, instance.primaryColor.copy(alpha = 0.4f), CircleShape)
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = instance.versionBadge,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Editorial Instance Title + Playtime & Session Counter
                Text(
                    text = instance.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 24.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "Total Bermain: ${instance.playHours} jam • ${instance.sessionsCount} Sesi",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )

                // Hardware quick tags row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiniInstanceSpecBadge(label = "${uiState.ramAllocationMb} MB RAM", color = CyberCyan)
                    MiniInstanceSpecBadge(label = uiState.selectedJreId, color = AmberGold)
                    MiniInstanceSpecBadge(label = uiState.gpuRenderer.substringBefore(" "), color = EmeraldGreen)
                }
            }

            // Middle Row: Interactive Version Switcher Arrows (< and > circular glass buttons with capsule page dots)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircularArrowButton(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDesc = "Versi Sebelumnya",
                    onClick = onPrevInstance,
                    modifier = Modifier.testTag("instance_prev_button")
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    uiState.instances.forEachIndexed { idx, _ ->
                        val active = idx == uiState.selectedInstanceIndex
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (active) 20.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (active) PureWhite else Color.White.copy(alpha = 0.22f),
                                    CircleShape
                                )
                                .clickable { onSelectInstanceDot(idx) }
                                .animateContentSize(spring(dampingRatio = 0.75f, stiffness = 350f))
                        )
                    }
                }

                CircularArrowButton(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDesc = "Versi Berikutnya",
                    onClick = onNextInstance,
                    modifier = Modifier.testTag("instance_next_button")
                )
            }

            // Bottom CTAs: Primary Solid White Pill "Mainkan Sekarang" + Secondary Glass Pill "+ Konfigurasi Instans"
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                SolidWhitePillButton(
                    text = "Mainkan Sekarang",
                    leadingIcon = Icons.Filled.PlayArrow,
                    onClick = onPlayNowClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("play_now_button")
                )

                DarkGlassPillButton(
                    text = "+ Konfigurasi Instans",
                    leadingIcon = Icons.Filled.Add,
                    onClick = onConfigureInstanceClick,
                    borderColor = accent.primaryColor.copy(alpha = 0.28f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("configure_instance_button")
                )
            }
        }
    }
}

@Composable
private fun MiniInstanceSpecBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f), CircleShape)
            .border(1.dp, color.copy(alpha = 0.30f), CircleShape)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp
            )
        )
    }
}

@Composable
private fun CircularArrowButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDesc: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.07f), CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.14f), CircleShape)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDesc,
            tint = PureWhite,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun RightBentoCommandDashboard(
    uiState: LauncherUiState,
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val accent = LocalLauncherAccent.current

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(bottom = 52.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // (a) "MATTZ DISCOVERY & COMMANDS" Header + 2 prominent pill buttons side-by-side
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "MATTZ DISCOVERY & COMMANDS",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = accent.primaryColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.8.sp
                    )
                )
                Text(
                    text = uiState.dynamicTimeGreeting,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 9.sp
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AmberGoldPillButton(
                    text = "Auto-Optimize & Boost",
                    leadingIcon = Icons.Filled.Bolt,
                    onClick = { viewModel.triggerAutoOptimizeAndBoost() },
                    modifier = Modifier.testTag("auto_optimize_button")
                )

                DarkGlassPillButton(
                    text = "Buka 50+ Fitur & Tweaks",
                    leadingIcon = Icons.Filled.AutoFixHigh,
                    onClick = { viewModel.selectBottomTab(BottomNavTab.PUSAT_ALAT) },
                    modifier = Modifier.testTag("open_50_tools_button")
                )
            }
        }

        // (b) Row of 4 Real-Time Animated Circular Ring Gauges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AnimatedCircularRingGauge(
                title = "RAM Usage",
                primaryValue = "${uiState.ramPercent}%",
                subValue = "${uiState.ramUsedGb}/${uiState.ramTotalGb} GB",
                progressFraction = uiState.ramPercent / 100f,
                accentColor = CyberCyan,
                onClick = { viewModel.openToolModal(DefaultLauncherCatalog.all52Tools[0]) },
                modifier = Modifier.weight(1f)
            )
            AnimatedCircularRingGauge(
                title = "CPU Load",
                primaryValue = "${uiState.cpuLoadPercent}%",
                subValue = "8-Core ARM64",
                progressFraction = uiState.cpuLoadPercent / 100f,
                accentColor = EmeraldGreen,
                onClick = { viewModel.openToolModal(DefaultLauncherCatalog.all52Tools[1]) },
                modifier = Modifier.weight(1f)
            )
            AnimatedCircularRingGauge(
                title = "Suhu Baterai",
                primaryValue = "${uiState.batteryTempCelsius}°",
                subValue = "${uiState.batteryTempCelsius}°C Aman",
                progressFraction = (uiState.batteryTempCelsius - 25f) / 25f,
                accentColor = AmberGold,
                onClick = { viewModel.openToolModal(DefaultLauncherCatalog.all52Tools[2]) },
                modifier = Modifier.weight(1f)
            )
            AnimatedCircularRingGauge(
                title = "Storage Free",
                primaryValue = "${uiState.storageFreeGb.roundToInt()}G",
                subValue = "${uiState.storageFreeGb} GB Free",
                progressFraction = 0.72f,
                accentColor = AmethystPurple,
                onClick = { viewModel.openToolModal(DefaultLauncherCatalog.all52Tools[3]) },
                modifier = Modifier.weight(1f)
            )
        }

        // (c) 2x2 Grid of compact 16.dp rounded Bento Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Card 1: Tuning JVM Preset
            GlassCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 16.dp,
                contentPadding = PaddingValues(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Memory,
                                contentDescription = "Tuning JVM Preset",
                                tint = AmberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Tuning JVM Preset",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        Text(
                            text = "${uiState.ramAllocationMb} MB",
                            style = MaterialTheme.typography.labelMedium.copy(color = AmberGold)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        uiState.jvmPresets.forEach { preset ->
                            val isSelected = preset.id == uiState.selectedJvmPresetId
                            val shortTitle = when (preset.id) {
                                "low_end_gc" -> "Low-End"
                                "balanced_g1" -> "Balanced"
                                "extreme_zgc" -> "ZGC FPS"
                                else -> "Aikar's"
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) PureWhite else Color.White.copy(alpha = 0.06f),
                                        CircleShape
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) PureWhite else Color.White.copy(alpha = 0.12f),
                                        CircleShape
                                    )
                                    .clickable { viewModel.selectJvmPreset(preset) }
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = shortTitle,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) DarkInk else TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.5.sp
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    val activePreset = uiState.jvmPresets.find { it.id == uiState.selectedJvmPresetId }
                        ?: uiState.jvmPresets[2]
                    Text(
                        text = "${activePreset.subtitle} (${activePreset.estFpsBoost})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 9.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Card 2: Status Server & Ping Radar (mc.hypixel.net + Quick Connect)
            GlassCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 16.dp,
                contentPadding = PaddingValues(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Dns,
                                contentDescription = "Status Server",
                                tint = CyberCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Status Server & Ping Radar",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "${uiState.livePingMs}ms",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = uiState.selectedServerHost,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = uiState.serverOnlinePlayers,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 9.sp
                                )
                            )
                        }

                        PingRadarGraphCanvas(
                            pingHistory = uiState.hypixelPingHistory,
                            accentColor = CyberCyan,
                            modifier = Modifier
                                .width(78.dp)
                                .height(26.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(PureWhite, CircleShape)
                                .clickable { viewModel.startPreLaunchSequence(uiState.selectedServerHost) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Join",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DarkInk,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Card 3: Pengatur Opsi Game (instant Fullbright 1000% switch & FOV slider)
            GlassCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 16.dp,
                contentPadding = PaddingValues(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = "Pengatur Opsi Game",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Pengatur Opsi Game",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Fullbright 1000%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (uiState.fullbrightEnabled) AmberGold else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                            Switch(
                                checked = uiState.fullbrightEnabled,
                                onCheckedChange = { viewModel.setFullbright(it) },
                                modifier = Modifier
                                    .height(20.dp)
                                    .testTag("fullbright_switch"),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DarkInk,
                                    checkedTrackColor = AmberGold,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "FOV: ${uiState.fovValue.roundToInt()}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.width(48.dp)
                        )
                        Slider(
                            value = uiState.fovValue,
                            onValueChange = { viewModel.setFovValue(it) },
                            valueRange = 30f..110f,
                            modifier = Modifier
                                .weight(1f)
                                .height(22.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = PureWhite,
                                activeTrackColor = CyberCyan,
                                inactiveTrackColor = Color.White.copy(alpha = 0.14f)
                            )
                        )
                    }
                }
            }

            // Card 4: Pembersih & Crash Doctor (one-click cache cleaner & AI crash log analyzer)
            GlassCard(
                modifier = Modifier.weight(1f),
                cornerRadius = 16.dp,
                contentPadding = PaddingValues(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CleaningServices,
                                contentDescription = "Pembersih & Crash Doctor",
                                tint = CrimsonRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Pembersih & Crash Doctor",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                        Text(
                            text = "+${uiState.cleanedCacheMb} MB",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = uiState.crashDoctorStatus,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 9.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(PureWhite, CircleShape)
                                .clickable { viewModel.runCleanCacheAndCrashDoctor() }
                                .padding(vertical = 4.dp)
                                .testTag("quick_clean_cache_button")
                        ) {
                            Text(
                                text = "Bersihkan Cache",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = DarkInk,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f), CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.16f), CircleShape)
                                .clickable { viewModel.setShowCrashDoctorModal(true) }
                                .padding(vertical = 4.dp)
                                .testTag("open_crash_doctor_button")
                        ) {
                            Text(
                                text = "Crash Doctor",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = PureWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
