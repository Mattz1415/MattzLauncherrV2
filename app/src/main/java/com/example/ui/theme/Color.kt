package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Smoky Charcoal-Slate & Ash-Grey Dark Glassmorphism Palette ("Abu-Abu Dark Glassmorphism Vibes")
val OledBlack = Color(0xFF08090D)
val DeepNavyGlow = Color(0xFF1A1F2B)
val MidnightSlateGlow = Color(0xFF232938)
val AshMistGlow = Color(0xFF2D3548)

val GlassSurfaceTop = Color(0xE6202531)     // Frosted Ash-Charcoal Top
val GlassSurfaceMid = Color(0xE6171B24)     // Smoky Slate Mid
val GlassSurfaceBottom = Color(0xF210131A)  // Deep Gunmetal Bottom
val GlassIslandBg = Color(0xEB151922)       // Floating Frosted Slate Island
val GlassPillSurface = Color(0xFF252B39)    // Gunmetal Grey Pill Surface

val GlassBorderSubtle = Color(0x29FFFFFF)   // Crisp frosted edge (~16% white)
val GlassBorderSpecular = Color(0x47FFFFFF) // Top-left specular glass highlight (~28% white)
val GlassBorderBottom = Color(0x0FFFFFFF)   // Bottom-right subtle glass rim (~6% white)
val GlassBorderBright = Color(0x59FFFFFF)

val PureWhite = Color(0xFFFFFFFF)
val PlatinumSilver = Color(0xFFE2E8F0)
val AshGreyAccent = Color(0xFFCBD5E1)
val DarkInk = Color(0xFF090A0F)
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFFA1ADC2)
val TextMuted = Color(0xFF64748B)

val AmberGold = Color(0xFFF59E0B)
val CyberCyan = Color(0xFF38BDF8)
val EmeraldGreen = Color(0xFF10B981)
val CrimsonRed = Color(0xFFF43F5E)
val AmethystPurple = Color(0xFFA855F7)

enum class AccentThemeOption(
    val id: String,
    val label: String,
    val primaryColor: Color,
    val secondaryGlow: Color
) {
    PLATINUM_ASH("PLATINUM_ASH", "Abu-Abu Glass", PlatinumSilver, Color(0xFF64748B)),
    WHITE("WHITE", "Pure White", PureWhite, CyberCyan),
    CYBER_CYAN("CYBER_CYAN", "Cyber Cyan", CyberCyan, Color(0xFF0284C7)),
    AMBER_GOLD("AMBER_GOLD", "Amber Gold", AmberGold, Color(0xFFD97706)),
    EMERALD("EMERALD", "Emerald Neon", EmeraldGreen, Color(0xFF059669)),
    CRIMSON("CRIMSON", "Crimson Pulse", CrimsonRed, Color(0xFFE11D48)),
    AMETHYST("AMETHYST", "Amethyst Void", AmethystPurple, Color(0xFF7C3AED));

    companion object {
        fun fromId(id: String): AccentThemeOption =
            entries.find { it.id == id } ?: PLATINUM_ASH
    }
}
