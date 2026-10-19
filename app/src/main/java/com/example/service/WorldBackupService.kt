package com.example.service

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object WorldBackupService {

    data class WorldSave(
        val folder: File,
        val name: String,
        val lastModifiedDate: String,
        val sizeMb: Float
    )

    fun listWorlds(gameDir: File): List<WorldSave> {
        val savesDir = File(gameDir, "saves")
        if (!savesDir.exists() || !savesDir.isDirectory) return emptyList()

        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        return savesDir.listFiles()?.filter { it.isDirectory }?.map { dir ->
            var sizeBytes = 0L
            try {
                dir.walkTopDown().maxDepth(3).forEach { f -> if (f.isFile) sizeBytes += f.length() }
            } catch (_: Exception) {}
            val mb = (sizeBytes / (1024f * 1024f)).coerceAtLeast(0.5f)
            WorldSave(
                folder = dir,
                name = dir.name,
                lastModifiedDate = dateFormat.format(Date(dir.lastModified())),
                sizeMb = ((mb * 10).toInt() / 10f)
            )
        } ?: emptyList()
    }

    fun backupWorld(gameDir: File, worldName: String): File? {
        val savesDir = File(gameDir, "saves")
        val worldDir = File(savesDir, worldName)
        if (!worldDir.exists() || !worldDir.isDirectory) return null

        val backupDir = File(gameDir, "backups")
        if (!backupDir.exists()) backupDir.mkdirs()

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val zipFile = File(backupDir, "${worldName}_backup_$timeStamp.zip")

        return try {
            ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
                zipDirectoryRecursive(worldDir, worldDir.name, zos)
            }
            zipFile
        } catch (_: Exception) {
            null
        }
    }

    private fun zipDirectoryRecursive(folder: File, parentPath: String, zos: ZipOutputStream) {
        folder.listFiles()?.forEach { file ->
            val path = "$parentPath/${file.name}"
            if (file.isDirectory) {
                zipDirectoryRecursive(file, path, zos)
            } else {
                try {
                    FileInputStream(file).use { fis ->
                        val entry = ZipEntry(path)
                        zos.putNextEntry(entry)
                        fis.copyTo(zos, bufferSize = 8192)
                        zos.closeEntry()
                    }
                } catch (_: Exception) {}
            }
        }
    }
}
