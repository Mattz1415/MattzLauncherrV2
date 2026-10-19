package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CrosshairConfig
import com.example.model.VirtualMouseConfig
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AshMistGlow
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DeepNavyGlow
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GlassBorderBottom
import com.example.ui.theme.GlassBorderSpecular
import com.example.ui.theme.GlassSurfaceBottom
import com.example.ui.theme.GlassSurfaceMid
import com.example.ui.theme.GlassSurfaceTop
import com.example.ui.theme.LocalLauncherAccent
import com.example.ui.theme.MidnightSlateGlow
import com.example.ui.theme.OledBlack
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AnimatedCyberMatrixBackground(
    modifier: Modifier = Modifier
) {
    val accent = LocalLauncherAccent.current
    val infiniteTransition = rememberInfiniteTransition(label = "ash_glass_matrix_bg")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_phase"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0C0E14),
                        OledBlack,
                        Color(0xFF0A0C12)
                    )
                )
            )
    ) {
        val w = size.width
        val h = size.height

        // Top-left Smoky Ash-Slate ambient glow
        val glow1Center = Offset(
            x = w * (0.18f + 0.08f * pulsePhase),
            y = h * (0.24f + 0.05f * (1f - pulsePhase))
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AshMistGlow.copy(alpha = 0.65f),
                    DeepNavyGlow.copy(alpha = 0.38f),
                    Color.Transparent
                ),
                center = glow1Center,
                radius = w * 0.56f
            ),
            radius = w * 0.56f,
            center = glow1Center
        )

        // Bottom-right Gunmetal Slate ambient glow
        val glow2Center = Offset(
            x = w * (0.80f - 0.07f * pulsePhase),
            y = h * (0.76f - 0.05f * pulsePhase)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    MidnightSlateGlow.copy(alpha = 0.72f),
                    accent.primaryColor.copy(alpha = 0.07f),
                    Color.Transparent
                ),
                center = glow2Center,
                radius = w * 0.52f
            ),
            radius = w * 0.52f,
            center = glow2Center
        )

        // Center-top soft frosted glass sheen beam
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.055f + 0.02f * pulsePhase),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, -h * 0.1f),
                radius = w * 0.45f
            ),
            radius = w * 0.45f,
            center = Offset(w * 0.5f, -h * 0.1f)
        )

        // Precision Ash-Grey Micro Dot-Matrix Grid
        val spacing = 22.dp.toPx()
        val dotRadius = 0.9.dp.toPx()
        val cols = (w / spacing).toInt() + 1
        val rows = (h / spacing).toInt() + 1
        val dotColor = Color.White.copy(alpha = 0.055f + 0.02f * pulsePhase)

        for (c in 0..cols) {
            val x = c * spacing
            for (r in 0..rows) {
                val y = r * spacing
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(x, y)
                )
            }
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    borderColor: Color = GlassBorderSpecular,
    topColor: Color = GlassSurfaceTop,
    bottomColor: Color = GlassSurfaceBottom,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val baseModifier = modifier
        .shadow(
            elevation = 8.dp,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = 0.55f),
            spotColor = Color.Black.copy(alpha = 0.65f)
        )
        .clip(shape)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    topColor,
                    GlassSurfaceMid,
                    bottomColor
                )
            ),
            shape = shape
        )
        .drawWithContent {
            drawContent()
            // Top frosted glass inner specular highlight line
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.15f),
                        Color.Transparent
                    )
                ),
                start = Offset(size.width * 0.12f, 1.dp.toPx()),
                end = Offset(size.width * 0.88f, 1.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )
        }
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    borderColor,
                    Color.White.copy(alpha = 0.12f),
                    GlassBorderBottom
                )
            ),
            shape = shape
        )

    val interactiveModifier = if (onClick != null) {
        baseModifier.clickable(onClick = onClick)
    } else {
        baseModifier
    }

    Box(
        modifier = interactiveModifier.padding(contentPadding),
        content = content
    )
}

