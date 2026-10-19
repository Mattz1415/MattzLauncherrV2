package com.example.service

import androidx.compose.ui.graphics.Color
import com.example.model.ModItem
import com.example.model.ModPlatform
import com.example.model.ModsSubTab
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PlatinumSilver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

object ModManagerService {

    fun scanInstalledMods(gameDir: File): List<ModItem> {
        val modsDir = File(gameDir, "mods")
        if (!modsDir.exists() || !modsDir.isDirectory) return emptyList()

        val list = mutableListOf<ModItem>()
        val colors = listOf(CyberCyan, EmeraldGreen, AmberGold, AmethystPurple, PlatinumSilver)

        modsDir.listFiles()?.filter { it.isFile && (it.name.endsWith(".jar") || it.name.endsWith(".jar.disabled")) }?.forEachIndexed { index, file ->
            val isEnabled = !file.name.endsWith(".disabled")
            val baseName = file.name.removeSuffix(".disabled").removeSuffix(".jar")
            val sizeMb = file.length() / (1024f * 1024f)
            val sizeStr = if (sizeMb >= 1f) String.format(Locale.US, "%.1f MB", sizeMb) else "${file.length() / 1024} KB"

            val category = when {
                baseName.contains("fabric", ignoreCase = true) || baseName.contains("api", ignoreCase = true) -> ModsSubTab.INSTALLED
                baseName.contains("sodium", ignoreCase = true) || baseName.contains("iris", ignoreCase = true) -> ModsSubTab.SHADER_PACKS
                else -> ModsSubTab.INSTALLED
            }

            list.add(
                ModItem(
                    id = "mod_${file.name.hashCode()}",
                    name = cleanModName(baseName),
                    author = "Local Modfile",
                    summary = "Berkas mod terpasang di direktori .minecraft/mods",
                    category = category,
                    platform = ModPlatform.ALL,
                    mcVersion = "Auto",
                    fileSize = sizeStr,
                    downloads = "Lokal",
                    fileNameJar = file.name,
                    isInstalled = true,
                    isEnabled = isEnabled,
                    accentColor = colors[index % colors.size]
                )
            )
        }

        return list
    }

    fun toggleMod(gameDir: File, modItem: ModItem): Boolean {
        val modsDir = File(gameDir, "mods")
        val currentFile = File(modsDir, modItem.fileNameJar)
        if (!currentFile.exists()) return false

        val targetName = if (modItem.isEnabled) {
            if (modItem.fileNameJar.endsWith(".disabled")) modItem.fileNameJar else "${modItem.fileNameJar}.disabled"
        } else {
            modItem.fileNameJar.removeSuffix(".disabled")
        }

        val targetFile = File(modsDir, targetName)
        return currentFile.renameTo(targetFile)
    }

    fun deleteMod(gameDir: File, modItem: ModItem): Boolean {
        val modsDir = File(gameDir, "mods")
        val file = File(modsDir, modItem.fileNameJar)
        return if (file.exists()) file.delete() else false
    }

    suspend fun searchModrinth(query: String, mcVersion: String? = null): List<ModItem> = withContext(Dispatchers.IO) {
        val cleanQuery = query.trim()
        val encodedQuery = URLEncoder.encode(cleanQuery, "UTF-8")
        val urlStr = "https://api.modrinth.com/v2/search?query=$encodedQuery&limit=15&facets=[[\"project_type:mod\"]]"

        return@withContext try {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 6000
            conn.readTimeout = 8000
            conn.setRequestProperty("User-Agent", "MattzLauncher/2.0")
            conn.connect()

            val jsonStr = conn.inputStream.bufferedReader().use { it.readText() }
            val root = JSONObject(jsonStr)
            val hits = root.getJSONArray("hits")

            val list = mutableListOf<ModItem>()
            val colors = listOf(CyberCyan, EmeraldGreen, AmberGold, AmethystPurple)

            for (i in 0 until hits.length()) {
                val hit = hits.getJSONObject(i)
                val title = hit.getString("title")
                val slug = hit.getString("slug")
                val author = hit.optString("author", "Community")
                val desc = hit.optString("description", "Minecraft Fabric / Forge Mod")
                val downloads = hit.optInt("downloads", 0)
                val dStr = if (downloads >= 1_000_000) String.format("%.1fM", downloads / 1_000_000f) else "${downloads / 1000}K"

                list.add(
                    ModItem(
                        id = "mr_$slug",
                        name = title,
                        author = author,
                        summary = desc,
                        category = ModsSubTab.DOWNLOAD_MOD,
                        platform = ModPlatform.MODRINTH,
                        mcVersion = mcVersion ?: "1.21.x",
                        fileSize = "Modrinth",
                        downloads = dStr,
                        fileNameJar = "$slug.jar",
                        isInstalled = false,
                        isEnabled = true,
                        accentColor = colors[i % colors.size]
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun downloadModrinthMod(
        gameDir: File,
        mod: ModItem,
        onProgress: (Float) -> Unit = {}
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val modsDir = File(gameDir, "mods")
            if (!modsDir.exists()) modsDir.mkdirs()

            val projectId = mod.id.removePrefix("mr_")
            val projectVersionUrl = "https://api.modrinth.com/v2/project/$projectId/version"

            val url = URL(projectVersionUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 6000
            conn.readTimeout = 8000
            conn.setRequestProperty("User-Agent", "MattzLauncher/2.0")
            conn.connect()

            val versionsJson = conn.inputStream.bufferedReader().use { it.readText() }
            val versionsArray = org.json.JSONArray(versionsJson)
            if (versionsArray.length() == 0) return@withContext false

            val latestVersion = versionsArray.getJSONObject(0)
            val filesArray = latestVersion.getJSONArray("files")
            if (filesArray.length() == 0) return@withContext false

            val fileObj = filesArray.getJSONObject(0)
            val downloadUrl = fileObj.getString("url")
            val fileName = fileObj.getString("filename")

            val targetFile = File(modsDir, fileName)

            val dlUrl = URL(downloadUrl)
            val dlConn = dlUrl.openConnection() as HttpURLConnection
            dlConn.connectTimeout = 10000
            dlConn.readTimeout = 15000
            dlConn.setRequestProperty("User-Agent", "MattzLauncher/2.0")
            dlConn.connect()

            val length = dlConn.contentLengthLong.coerceAtLeast(1L)
            var totalRead = 0L

            dlConn.inputStream.use { input ->
                FileOutputStream(targetFile).use { output ->
                    val buffer = ByteArray(8192)
                    var read = input.read(buffer)
                    while (read >= 0) {
                        output.write(buffer, 0, read)
                        totalRead += read
                        onProgress((totalRead.toFloat() / length).coerceIn(0f, 1f))
                        read = input.read(buffer)
                    }
                }
            }

            return@withContext true
        } catch (_: Exception) {
            return@withContext false
        }
    }

    private fun cleanModName(raw: String): String {
        return raw.replace("-", " ")
            .replace("_", " ")
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }
}
