package com.example.service

import java.io.File

object OptionsTxtService {

    fun readOptions(gameDir: File): Map<String, String> {
        val optionsFile = File(gameDir, "options.txt")
        if (!optionsFile.exists()) return emptyMap()

        val map = mutableMapOf<String, String>()
        try {
            optionsFile.forEachLine { line ->
                val trimmed = line.trim()
                if (trimmed.contains(":") && !trimmed.startsWith("#")) {
                    val parts = trimmed.split(":", limit = 2)
                    map[parts[0].trim()] = parts[1].trim()
                }
            }
        } catch (_: Exception) {}
        return map
    }

    fun setOption(gameDir: File, key: String, value: String) {
        val optionsFile = File(gameDir, "options.txt")
        val lines = if (optionsFile.exists()) optionsFile.readLines().toMutableList() else mutableListOf()

        var replaced = false
        for (i in lines.indices) {
            val line = lines[i].trim()
            if (line.startsWith("$key:")) {
                lines[i] = "$key:$value"
                replaced = true
                break
            }
        }
        if (!replaced) {
            lines.add("$key:$value")
        }

        try {
            optionsFile.writeText(lines.joinToString("\n"))
        } catch (_: Exception) {}
    }

    fun isFullbright(gameDir: File): Boolean {
        val opts = readOptions(gameDir)
        val gamma = opts["gamma"]?.toDoubleOrNull() ?: 1.0
        return gamma >= 5.0
    }

    fun setFullbright(gameDir: File, enabled: Boolean) {
        setOption(gameDir, "gamma", if (enabled) "10.0" else "1.0")
    }

    fun setFov(gameDir: File, fov: Float) {
        // Minecraft vanilla maps FOV 70 -> 0.0, 110 -> 1.0
        val mcFov = ((fov - 70f) / 40f).coerceIn(0f, 1f)
        setOption(gameDir, "fov", String.format(java.util.Locale.US, "%.2f", mcFov))
    }

    fun setRenderDistance(gameDir: File, chunks: Int) {
        setOption(gameDir, "renderDistance", chunks.coerceIn(2, 32).toString())
    }

    fun setVsync(gameDir: File, enabled: Boolean) {
        setOption(gameDir, "enableVsync", enabled.toString())
    }

    fun setGuiScale(gameDir: File, scale: Int) {
        setOption(gameDir, "guiScale", scale.coerceIn(0, 5).toString())
    }

    fun setClouds(gameDir: File, enabled: Boolean) {
        setOption(gameDir, "renderClouds", if (enabled) "true" else "false")
    }

    fun setSmoothLighting(gameDir: File, enabled: Boolean) {
        setOption(gameDir, "ao", if (enabled) "2" else "0")
    }

    fun setEntityShadows(gameDir: File, enabled: Boolean) {
        setOption(gameDir, "entityShadows", enabled.toString())
    }

    fun setAutoJump(gameDir: File, enabled: Boolean) {
        setOption(gameDir, "autoJump", enabled.toString())
    }
}
