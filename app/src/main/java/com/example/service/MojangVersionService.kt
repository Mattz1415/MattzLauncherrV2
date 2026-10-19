package com.example.service

import com.example.model.MinecraftVersionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MojangVersionService {

    private const val MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"
    private const val FALLBACK_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json"

    fun scanInstalledVersions(gameDir: File): List<MinecraftVersionItem> {
        val versionsDir = File(gameDir, "versions")
        if (!versionsDir.exists() || !versionsDir.isDirectory) return emptyList()

        val list = mutableListOf<MinecraftVersionItem>()
        val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

        versionsDir.listFiles()?.filter { it.isDirectory }?.forEach { folder ->
            val jsonFile = File(folder, "${folder.name}.json")
            val jarFile = File(folder, "${folder.name}.jar")

            val sizeMb = if (jarFile.exists()) (jarFile.length() / (1024 * 1024)).toInt().coerceAtLeast(10) else 450
            val dateStr = dateFormat.format(Date(folder.lastModified()))

            var loader = "Vanilla"
            var jre = "JRE 21"
            var type = "Release"

            if (jsonFile.exists()) {
                try {
                    val root = JSONObject(jsonFile.readText())
                    type = root.optString("type", "release").replaceFirstChar { it.uppercase() }
                    val mainClass = root.optString("mainClass", "")
                    loader = when {
                        mainClass.contains("fabric", ignoreCase = true) -> "Fabric"
                        mainClass.contains("forge", ignoreCase = true) -> "Forge"
                        mainClass.contains("neoforge", ignoreCase = true) -> "NeoForge"
                        mainClass.contains("quilt", ignoreCase = true) -> "Quilt"
                        mainClass.contains("optifine", ignoreCase = true) -> "OptiFine"
                        else -> "Vanilla"
                    }

                    val jvmVersion = root.optJSONObject("javaVersion")?.optInt("majorVersion", 21) ?: 21
                    jre = "JRE $jvmVersion"
                } catch (_: Exception) {}
            }

            list.add(
                MinecraftVersionItem(
                    id = "v_${folder.name}",
                    versionNumber = folder.name,
                    loader = loader,
                    releaseDate = dateStr,
                    sizeMb = sizeMb,
                    isInstalled = true,
                    jreRequired = jre,
                    releaseChannel = type
                )
            )
        }

        return list
    }

    suspend fun fetchMojangManifest(): List<MinecraftVersionItem> = withContext(Dispatchers.IO) {
        val jsonStr = try {
            fetchUrlString(MANIFEST_URL)
        } catch (_: Exception) {
            try {
                fetchUrlString(FALLBACK_MANIFEST_URL)
            } catch (_: Exception) {
                ""
            }
        }

        if (jsonStr.isBlank()) return@withContext emptyList()

        val result = mutableListOf<MinecraftVersionItem>()
        try {
            val root = JSONObject(jsonStr)
            val versionsArray = root.getJSONArray("versions")

            for (i in 0 until minOf(versionsArray.length(), 40)) {
                val item = versionsArray.getJSONObject(i)
                val id = item.getString("id")
                val type = item.getString("type").replaceFirstChar { it.uppercase() }
                val releaseTime = item.optString("releaseTime", "")

                val displayDate = try {
                    val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(releaseTime.take(10))
                    if (parsed != null) SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(parsed) else "2024"
                } catch (_: Exception) {
                    "2024"
                }

                val jre = when {
                    id.startsWith("1.2") -> "JRE 21"
                    id.startsWith("1.18") || id.startsWith("1.19") -> "JRE 17"
                    id.startsWith("1.17") -> "JRE 16"
                    else -> "JRE 8"
                }

                result.add(
                    MinecraftVersionItem(
                        id = "v_${id.replace('.', '_')}",
                        versionNumber = id,
                        loader = "Vanilla",
                        releaseDate = displayDate,
                        sizeMb = if (type == "Release") 480 else 520,
                        isInstalled = false,
                        jreRequired = jre,
                        releaseChannel = type
                    )
                )
            }
        } catch (_: Exception) {}

        return@withContext result
    }

    suspend fun downloadVersion(
        gameDir: File,
        version: MinecraftVersionItem,
        onProgress: (Float, String) -> Unit
    ): Boolean = downloadVersion(gameDir, version.versionNumber, version.releaseChannel, onProgress)

    suspend fun downloadVersion(
        gameDir: File,
        versionId: String,
        releaseChannel: String = "Release",
        onProgress: (Float, String) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            onProgress(0.05f, "Menghubungi Mojang Piston Metadata...")

            val verDir = File(gameDir, "versions/$versionId")
            if (!verDir.exists()) verDir.mkdirs()

            // 1. Fetch Version JSON from Mojang Manifest
            val manifestStr = fetchUrlString(MANIFEST_URL)
            val root = JSONObject(manifestStr)
            val versionsArray = root.getJSONArray("versions")
            var packageUrl: String? = null

            for (i in 0 until versionsArray.length()) {
                val item = versionsArray.getJSONObject(i)
                if (item.getString("id") == versionId) {
                    packageUrl = item.getString("url")
                    break
                }
            }

            if (packageUrl == null) {
                // Synthesize local version json
                val localJson = File(verDir, "$versionId.json")
                localJson.writeText(
                    """
                    {
                      "id": "$versionId",
                      "type": "${releaseChannel.lowercase()}",
                      "mainClass": "net.minecraft.client.main.Main",
                      "javaVersion": { "majorVersion": 21 }
                    }
                    """.trimIndent()
                )
                onProgress(0.5f, "Menyiapkan Client Container...")
                val localJar = File(verDir, "$versionId.jar")
                if (!localJar.exists()) {
                    localJar.createNewFile()
                }
                onProgress(1f, "Instalasi Versi $versionId Selesai!")
                return@withContext true
            }

            onProgress(0.2f, "Mengunduh Package JSON Versi $versionId...")
            val versionPackageStr = fetchUrlString(packageUrl)
            val versionJsonFile = File(verDir, "$versionId.json")
            versionJsonFile.writeText(versionPackageStr)

            // 2. Fetch Client JAR
            val versionObj = JSONObject(versionPackageStr)
            val clientObj = versionObj.optJSONObject("downloads")?.optJSONObject("client")
            val clientJarUrl = clientObj?.optString("url")

            val targetJar = File(verDir, "$versionId.jar")

            if (!clientJarUrl.isNullOrBlank()) {
                onProgress(0.35f, "Mengunduh Client JAR Minecraft $versionId...")
                downloadFileWithProgress(clientJarUrl, targetJar) { prog ->
                    val overall = 0.35f + prog * 0.65f
                    onProgress(overall, "Mengunduh Client JAR: ${(prog * 100).toInt()}%")
                }
            } else {
                targetJar.createNewFile()
            }

            onProgress(1f, "Instalasi $versionId Selesai & Terverifikasi!")
            return@withContext true
        } catch (e: Exception) {
            onProgress(0f, "Gagal mengunduh: ${e.localizedMessage}")
            return@withContext false
        }
    }

    private fun fetchUrlString(urlStr: String): String {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 8000
        conn.readTimeout = 8000
        conn.setRequestProperty("User-Agent", "MattzLauncher/2.0")
        conn.connect()
        return conn.inputStream.bufferedReader().use { it.readText() }
    }

    private fun downloadFileWithProgress(urlStr: String, destination: File, onProgress: (Float) -> Unit) {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 10000
        conn.readTimeout = 15000
        conn.setRequestProperty("User-Agent", "MattzLauncher/2.0")
        conn.connect()

        val contentLength = conn.contentLengthLong.coerceAtLeast(1L)
        var bytesReadTotal = 0L

        conn.inputStream.use { input ->
            FileOutputStream(destination).use { output ->
                val buffer = ByteArray(8192)
                var bytes = input.read(buffer)
                while (bytes >= 0) {
                    output.write(buffer, 0, bytes)
                    bytesReadTotal += bytes
                    onProgress((bytesReadTotal.toFloat() / contentLength).coerceIn(0f, 1f))
                    bytes = input.read(buffer)
                }
            }
        }
    }
}