@Composable
fun SolidWhitePillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    customCanvasLeading: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
        label = "white_pill_scale"
    )
    val accent = LocalLauncherAccent.current

    Row(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                ambientColor = accent.primaryColor.copy(alpha = 0.4f),
                spotColor = PureWhite.copy(alpha = 0.5f)
            )
            .clip(CircleShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PureWhite, Color(0xFFE2E8F0))
                ),
                shape = CircleShape
            )
            .border(1.dp, PureWhite, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (customCanvasLeading != null) {
            customCanvasLeading()
            Spacer(modifier = Modifier.width(6.dp))
        } else if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = text,
                tint = DarkInk,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = DarkInk,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AmberGoldPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
        label = "amber_pill_scale"
    )

    Row(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = AmberGold.copy(alpha = 0.45f),
                spotColor = AmberGold.copy(alpha = 0.55f)
            )
            .clip(CircleShape)
            .background(AmberGold, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = text,
                tint = DarkInk,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = DarkInk,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DarkGlassPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    borderColor: Color = Color.White.copy(alpha = 0.22f),
    textColor: Color = TextPrimary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
        label = "glass_pill_scale"
    )

    Row(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF282F3E), Color(0xFF1A1F29))
                ),
                shape = CircleShape
            )
            .border(1.dp, borderColor, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = text,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                color = textColor,
                fontSize = 11.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AnimatedCircularRingGauge(
    title: String,
    primaryValue: String,
    subValue: String,
    progressFraction: Float,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gauge_pulse_$title")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    val animatedSweep by animateFloatAsState(
        targetValue = (progressFraction.coerceIn(0.05f, 1f)) * 270f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
        label = "sweep_angle"
    )

    GlassCard(
        modifier = modifier,
        cornerRadius = 18.dp,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(42.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 4.dp.toPx()
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)

                    // Background track arc (Ash Grey translucent)
                    drawArc(
                        color = Color.White.copy(alpha = 0.12f),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Pulsing outer halo arc
                    drawArc(
                        color = accentColor.copy(alpha = pulseAlpha * 0.28f),
                        startAngle = 135f,
                        sweepAngle = animatedSweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth * 1.9f, cap = StrokeCap.Round)
                    )

                    // Foreground SweepGradient arc
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.55f),
                                accentColor,
                                PureWhite,
                                accentColor
                            )
                        ),
                        startAngle = 135f,
                        sweepAngle = animatedSweep,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = primaryValue,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subValue,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun DualRingLoadingSpinner(
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    primaryColor: Color = CyberCyan,
    secondaryColor: Color = PureWhite
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dual_ring_spinner")
    val outerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1350, easing = LinearEasing)
        ),
        label = "outer_rot"
    )
    val innerRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 950, easing = LinearEasing)
        ),
        label = "inner_rot"
    )

    Canvas(modifier = modifier.size(size)) {
        val outerStroke = 3.dp.toPx()
        val innerStroke = 2.2.dp.toPx()

        // Outer clockwise ring
        rotate(degrees = outerRotation) {
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Color.Transparent, primaryColor.copy(alpha = 0.4f), primaryColor, PureWhite)
                ),
                startAngle = 0f,
                sweepAngle = 240f,
                useCenter = false,
                topLeft = Offset(outerStroke, outerStroke),
                size = Size(this.size.width - outerStroke * 2, this.size.height - outerStroke * 2),
                style = Stroke(width = outerStroke, cap = StrokeCap.Round)
            )
        }

        // Inner counter-rotating ring
        val inset = this.size.width * 0.22f
        rotate(degrees = innerRotation) {
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Color.Transparent, secondaryColor.copy(alpha = 0.3f), secondaryColor)
                ),
                startAngle = 90f,
                sweepAngle = 200f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(this.size.width - inset * 2, this.size.height - inset * 2),
                style = Stroke(width = innerStroke, cap = StrokeCap.Round)
            )
        }

        // Core center dot
        drawCircle(
            color = primaryColor,
            radius = 2.5.dp.toPx(),
            center = center
        )
    }
}

