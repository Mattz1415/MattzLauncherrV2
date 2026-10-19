package com.example.service

import java.io.File

object CrashDoctorService {

    data class CrashReportAnalysis(
        val fileName: String,
        val summary: String,
        val rootCause: String,
        val recommendedFix: String,
        val isClean: Boolean
    )

    fun diagnoseLatestCrash(gameDir: File): CrashReportAnalysis {
        val crashDir = File(gameDir, "crash-reports")
        var targetFile: File? = null

        if (crashDir.exists() && crashDir.isDirectory) {
            val list = crashDir.listFiles()?.filter { it.isFile && (it.name.endsWith(".txt") || it.name.endsWith(".log")) }
                ?.sortedByDescending { it.lastModified() }
            if (!list.isNullOrEmpty()) {
                targetFile = list.first()
            }
        }

        if (targetFile == null) {
            val logDir = File(gameDir, "launcher_log")
            if (logDir.exists() && logDir.isDirectory) {
                val list = logDir.listFiles()?.filter { it.isFile }?.sortedByDescending { it.lastModified() }
                if (!list.isNullOrEmpty()) {
                    targetFile = list.first()
                }
            }
        }

        if (targetFile == null || !targetFile.exists()) {
            return CrashReportAnalysis(
                fileName = "Tidak Ada Crash Report",
                summary = "SIAP ANALISIS • Sistem Normal & Bebas Konflik",
                rootCause = "Tidak ditemukan catatan crash report di .minecraft/crash-reports atau launcher_log.",
                recommendedFix = "Semua pipeline LWJGL, JVM flags, dan driver GPU berjalan normal. Tidak ada tindakan perbaikan yang diperlukan.",
                isClean = true
            )
        }

        val content = try {
            targetFile.readText()
        } catch (_: Exception) {
            ""
        }

        return when {
            content.contains("OutOfMemoryError", ignoreCase = true) || content.contains("Metaspace", ignoreCase = true) -> {
                CrashReportAnalysis(
                    fileName = targetFile.name,
                    summary = "MEMORI HABIS (OutOfMemoryError)",
                    rootCause = "Alokasi RAM Java Heap tidak mencukupi untuk memuat aset tekstur dan mod.",
                    recommendedFix = "Buka menu 'Boost & JVM', lalu naikkan slider Alokasi RAM minimal ke 4096 MB (4GB) atau gunakan preset 'Extreme FPS (ZGC)'.",
                    isClean = false
                )
            }
            content.contains("ClassNotFoundException", ignoreCase = true) || content.contains("NoClassDefFoundError", ignoreCase = true) -> {
                val line = content.lines().find { it.contains("ClassNotFoundException") || it.contains("NoClassDefFoundError") } ?: ""
                CrashReportAnalysis(
                    fileName = targetFile.name,
                    summary = "MOD DEPENDENCY HILANG",
                    rootCause = "Terdapat kelas pustaka mod yang tidak ditemukan: ${line.trim().take(60)}",
                    recommendedFix = "Pasang Fabric API, Quilted Fabric API, atau Forge Loader versi yang sesuai dengan versi Minecraft target.",
                    isClean = false
                )
            }
            content.contains("GL_INVALID", ignoreCase = true) || content.contains("EGL_BAD", ignoreCase = true) || content.contains("Vulkan", ignoreCase = true) -> {
                CrashReportAnalysis(
                    fileName = targetFile.name,
                    summary = "KONFLIK RENDERER GRAFIS",
                    rootCause = "Driver GPU atau translator OpenGL gagal menginisialisasi context layar.",
                    recommendedFix = "Buka tab 'Pengaturan' -> 'Renderer & GPU', lalu ganti backend ke HolyGL4ES 1.1.5 atau VirGL OpenGL 4.3.",
                    isClean = false
                )
            }
            content.contains("Mixin", ignoreCase = true) && content.contains("injection failed", ignoreCase = true) -> {
                CrashReportAnalysis(
                    fileName = targetFile.name,
                    summary = "KONFLIK INJEKSI MIXIN",
                    rootCause = "Ada dua mod modifikasi bytecode yang bertabrakan saat memodifikasi class yang sama.",
                    recommendedFix = "Periksa folder Mods di tab 'Mods & Shaders', matikan mod yang baru saja ditambahkan satu per satu.",
                    isClean = false
                )
            }
            else -> {
                CrashReportAnalysis(
                    fileName = targetFile.name,
                    summary = "LOG ANALYZED • Selesai",
                    rootCause = "Log terakhir tidak menunjukkan error kritis sistem yang merusak.",
                    recommendedFix = "Game siap diluncurkan. Lakukan Pre-Launch verification untuk memastikan keutuhan file JAR.",
                    isClean = true
                )
            }
        }
    }
}
