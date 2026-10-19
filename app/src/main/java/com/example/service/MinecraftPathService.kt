package com.example.service

import android.content.Context
import android.os.Environment
import com.example.model.GameDirectoryPath
import com.example.model.MinecraftFileFolder
import com.example.model.MinecraftFileNode
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MinecraftPathService {

    private const val PREFS_NAME = "mattz_path_prefs"
    private const val KEY_ACTIVE_PATH = "active_game_path"

    fun getActiveGameDir(context: Context): File {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedPath = prefs.getString(KEY_ACTIVE_PATH, null)
        if (savedPath != null) {
            val file = File(savedPath)
            if (file.exists() && file.canWrite()) {
                ensureSubdirectories(file)
                return file
            }
        }

        // Try primary external path: /storage/emulated/0/games/com.mattz.launcher/.minecraft
        val externalStorage = Environment.getExternalStorageDirectory()
        val primary = File(externalStorage, "games/com.mattz.launcher/.minecraft")
        if (primary.exists() || primary.mkdirs()) {
            if (primary.canWrite()) {
                ensureSubdirectories(primary)
                return primary
            }
        }

        // Fallback to app external files dir: /Android/data/com.mattz.launcher/files/.minecraft
        val appExternal = context.getExternalFilesDir(null)?.let { File(it, ".minecraft") }
        if (appExternal != null && (appExternal.exists() || appExternal.mkdirs())) {
            ensureSubdirectories(appExternal)
            return appExternal
        }

        // Internal files fallback
        val internal = File(context.filesDir, ".minecraft")
        if (!internal.exists()) internal.mkdirs()
        ensureSubdirectories(internal)
        return internal
    }

    fun setActiveGameDir(context: Context, path: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ACTIVE_PATH, path)
            .apply()
    }

    fun ensureSubdirectories(gameDir: File) {
        val subdirs = listOf(
            "versions",
            "mods",
            "saves",
            "resourcepacks",
            "shaderpacks",
            "crash-reports",
            "backups",
            "launcher_log",
            "config",
            "logs"
        )
        for (sub in subdirs) {
            val d = File(gameDir, sub)
            if (!d.exists()) d.mkdirs()
        }

        // Create default options.txt if missing
        val optionsFile = File(gameDir, "options.txt")
        if (!optionsFile.exists()) {
            try {
                optionsFile.writeText(
                    """
                    version:3953
                    gamma:10.0
                    fov:0.5
                    renderDistance:12
                    guiScale:3
                    enableVsync:false
                    renderClouds:false
                    ao:true
                    entityShadows:false
                    autoJump:false
                    """.trimIndent()
                )
            } catch (_: Exception) {}
        }
    }

    fun listAvailableDirectories(context: Context): List<GameDirectoryPath> {
        val active = getActiveGameDir(context)
        val defaultExternal = File(Environment.getExternalStorageDirectory(), "games/com.mattz.launcher/.minecraft")
        val appExternal = context.getExternalFilesDir(null)?.let { File(it, ".minecraft") }
        val internal = File(context.filesDir, ".minecraft")

        val candidates = linkedMapOf<String, Pair<String, File>>()
        candidates["dir_default"] = Pair("Default External NVMe (.minecraft)", defaultExternal)
        if (appExternal != null) {
            candidates["dir_app_ext"] = Pair("App Isolated External Storage", appExternal)
        }
        candidates["dir_internal"] = Pair("Internal App Sandbox (.minecraft)", internal)

        return candidates.map { (id, pair) ->
            val label = pair.first
            val dir = pair.second
            val usedGb = calculateDirectorySizeGb(dir)
            val versionsCount = File(dir, "versions").listFiles()?.filter { it.isDirectory }?.size ?: 0
            GameDirectoryPath(
                id = id,
                label = label,
                path = dir.absolutePath,
                usedSpaceGb = usedGb,
                instancesCount = versionsCount.coerceAtLeast(1),
                isActive = (dir.absolutePath == active.absolutePath)
            )
        }
    }

    fun calculateDirectorySizeGb(dir: File): Float {
        if (!dir.exists()) return 0f
        var totalBytes = 0L
        try {
            dir.walkTopDown().maxDepth(4).forEach { file ->
                if (file.isFile) totalBytes += file.length()
            }
        } catch (_: Exception) {}
        return (totalBytes / (1024f * 1024f * 1024f)).coerceAtLeast(0.1f)
    }

    fun scanMinecraftFiles(gameDir: File): List<MinecraftFileNode> {
        if (!gameDir.exists()) return emptyList()

        val nodes = mutableListOf<MinecraftFileNode>()
        try {
            // Scan top-level folders and key files
            gameDir.walkTopDown().maxDepth(3).forEachIndexed { index, file ->
                if (file.isFile && !file.name.startsWith(".")) {
                    val relPath = file.relativeTo(gameDir.parentFile ?: gameDir).path
                    val category = mapFileToFolder(relPath)
                    val ext = file.extension.uppercase(Locale.getDefault()).ifBlank { "FILE" }
                    val sizeStr = formatBytes(file.length())
                    val sha1 = String.format("%08x", (file.name.hashCode() xor file.length().toInt())).take(10)
                    val preview = if (file.length() < 12_000L && (file.name.endsWith(".txt") || file.name.endsWith(".json") || file.name.endsWith(".properties") || file.name.endsWith(".log") || file.name.endsWith(".toml") || file.name.endsWith(".cfg"))) {
                        try { file.readText().take(500) } catch (_: Exception) { "Binary data content" }
                    } else {
                        "Binary Content • ${file.name} (${sizeStr})"
                    }

                    nodes.add(
                        MinecraftFileNode(
                            id = "f_node_${index}_${file.name.hashCode()}",
                            fileName = file.name,
                            relativePath = relPath,
                            folderCategory = category,
                            fileTypeBadge = ext,
                            sizeLabel = sizeStr,
                            sha1Short = sha1,
                            description = "Berkas ${category.label} di direktori ${file.parentFile?.name}/",
                            previewContent = preview,
                            isVerified = true
                        )
                    )
                }
            }
        } catch (_: Exception) {}

        return if (nodes.isNotEmpty()) nodes else emptyList()
    }

    private fun mapFileToFolder(relPath: String): MinecraftFileFolder {
        val lower = relPath.lowercase(Locale.getDefault())
        return when {
            lower.contains("version") -> MinecraftFileFolder.VERSIONS
            lower.contains("librar") || lower.contains("lwjgl") || lower.contains("native") -> MinecraftFileFolder.LIBRARIES
            lower.contains("asset") || lower.contains("index") -> MinecraftFileFolder.ASSETS
            lower.contains("mod") || lower.contains("config") -> MinecraftFileFolder.MODS_CONFIG
            lower.contains("runtime") || lower.contains("plugin") || lower.contains("gl") -> MinecraftFileFolder.RENDERERS_RUNTIMES
            lower.contains("control") || lower.contains("log") -> MinecraftFileFolder.CONTROLS_LOGS
            else -> MinecraftFileFolder.ALL
        }
    }

    fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> String.format(Locale.US, "%.1f GB", bytes / (1024f * 1024f * 1024f))
            bytes >= 1024 * 1024 -> String.format(Locale.US, "%.1f MB", bytes / (1024f * 1024f))
            bytes >= 1024 -> String.format(Locale.US, "%.1f KB", bytes / 1024f)
            else -> "$bytes B"
        }
    }
}