@Composable
fun VectorSparkleIcon(
    modifier: Modifier = Modifier,
    color: Color = EmeraldGreen,
    size: Dp = 12.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            quadraticTo(w * 0.52f, h * 0.48f, w, h * 0.5f)
            quadraticTo(w * 0.52f, h * 0.52f, w * 0.5f, h)
            quadraticTo(w * 0.48f, h * 0.52f, 0f, h * 0.5f)
            quadraticTo(w * 0.48f, h * 0.48f, w * 0.5f, 0f)
            close()
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun MattzMonogramButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = LocalLauncherAccent.current
    val infiniteTransition = rememberInfiniteTransition(label = "monogram_glow")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4800, easing = LinearEasing)
        ),
        label = "monogram_angle"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.verticalGradient(listOf(GlassSurfaceTop, GlassSurfaceBottom)),
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = 1.5.dp.toPx()
            rotate(degrees = angle) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.12f),
                            accent.primaryColor,
                            PureWhite,
                            Color.White.copy(alpha = 0.12f)
                        )
                    ),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(strokePx / 2f, strokePx / 2f),
                    size = Size(size.width - strokePx, size.height - strokePx),
                    style = Stroke(width = strokePx)
                )
            }

            // Custom Canvas "M" vector path (1.5dp stroke)
            val w = size.width
            val h = size.height
            val mPath = Path().apply {
                moveTo(w * 0.28f, h * 0.68f)
                lineTo(w * 0.28f, h * 0.32f)
                lineTo(w * 0.50f, h * 0.54f)
                lineTo(w * 0.72f, h * 0.32f)
                lineTo(w * 0.72f, h * 0.68f)
            }
            drawPath(
                path = mPath,
                color = PureWhite,
                style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun Minecraft2DAvatarHead(
    primaryColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp
) {
    Canvas(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(6.dp))
    ) {
        val w = this.size.width
        val h = this.size.height

        // Base skin tone
        drawRect(color = Color(0xFF1E293B))

        // Hair / helmet top layer
        drawRect(
            color = primaryColor,
            topLeft = Offset(0f, 0f),
            size = Size(w, h * 0.36f)
        )
        drawRect(
            color = primaryColor.copy(alpha = 0.8f),
            topLeft = Offset(0f, h * 0.36f),
            size = Size(w * 0.16f, h * 0.25f)
        )
        drawRect(
            color = primaryColor.copy(alpha = 0.8f),
            topLeft = Offset(w * 0.84f, h * 0.36f),
            size = Size(w * 0.16f, h * 0.25f)
        )

        // Glowing Cyber Visor Eyes
        drawRect(
            color = PureWhite,
            topLeft = Offset(w * 0.16f, h * 0.48f),
            size = Size(w * 0.24f, h * 0.14f)
        )
        drawRect(
            color = accentColor,
            topLeft = Offset(w * 0.26f, h * 0.48f),
            size = Size(w * 0.14f, h * 0.14f)
        )

        drawRect(
            color = PureWhite,
            topLeft = Offset(w * 0.60f, h * 0.48f),
            size = Size(w * 0.24f, h * 0.14f)
        )
        drawRect(
            color = accentColor,
            topLeft = Offset(w * 0.60f, h * 0.48f),
            size = Size(w * 0.14f, h * 0.14f)
        )

        // Mouth / Mask detail
        drawRect(
            color = Color(0xFF0F172A),
            topLeft = Offset(w * 0.30f, h * 0.74f),
            size = Size(w * 0.40f, h * 0.12f)
        )
    }
}

