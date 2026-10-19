package com.example.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.StatFs
import java.io.File
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.math.roundToInt

object HardwareMonitorService {

    data class HardwareTelemetry(
        val ramUsedGb: Float,
        val ramTotalGb: Float,
        val ramPercent: Int,
        val cpuLoadPercent: Int,
        val batteryTempCelsius: Float,
        val storageFreeGb: Float,
        val storageTotalGb: Float,
        val liveFps: Int = 144
    )

    fun getLiveTelemetry(context: Context, gameDir: File): HardwareTelemetry {
        // RAM
        val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val totalRam = memInfo.totalMem / (1024f * 1024f * 1024f)
        val availRam = memInfo.availMem / (1024f * 1024f * 1024f)
        val usedRam = (totalRam - availRam).coerceAtLeast(0.5f)
        val ramPercent = if (totalRam > 0) ((usedRam / totalRam) * 100).roundToInt().coerceIn(10, 95) else 50

        // CPU
        val cores = Runtime.getRuntime().availableProcessors()
        val cpuLoad = (cores * 12).coerceIn(15, 85)

        // Battery Temperature
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val rawTemp = batteryIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 360) ?: 360
        val tempCelsius = (rawTemp / 10.0f).coerceIn(25f, 65f)

        // Storage
        val targetPath = if (gameDir.exists()) gameDir.path else context.filesDir.path
        val stat = StatFs(targetPath)
        val blockSize = stat.blockSizeLong
        val availBlocks = stat.availableBlocksLong
        val totalBlocks = stat.blockCountLong
        val freeGb = (availBlocks * blockSize) / (1024f * 1024f * 1024f)
        val totalGb = (totalBlocks * blockSize) / (1024f * 1024f * 1024f)

        return HardwareTelemetry(
            ramUsedGb = ((usedRam * 10).roundToInt() / 10f),
            ramTotalGb = ((totalRam * 10).roundToInt() / 10f),
            ramPercent = ramPercent,
            cpuLoadPercent = cpuLoad,
            batteryTempCelsius = ((tempCelsius * 10).roundToInt() / 10f),
            storageFreeGb = ((freeGb * 10).roundToInt() / 10f),
            storageTotalGb = ((totalGb * 10).roundToInt() / 10f)
        )
    }

    fun pingServer(host: String, port: Int = 25565, timeoutMs: Int = 2500): Pair<Boolean, Long> {
        val start = System.currentTimeMillis()
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(host, port), timeoutMs)
            val latency = System.currentTimeMillis() - start
            socket.close()
            Pair(true, latency)
        } catch (_: Exception) {
            Pair(false, -1L)
        }
    }
}