@Composable
fun PingRadarGraphCanvas(
    pingHistory: List<Int>,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        if (pingHistory.size < 2) return@Canvas

        val minPing = 10f
        val maxPing = 45f
        val stepX = w / (pingHistory.size - 1).coerceAtLeast(1)

        val linePath = Path()
        val fillPath = Path()

        pingHistory.forEachIndexed { idx, ping ->
            val x = idx * stepX
            val normalized = ((ping - minPing) / (maxPing - minPing)).coerceIn(0f, 1f)
            val y = h - (normalized * (h * 0.75f) + h * 0.12f)
            if (idx == 0) {
                linePath.moveTo(x, y)
                fillPath.moveTo(x, h)
                fillPath.lineTo(x, y)
            } else {
                linePath.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            if (idx == pingHistory.lastIndex) {
                fillPath.lineTo(x, h)
                fillPath.close()
                drawCircle(
                    color = accentColor,
                    radius = 3.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(accentColor.copy(alpha = 0.32f), Color.Transparent)
            )
        )
        drawPath(
            path = linePath,
            color = accentColor,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun CustomCrosshairCanvas(
    config: CrosshairConfig,
    modifier: Modifier = Modifier
) {
    val accent = LocalLauncherAccent.current
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val len = config.sizePx.dp.toPx()
        val gap = config.gapPx.dp.toPx()
        val stroke = config.thicknessPx.dp.toPx()

        // Subtle target grid
        drawCircle(
            color = Color.White.copy(alpha = 0.10f),
            radius = size.minDimension * 0.38f,
            center = center,
            style = Stroke(width = 1.dp.toPx())
        )

        when (config.styleIndex) {
            0 -> {
                // Cyber Plus
                drawLine(accent.primaryColor, Offset(cx - gap - len, cy), Offset(cx - gap, cy), strokeWidth = stroke, cap = StrokeCap.Round)
                drawLine(accent.primaryColor, Offset(cx + gap, cy), Offset(cx + gap + len, cy), strokeWidth = stroke, cap = StrokeCap.Round)
                drawLine(accent.primaryColor, Offset(cx, cy - gap - len), Offset(cx, cy - gap), strokeWidth = stroke, cap = StrokeCap.Round)
                drawLine(accent.primaryColor, Offset(cx, cy + gap), Offset(cx, cy + gap + len), strokeWidth = stroke, cap = StrokeCap.Round)
            }
            1 -> {
                // Dot Ring
                drawCircle(
                    color = accent.primaryColor,
                    radius = len + gap,
                    center = center,
                    style = Stroke(width = stroke)
                )
            }
            else -> {
                // Chevron Tactical
                val path = Path().apply {
                    moveTo(cx - len, cy + len * 0.6f)
                    lineTo(cx, cy - gap)
                    lineTo(cx + len, cy + len * 0.6f)
                }
                drawPath(path, color = accent.primaryColor, style = Stroke(width = stroke, cap = StrokeCap.Round))
            }
        }

        if (config.showCenterDot) {
            drawCircle(
                color = PureWhite,
                radius = (stroke * 0.85f).coerceAtLeast(2f),
                center = center
            )
        }
    }
}

@Composable
fun VirtualMouseCursorCanvas(
    config: VirtualMouseConfig,
    modifier: Modifier = Modifier
) {
    val accent = LocalLauncherAccent.current
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val s = 18.dp.toPx() * config.cursorScale

        val cursorPath = Path().apply {
            when (config.cursorStyle) {
                0 -> {
                    // Precision Spear
                    moveTo(cx - s * 0.4f, cy - s * 0.5f)
                    lineTo(cx + s * 0.5f, cy)
                    lineTo(cx - s * 0.1f, cy + s * 0.12f)
                    lineTo(cx - s * 0.4f, cy + s * 0.55f)
                    close()
                }
                1 -> {
                    // Neon Diamond
                    moveTo(cx, cy - s * 0.55f)
                    lineTo(cx + s * 0.45f, cy)
                    lineTo(cx, cy + s * 0.55f)
                    lineTo(cx - s * 0.45f, cy)
                    close()
                }
                else -> {
                    // Minimal Crosshair Cursor
                    moveTo(cx - s * 0.5f, cy)
                    lineTo(cx + s * 0.5f, cy)
                    moveTo(cx, cy - s * 0.5f)
                    lineTo(cx, cy + s * 0.5f)
                }
            }
        }
        drawPath(
            path = cursorPath,
            color = accent.primaryColor.copy(alpha = 0.3f)
        )
        drawPath(
            path = cursorPath,
            color = PureWhite,
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun GlassModalDialog(
    visible: Boolean,
    title: String,
    subtitle: String? = null,
    onDismiss: () -> Unit,
    maxWidth: Dp = 580.dp,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(180)) + scaleIn(
            initialScale = 0.88f,
            animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f)
        ),
        exit = fadeOut(tween(140)) + scaleOut(
            targetScale = 0.92f,
            animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xD9080A0F))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            val accent = LocalLauncherAccent.current
            GlassCard(
                modifier = Modifier
                    .widthIn(max = maxWidth)
                    .fillMaxWidth(0.90f)
                    .padding(vertical = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    ),
                cornerRadius = 24.dp,
                borderColor = accent.primaryColor.copy(alpha = 0.40f),
                contentPadding = PaddingValues(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            if (!subtitle.isNullOrBlank()) {
                                Text(
                                    text = subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                            }
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.10f), CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape)
                                .clickable(onClick = onDismiss)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Tutup Modal",
                                tint = TextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    content()
                }
            }
        }
    }
}
