package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PlatinumSilver
import com.example.ui.theme.PureWhite

enum class TopBarSegment(val label: String) {
    INSTANS("Instans"),
    LIBRARY("Library"),
    KOMUNITAS("Komunitas"),
    MARKETPLACE("Marketplace")
}

enum class BottomNavTab(val index: Int, val label: String) {
    JELAJAH(0, "Jelajah"),
    MODS_SHADERS(1, "Mods & Shaders"),
    BOOST_JVM(2, "Boost & JVM"),
    PUSAT_ALAT(3, "Pusat Alat"),
    VERSI_FILES(4, "Versi & Files"),
    PENGATURAN(5, "Pengaturan")
}

data class MinecraftInstance(
    val id: String,
    val title: String,
    val versionBadge: String,
    val loaderType: String,
    val playHours: Int,
    val sessionsCount: Int,
    val primaryColor: Color,
    val secondaryColor: Color,
    val jvmPresetId: String,
    val ramAllocationMb: Int,
    val jreVersion: String,
    val resolutionScale: Int
)

enum class ModsSubTab(val label: String) {
    INSTALLED("Installed Mods (.jar Toggle)"),
    DOWNLOAD_MOD("Download Mod"),
    MODPACKS("Modpacks"),
    RESOURCE_PACKS("Resource Packs"),
    SHADER_PACKS("Shader Packs"),
    WORLDS("Worlds")
}

enum class ModPlatform(val label: String) {
    ALL("Semua Platform"),
    MODRINTH("Modrinth"),
    CURSEFORGE("CurseForge")
}

data class ModItem(
    val id: String,
    val name: String,
    val author: String,
    val summary: String,
    val category: ModsSubTab,
    val platform: ModPlatform,
    val mcVersion: String,
    val fileSize: String,
    val downloads: String,
    val fileNameJar: String,
    val isInstalled: Boolean,
    val isEnabled: Boolean,
    val accentColor: Color
)

data class JvmPreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val flags: String,
    val recommendedRamMb: Int,
    val estFpsBoost: String
)

data class JreRuntime(
    val id: String,
    val name: String,
    val vendor: String,
    val javaVersion: String,
    val architecture: String,
    val path: String,
    val statusReady: Boolean,
    val compatibleMc: String
)

enum class ToolCategory(val label: String) {
    ALL("Semua (52)"),
    TELEMETRY_DISPLAY("1-10 Telemetry & Display"),
    JVM_MEMORY("11-20 JVM & Memory"),
    GAME_OPTIONS("21-30 Opsi Game"),
    SERVER_WORLD("31-40 Server & World"),
    ADVANCED_SYSTEM("41-52 Suite Lanjutan")
}

data class ToolDefinition(
    val id: Int,
    val title: String,
    val subtitle: String,
    val badgeText: String,
    val category: ToolCategory,
    val accentColor: Color
)

data class MinecraftVersionItem(
    val id: String,
    val versionNumber: String,
    val loader: String,
    val releaseDate: String,
    val sizeMb: Int,
    val isInstalled: Boolean,
    val jreRequired: String,
    val releaseChannel: String = "Release" // "Release", "Snapshot", "ModLoader"
)

enum class MinecraftFileFolder(val label: String, val relativeDir: String) {
    ALL("Semua File (.minecraft)", ".minecraft/"),
    VERSIONS("versions/", ".minecraft/versions/"),
    LIBRARIES("libraries/ & LWJGL", ".minecraft/libraries/"),
    ASSETS("assets/ & indexes/", ".minecraft/assets/"),
    MODS_CONFIG("mods/ & config/", ".minecraft/mods/"),
    RENDERERS_RUNTIMES("runtimes/ & plugins/", "zalith_data/plugins/"),
    CONTROLS_LOGS("controlmap/ & logs/", "zalith_data/controlmap/")
}

data class MinecraftFileNode(
    val id: String,
    val fileName: String,
    val relativePath: String,
    val folderCategory: MinecraftFileFolder,
    val fileTypeBadge: String,
    val sizeLabel: String,
    val sha1Short: String,
    val description: String,
    val previewContent: String,
    val isVerified: Boolean = true
)

data class ZalithReleaseAsset(
    val id: String,
    val assetName: String,
    val versionTag: String,
    val category: String, // "Renderer Plugin", "Java Runtime", "LWJGL Native", "Control Map"
    val arch: String,
    val fileSize: String,
    val description: String,
    val isInstalled: Boolean
)

data class GameDirectoryPath(
    val id: String,
    val label: String,
    val path: String,
    val usedSpaceGb: Float,
    val instancesCount: Int,
    val isActive: Boolean
)

data class LauncherAccount(
    val id: String,
    val username: String,
    val accountType: String, // "Microsoft OAuth" or "Offline Account"
    val uuid: String,
    val primarySkinColor: Color,
    val accentSkinColor: Color,
    val capeName: String,
    val isActive: Boolean
)

data class SeedVaultEntry(
    val id: String,
    val title: String,
    val seedCode: String,
    val dimension: String,
    val x: Int,
    val y: Int,
    val z: Int
)

data class KeybindMacro(
    val id: String,
    val name: String,
    val triggerKey: String,
    val commandOrAction: String,
    val delayMs: Int,
    val isEnabled: Boolean
)

data class CrosshairConfig(
    val styleIndex: Int = 0, // 0: Cyber Plus, 1: Dot Ring, 2: Chevron Tactical
    val sizePx: Float = 14f,
    val gapPx: Float = 4f,
    val thicknessPx: Float = 2f,
    val showCenterDot: Boolean = true,
    val colorHex: String = "#E2E8F0"
)

data class VirtualMouseConfig(
    val speedSensitivity: Float = 1.35f,
    val gyroSensitivity: Float = 0.85f,
    val cursorScale: Float = 1.15f,
    val cursorStyle: Int = 0, // 0: Precision Spear, 1: Neon Diamond, 2: Minimal Cross
    val autoHideSeconds: Int = 4
)

enum class SettingsCategory(val index: Int, val title: String, val subtitle: String) {
    RENDERER_GRAFIK(0, "Renderer & Grafik", "MobileGlues, Zink, LTW, 120Hz"),
    JAVA_MEMORI(1, "Java & Memori", "RAM, JRE 8/17/21/25, GC Flags"),
    KONTROL_MOUSE(2, "Kontrol & Virtual Mouse", "Gyro, ControlMap, Kursor Kustom"),
    ANTARMUKA_ANIMASI(3, "Antarmuka & Animasi Futuristik", "Dark Abu-Abu Glass, Warna Aksen"),
    SISTEM_DIAGNOSTIK(4, "Sistem & Diagnostik", "Logcat, Crash Doctor, GitHub CI"),
    TENTANG(5, "Tentang MattzLauncher", "Versi 1.0.0, Kreator, GPLv3")
}

object DefaultLauncherCatalog {
    val defaultInstances = listOf(
        MinecraftInstance(
            id = "inst_1214_fabric",
            title = "Minecraft 1.21.4 Ultra FPS",
            versionBadge = "1.21.4 Fabric • Sodium",
            loaderType = "Fabric 0.16.9",
            playHours = 42,
            sessionsCount = 18,
            primaryColor = PlatinumSilver,
            secondaryColor = CyberCyan,
            jvmPresetId = "extreme_zgc",
            ramAllocationMb = 4608,
            jreVersion = "JRE 21",
            resolutionScale = 100
        ),
        MinecraftInstance(
            id = "inst_1201_forge",
            title = "1.20.1 Forge Modded",
            versionBadge = "1.20.1 Forge • Embeddium",
            loaderType = "Forge 47.3.0",
            playHours = 86,
            sessionsCount = 34,
            primaryColor = AmberGold,
            secondaryColor = CrimsonRed,
            jvmPresetId = "aikar_flags",
            ramAllocationMb = 6144,
            jreVersion = "JRE 17",
            resolutionScale = 90
        ),
        MinecraftInstance(
            id = "inst_1165_speedrun",
            title = "1.16.5 Speedrun",
            versionBadge = "1.16.5 Fabric • MCSR",
            loaderType = "Fabric 0.15.11",
            playHours = 64,
            sessionsCount = 52,
            primaryColor = EmeraldGreen,
            secondaryColor = CyberCyan,
            jvmPresetId = "balanced_g1",
            ramAllocationMb = 3584,
            jreVersion = "JRE 17",
            resolutionScale = 85
        ),
        MinecraftInstance(
            id = "inst_189_pvp",
            title = "1.8.9 PvP Client",
            versionBadge = "1.8.9 OptiFine • Bedwars",
            loaderType = "OptiFine HD U M5",
            playHours = 128,
            sessionsCount = 95,
            primaryColor = CrimsonRed,
            secondaryColor = AmethystPurple,
            jvmPresetId = "low_end_gc",
            ramAllocationMb = 2560,
            jreVersion = "JRE 8",
            resolutionScale = 100
        )
    )

    val jvmPresets = listOf(
        JvmPreset(
            id = "low_end_gc",
            title = "Low-End GC",
            subtitle = "Serial/Parallel kompak untuk RAM di bawah 3GB",
            badge = "HEMAT RAM",
            flags = "-XX:+UseSerialGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -Dsun.rmi.dgc.client.gcInterval=2147483646 -XX:+UnlockExperimentalVMOptions",
            recommendedRamMb = 2048,
            estFpsBoost = "+28% Low-1% FPS"
        ),
        JvmPreset(
            id = "balanced_g1",
            title = "Balanced",
            subtitle = "G1GC adaptif dengan jeda mikro di bawah 25ms",
            badge = "DIREKOMENDASIKAN",
            flags = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=25 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:G1NewSizePercent=20",
            recommendedRamMb = 4096,
            estFpsBoost = "+42% Avg FPS"
        ),
        JvmPreset(
            id = "extreme_zgc",
            title = "Extreme FPS ZGC",
            subtitle = "Generational ZGC ultra-low latency untuk JRE 21/25",
            badge = "ULTRA FPS",
            flags = "-XX:+UseZGC -XX:+ZGenerational -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Dfml.readTimeout=90",
            recommendedRamMb = 4608,
            estFpsBoost = "+65% Peak FPS"
        ),
        JvmPreset(
            id = "aikar_flags",
            title = "Aikar's High Stability Flags",
            subtitle = "Standar emas modpack berat & chunk loading intensif",
            badge = "HEAVY MODPACK",
            flags = "-XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M",
            recommendedRamMb = 6144,
            estFpsBoost = "0% Stutter Spike"
        )
    )

    val jreRuntimes = listOf(
        JreRuntime(
            id = "JRE 8",
            name = "Internal-JRE-8u422 (Liberica/Temurin)",
            vendor = "Zalith Bundled ARM64",
            javaVersion = "1.8.0_422-b05",
            architecture = "aarch64 • HotSpot C2",
            path = "/data/user/0/com.mattz.launcher/runtimes/Internal-JRE-8",
            statusReady = true,
            compatibleMc = "MC 1.7.10 - 1.16.5 (OptiFine / Forge)"
        ),
        JreRuntime(
            id = "JRE 17",
            name = "Internal-JRE-17.0.12 LTS (GraalVM)",
            vendor = "Zalith Bundled ARM64",
            javaVersion = "17.0.12+8-LTS",
            architecture = "aarch64 • Graal JIT",
            path = "/data/user/0/com.mattz.launcher/runtimes/Internal-JRE-17",
            statusReady = true,
            compatibleMc = "MC 1.17 - 1.20.4 & Cleanroom"
        ),
        JreRuntime(
            id = "JRE 21",
            name = "Internal-JRE-21.0.5 LTS (Azul Zulu GenZGC)",
            vendor = "Zalith Bundled ARM64",
            javaVersion = "21.0.5+11-LTS",
            architecture = "aarch64 • Falcon ZGC",
            path = "/data/user/0/com.mattz.launcher/runtimes/Internal-JRE-21",
            statusReady = true,
            compatibleMc = "MC 1.20.5 - 1.21.4+ (Fabric / NeoForge)"
        ),
        JreRuntime(
            id = "JRE 25",
            name = "Internal-JRE-25-ea (Experimental Vector API)",
            vendor = "Zalith Next-Gen ARM64",
            javaVersion = "25-ea+14-zalith",
            architecture = "aarch64 • Project Leyden + ZGC",
            path = "/data/user/0/com.mattz.launcher/runtimes/Internal-JRE-25",
            statusReady = true,
            compatibleMc = "MC 1.21.4+ & Snapshot 25w06a"
        )
    )

    val minecraftFileTree: List<MinecraftFileNode> = listOf(
        // versions/
        MinecraftFileNode(
            id = "f_ver_1214_jar",
            fileName = "1.21.4.jar",
            relativePath = ".minecraft/versions/1.21.4/1.21.4.jar",
            folderCategory = MinecraftFileFolder.VERSIONS,
            fileTypeBadge = "CLIENT JAR",
            sizeLabel = "26.4 MB",
            sha1Short = "8a4f9c2e1b",
            description = "Binary utama klien Minecraft: Java Edition 1.21.4 Official Mojang.",
            previewContent = "Manifest-Version: 1.0\nMain-Class: net.minecraft.client.main.Main\nImplementation-Version: 1.21.4\nBundled-LWJGL: 3.3.6-zalith-arm64"
        ),
        MinecraftFileNode(
            id = "f_ver_1214_json",
            fileName = "1.21.4-Fabric-0.16.9.json",
            relativePath = ".minecraft/versions/1.21.4-Fabric/1.21.4-Fabric.json",
            folderCategory = MinecraftFileFolder.VERSIONS,
            fileTypeBadge = "VERSION JSON",
            sizeLabel = "42 KB",
            sha1Short = "3d91e7a04c",
            description = "Deskriptor classpath, mainClass KnotClient, dan argumen JVM Fabric Loader.",
            previewContent = "{\n  \"id\": \"1.21.4-Fabric-0.16.9\",\n  \"inheritsFrom\": \"1.21.4\",\n  \"mainClass\": \"net.fabricmc.loader.impl.launch.knot.KnotClient\",\n  \"javaVersion\": { \"component\": \"java-runtime-delta\", \"majorVersion\": 21 }\n}"
        ),
        MinecraftFileNode(
            id = "f_ver_1201_forge",
            fileName = "1.20.1-forge-47.3.0.json",
            relativePath = ".minecraft/versions/1.20.1-forge-47.3.0/1.20.1-forge-47.3.0.json",
            folderCategory = MinecraftFileFolder.VERSIONS,
            fileTypeBadge = "FORGE JSON",
            sizeLabel = "68 KB",
            sha1Short = "9c11f84b20",
            description = "Konfigurasi BootstrapLauncher ModLauncher Forge 47.3.0 untuk MC 1.20.1.",
            previewContent = "{\n  \"id\": \"1.20.1-forge-47.3.0\",\n  \"mainClass\": \"cpw.mods.bootstraplauncher.BootstrapLauncher\",\n  \"minimumLauncherVersion\": 21\n}"
        ),
        // libraries/ & LWJGL
        MinecraftFileNode(
            id = "f_lib_lwjgl",
            fileName = "lwjgl-3.3.6-zalith-arm64.jar",
            relativePath = ".minecraft/libraries/org/lwjgl/lwjgl/3.3.6/lwjgl-3.3.6-zalith-arm64.jar",
            folderCategory = MinecraftFileFolder.LIBRARIES,
            fileTypeBadge = "LWJGL 3.3.6",
            sizeLabel = "1.84 MB",
            sha1Short = "f49b201c8e",
            description = "Lightweight Java Game Library 3.3.6 yang dioptimalkan untuk Android ARM64.",
            previewContent = "Native Bindings: liblwjgl.so, liblwjgl_opengl.so, liblwjgl_stb.so, liblwjgl_tinyfd.so\nTarget ABI: arm64-v8a (API 24+)"
        ),
        MinecraftFileNode(
            id = "f_lib_openal",
            fileName = "libopenal_soft_arm64.so",
            relativePath = ".minecraft/libraries/org/lwjgl/openal/3.3.6/libopenal_soft_arm64.so",
            folderCategory = MinecraftFileFolder.LIBRARIES,
            fileTypeBadge = "NATIVE SO",
            sizeLabel = "1.12 MB",
            sha1Short = "7e22d10a9f",
            description = "Engine audio spasial OpenAL Soft 1.23.1 dengan dukungan AAudio/OpenSL ES latensi rendah.",
            previewContent = "ELF 64-bit LSB shared object, ARM aarch64, version 1 (SYSV), dynamically linked\nAudio Backend: Android AAudio Low-Latency"
        ),
        MinecraftFileNode(
            id = "f_lib_authlib",
            fileName = "authlib-6.0.54.jar",
            relativePath = ".minecraft/libraries/com/mojang/authlib/6.0.54/authlib-6.0.54.jar",
            folderCategory = MinecraftFileFolder.LIBRARIES,
            fileTypeBadge = "AUTHLIB",
            sizeLabel = "318 KB",
            sha1Short = "5a88c3102b",
            description = "Pustaka autentikasi Yggdrasil / Microsoft Xbox Live OAuth dan pemuat tekstur skin.",
            previewContent = "SessionServer: https://sessionserver.mojang.com\nServicesAPI: https://api.minecraftservices.com"
        ),
        // assets/ & indexes/
        MinecraftFileNode(
            id = "f_ast_idx19",
            fileName = "19.json (Asset Index 1.21.4)",
            relativePath = ".minecraft/assets/indexes/19.json",
            folderCategory = MinecraftFileFolder.ASSETS,
            fileTypeBadge = "ASSET INDEX",
            sizeLabel = "448 KB",
            sha1Short = "1b77a94e01",
            description = "Daftar hash SHA-1 untuk 4,120 objek suara, musik, ikon, dan bahasa.",
            previewContent = "{\n  \"objects\": {\n    \"minecraft/sounds/ambient/cave/cave1.ogg\": { \"hash\": \"f89c12...\", \"size\": 48210 },\n    \"minecraft/lang/id_id.json\": { \"hash\": \"44a91b...\", \"size\": 812400 }\n  }\n}"
        ),
        // mods/ & config/
        MinecraftFileNode(
            id = "f_cfg_options",
            fileName = "options.txt",
            relativePath = ".minecraft/options.txt",
            folderCategory = MinecraftFileFolder.MODS_CONFIG,
            fileTypeBadge = "CONFIG TXT",
            sizeLabel = "6.4 KB",
            sha1Short = "e019c44a82",
            description = "File konfigurasi utama klien Minecraft (Gamma Fullbright, FOV, Render Distance, Keybinds).",
            previewContent = "version:4189\nautoJump:false\nrenderDistance:12\nsimulationDistance:8\nfov:0.5\ngamma:10.0\nguiScale:3\nmaxFps:260\nenableVsync:false"
        ),
        MinecraftFileNode(
            id = "f_cfg_sodium",
            fileName = "sodium-options.json",
            relativePath = ".minecraft/config/sodium-options.json",
            folderCategory = MinecraftFileFolder.MODS_CONFIG,
            fileTypeBadge = "SODIUM JSON",
            sizeLabel = "2.1 KB",
            sha1Short = "c821b09f34",
            description = "Pengaturan pipeline rendering Sodium (Chunk Memory Allocator Async, Always Defer Chunk Updates).",
            previewContent = "{\n  \"quality\": { \"clouds_quality\": \"OFF\", \"weather_quality\": \"FAST\", \"leaves_quality\": \"FAST\" },\n  \"performance\": { \"chunk_builder_threads\": 6, \"always_defer_chunk_updates\": true }\n}"
        ),
        // runtimes/ & renderer_plugins/
        MinecraftFileNode(
            id = "f_plg_mobileglues",
            fileName = "libmobileglues_v2.7.so",
            relativePath = "zalith_data/plugins/RendererPlugin-MobileGlues/libmobileglues_v2.7.so",
            folderCategory = MinecraftFileFolder.RENDERERS_RUNTIMES,
            fileTypeBadge = "RENDERER SO",
            sizeLabel = "3.45 MB",
            sha1Short = "a902f11c48",
            description = "Plugin Renderer MobileGlues v2.7 (OpenGL 4.0 -> Vulkan 1.3) dengan dukungan penuh Sodium & Iris.",
            previewContent = "Renderer-ID: MobileGlues-Vulkan-1.3\nGLSL-Translator: SPIRV-Cross + glslang\nEnv: MG_MAX_GLSL_CACHE_SIZE=512M, MG_MULTIDRAW_MODE=indirect"
        ),
        MinecraftFileNode(
            id = "f_plg_zink",
            fileName = "libzink_mesa_turnip.so",
            relativePath = "zalith_data/plugins/RendererPlugin-MesaZink/libzink_mesa_turnip.so",
            folderCategory = MinecraftFileFolder.RENDERERS_RUNTIMES,
            fileTypeBadge = "MESA ZINK",
            sizeLabel = "14.8 MB",
            sha1Short = "d734e89a12",
            description = "Mesa 24.3 Zink + Freedreno Turnip Vulkan Driver untuk GPU Qualcomm Adreno.",
            previewContent = "MESA_LOADER_DRIVER_OVERRIDE=zink\nTU_DEBUG=noconform\nZINK_DESCRIPTORS=lazy"
        ),
        MinecraftFileNode(
            id = "f_plg_holygl4es",
            fileName = "libgl4es_114_zalith.so",
            relativePath = "zalith_data/plugins/HolyGL4ES/libgl4es_114_zalith.so",
            folderCategory = MinecraftFileFolder.RENDERERS_RUNTIMES,
            fileTypeBadge = "GL4ES SO",
            sizeLabel = "1.92 MB",
            sha1Short = "b412c09e77",
            description = "HolyGL4ES 1.1.5 untuk kompatibilitas tinggi pada semua perangkat OpenGL ES 2.0/3.0.",
            previewContent = "LIBGL_ES=2\nLIBGL_MIPMAP=3\nLIBGL_NORMALIZE=1\nLIBGL_NOERROR=1"
        ),
        // controlmap/ & logs/
        MinecraftFileNode(
            id = "f_ctl_pvp",
            fileName = "mattz_pvp_pro_landscape.json",
            relativePath = "zalith_data/controlmap/mattz_pvp_pro_landscape.json",
            folderCategory = MinecraftFileFolder.CONTROLS_LOGS,
            fileTypeBadge = "CONTROLMAP",
            sizeLabel = "18 KB",
            sha1Short = "6f01a88c31",
            description = "Peta tombol khusus layar landscape (Hotbar 1-9 cepat, F5 Perspektif, Gudang Macro, Gyro Aim).",
            previewContent = "{\n  \"layoutName\": \"Mattz PvP Pro Landscape\",\n  \"version\": 2,\n  \"gyroEnabled\": true,\n  \"buttons\": [\"LMB_ATTACK\", \"RMB_PLACE\", \"SNEAK_HOLD\", \"F5_CAM\", \"ZOOM_C\"]\n}"
        ),
        MinecraftFileNode(
            id = "f_log_latest",
            fileName = "latest.log",
            relativePath = ".minecraft/logs/latest.log",
            folderCategory = MinecraftFileFolder.CONTROLS_LOGS,
            fileTypeBadge = "RUNTIME LOG",
            sizeLabel = "124 KB",
            sha1Short = "2c99e41f08",
            description = "Catatan waktu nyata proses JVM, pemuatan Mixin Fabric, dan inisialisasi renderer.",
            previewContent = "[05:06:12] [Render thread/INFO]: Backend library: LWJGL version 3.3.6-zalith-arm64\n[05:06:13] [Render thread/INFO]: OpenGL Renderer: MobileGlues (Vulkan 1.3 Pipeline)\n[05:06:14] [Render thread/INFO]: Loaded 5 Fabric mods; Sodium 0.6.5 active"
        )
    )

    val zalithReleaseAssets: List<ZalithReleaseAsset> = listOf(
        ZalithReleaseAsset(
            id = "zr_mobileglues",
            assetName = "RendererPlugin-MobileGlues-v2.7.1-arm64.apk",
            versionTag = "v2.7.1",
            category = "Renderer Plugin",
            arch = "arm64-v8a",
            fileSize = "4.2 MB",
            description = "Renderer Vulkan 1.3 generasi baru untuk Sodium 0.6+ & Iris Shaders tanpa artefak bayangan.",
            isInstalled = true
        ),
        ZalithReleaseAsset(
            id = "zr_ltw",
            assetName = "RendererPlugin-LTW-GLES32-v2.7.apk",
            versionTag = "v2.7.0",
            category = "Renderer Plugin",
            arch = "arm64-v8a / armeabi-v7a",
            fileSize = "2.1 MB",
            description = "LightWeightTranslation OpenGL ES 3.2 wrapper hemat baterai untuk semua GPU Mali & Adreno.",
            isInstalled = true
        ),
        ZalithReleaseAsset(
            id = "zr_turnip",
            assetName = "Mesa-Zink-Turnip-Adreno-v24.3.4.tar.xz",
            versionTag = "v24.3.4",
            category = "Renderer Plugin",
            arch = "arm64-v8a (Adreno 6xx/7xx)",
            fileSize = "11.6 MB",
            description = "Driver Vulkan Turnip kustom dengan patch ekstensi descriptor indexing untuk modpack besar.",
            isInstalled = true
        ),
        ZalithReleaseAsset(
            id = "zr_jre25",
            assetName = "Internal-JRE-25-ea-aarch64-linux-android.tar.xz",
            versionTag = "25-ea+14",
            category = "Java Runtime",
            arch = "arm64-v8a",
            fileSize = "43.8 MB",
            description = "Runtime Java 25 eksperimental dari rilis terbaru dengan optimasi Generational ZGC & Vector API.",
            isInstalled = true
        ),
        ZalithReleaseAsset(
            id = "zr_lwjgl336",
            assetName = "LWJGL-3.3.6-zalith-natives-arm64.tar.xz",
            versionTag = "3.3.6-r2",
            category = "LWJGL Native",
            arch = "arm64-v8a",
            fileSize = "3.9 MB",
            description = "Perbaikan LWJGL-OpenGL mapping errors, GLFW touch bridge, dan peningkatan stabilitas mouse.",
            isInstalled = true
        ),
        ZalithReleaseAsset(
            id = "zr_cleanroom",
            assetName = "Cleanroom-Loader-0.2.4-alpha-MC1.12.2.zip",
            versionTag = "v0.2.4",
            category = "ModLoader Bridge",
            arch = "Universal JRE 21",
            fileSize = "8.7 MB",
            description = "Jalankan modpack Forge 1.12.2 klasik di atas Java 21 modern dengan peningkatan FPS drastis.",
            isInstalled = false
        )
    )

    val initialMods = listOf(
        // INSTALLED MODS (.jar Toggle)
        ModItem(
            id = "mod_sodium",
            name = "Sodium Ultra Engine",
            author = "CaffeineMC",
            summary = "Modern OpenGL/Vulkan chunk rendering pipeline pengganti renderer bawaan.",
            category = ModsSubTab.INSTALLED,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "1.18 MB",
            downloads = "42.8M",
            fileNameJar = "sodium-fabric-0.6.5+mc1.21.4.jar",
            isInstalled = true,
            isEnabled = true,
            accentColor = EmeraldGreen
        ),
        ModItem(
            id = "mod_lithium",
            name = "Lithium Server & Physics AI",
            author = "JellySquid",
            summary = "Optimasi fisika mob, AI tick, dan logika blok tanpa mengubah mekanik vanilla.",
            category = ModsSubTab.INSTALLED,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "690 KB",
            downloads = "31.4M",
            fileNameJar = "lithium-fabric-0.14.3+mc1.21.4.jar",
            isInstalled = true,
            isEnabled = true,
            accentColor = AmberGold
        ),
        ModItem(
            id = "mod_iris",
            name = "Iris Shaders Bridge",
            author = "Coderbot",
            summary = "Pipeline shader modern yang terintegrasi penuh dengan Sodium & MobileGlues.",
            category = ModsSubTab.INSTALLED,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "2.45 MB",
            downloads = "29.1M",
            fileNameJar = "iris-fabric-1.8.4+mc1.21.4.jar",
            isInstalled = true,
            isEnabled = true,
            accentColor = CyberCyan
        ),
        ModItem(
            id = "mod_ferritecore",
            name = "FerriteCore Memory Reducer",
            author = "malte0811",
            summary = "Mengurangi pemakaian RAM blockstate & model hingga 45% pada perangkat mobile.",
            category = ModsSubTab.INSTALLED,
            platform = ModPlatform.CURSEFORGE,
            mcVersion = "1.21.4",
            fileSize = "312 KB",
            downloads = "54.0M",
            fileNameJar = "ferritecore-7.1.0-fabric.jar",
            isInstalled = true,
            isEnabled = true,
            accentColor = AmethystPurple
        ),
        ModItem(
            id = "mod_entityculling",
            name = "EntityCulling Raytrace",
            author = "tr7zw",
            summary = "Melewati render tile-entity dan mob yang terhalang tembok dengan raytracing async.",
            category = ModsSubTab.INSTALLED,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "420 KB",
            downloads = "24.6M",
            fileNameJar = "entityculling-fabric-1.7.2-mc1.21.4.jar",
            isInstalled = true,
            isEnabled = false,
            accentColor = CrimsonRed
        ),
        // DOWNLOAD MOD
        ModItem(
            id = "dl_immediatelyfast",
            name = "ImmediatelyFast Batching",
            author = "RK_01",
            summary = "Meningkatkan performa immediate mode rendering untuk GUI, HUD, dan entitas.",
            category = ModsSubTab.DOWNLOAD_MOD,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "380 KB",
            downloads = "19.2M",
            fileNameJar = "ImmediatelyFast-Fabric-1.3.3+1.21.4.jar",
            isInstalled = false,
            isEnabled = false,
            accentColor = CyberCyan
        ),
        ModItem(
            id = "dl_modernfix",
            name = "ModernFix All-In-One",
            author = "embeddedt",
            summary = "Mempercepat waktu loading dunia 2x lipat dan membebaskan alokasi heap dinamis.",
            category = ModsSubTab.DOWNLOAD_MOD,
            platform = ModPlatform.CURSEFORGE,
            mcVersion = "1.21.4",
            fileSize = "940 KB",
            downloads = "36.5M",
            fileNameJar = "modernfix-fabric-5.20.0+mc1.21.4.jar",
            isInstalled = false,
            isEnabled = false,
            accentColor = AmberGold
        ),
        ModItem(
            id = "dl_zoomify",
            name = "Zoomify Optic",
            author = "isXander",
            summary = "Zoom kamera sinematik dengan transisi spring yang dapat dikustomisasi penuh.",
            category = ModsSubTab.DOWNLOAD_MOD,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "810 KB",
            downloads = "11.4M",
            fileNameJar = "Zoomify-2.14.2+1.21.4.jar",
            isInstalled = false,
            isEnabled = false,
            accentColor = EmeraldGreen
        ),
        // MODPACKS
        ModItem(
            id = "mp_fabulously",
            name = "Fabulously Optimized Mobile",
            author = "Robotkoer",
            summary = "Modpack kompetitif untuk FPS maksimal, fitur OptiFine lengkap, dan stabilitas mobile.",
            category = ModsSubTab.MODPACKS,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "44.2 MB",
            downloads = "9.8M",
            fileNameJar = "FabulouslyOptimized-6.2.0.mrpack",
            isInstalled = true,
            isEnabled = true,
            accentColor = AmberGold
        ),
        ModItem(
            id = "mp_prominence",
            name = "Prominence II RPG: Mobile Lite",
            author = "LunaPixelStudios",
            summary = "Eksplorasi dimensi baru, boss kustom, dan sistem talent tree dengan tuning RAM hemat.",
            category = ModsSubTab.MODPACKS,
            platform = ModPlatform.CURSEFORGE,
            mcVersion = "1.20.1",
            fileSize = "312.0 MB",
            downloads = "5.4M",
            fileNameJar = "Prominence-II-Lite-2.8.5.zip",
            isInstalled = false,
            isEnabled = false,
            accentColor = AmethystPurple
        ),
        // RESOURCE PACKS
        ModItem(
            id = "rp_faithful64",
            name = "Faithful 64x Dark Glass Edition",
            author = "FaithfulTeam",
            summary = "Tekstur resolusi tinggi 64x64 dengan GUI transparan abu-abu gelap bergaya kaca.",
            category = ModsSubTab.RESOURCE_PACKS,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "28.5 MB",
            downloads = "14.1M",
            fileNameJar = "Faithful-64x-1.21.4.zip",
            isInstalled = true,
            isEnabled = true,
            accentColor = EmeraldGreen
        ),
        ModItem(
            id = "rp_barebones_pvp",
            name = "Bare Bones PvP Low-Fire",
            author = "RobotPantaloons",
            summary = "Tekstur bersih bergaya trailer resmi dengan api rendah untuk visibilitas PvP.",
            category = ModsSubTab.RESOURCE_PACKS,
            platform = ModPlatform.CURSEFORGE,
            mcVersion = "1.21.4",
            fileSize = "8.4 MB",
            downloads = "8.7M",
            fileNameJar = "BareBones-PvP-1.21.zip",
            isInstalled = true,
            isEnabled = false,
            accentColor = CyberCyan
        ),
        // SHADER PACKS
        ModItem(
            id = "sh_complementary",
            name = "Complementary Reimagined Mobile",
            author = "EminGT",
            summary = "Shader atmosferik dengan air reflektif dan awan volumetrik ringan untuk MobileGlues/Zink.",
            category = ModsSubTab.SHADER_PACKS,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "1.1 MB",
            downloads = "27.3M",
            fileNameJar = "ComplementaryReimagined_r5.3_Mobile.zip",
            isInstalled = true,
            isEnabled = true,
            accentColor = AmethystPurple
        ),
        ModItem(
            id = "sh_bsl_lite",
            name = "BSL Shaders Ultra-Low Latency",
            author = "Capttatsu",
            summary = "Pencahayaan hangat sinematik dengan bloom lembut dan bayangan real-time hemat GPU.",
            category = ModsSubTab.SHADER_PACKS,
            platform = ModPlatform.CURSEFORGE,
            mcVersion = "1.21.4",
            fileSize = "920 KB",
            downloads = "33.9M",
            fileNameJar = "BSL_v8.4_MobileLite.zip",
            isInstalled = false,
            isEnabled = false,
            accentColor = AmberGold
        ),
        // WORLDS
        ModItem(
            id = "wld_cyber_citadel",
            name = "Nexus Cyber Citadel Survival",
            author = "Mattz1415",
            summary = "Dunia survival teknikal dengan megabase redstone otomatis dan perimeter penuh.",
            category = ModsSubTab.WORLDS,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "142.6 MB",
            downloads = "Local Save",
            fileNameJar = "saves/Nexus_Cyber_Citadel",
            isInstalled = true,
            isEnabled = true,
            accentColor = CyberCyan
        ),
        ModItem(
            id = "wld_pvp_dojo",
            name = "Aim & Clutch Training Arena",
            author = "Mattz1415",
            summary = "Map latihan bridge, block clutch, dan crystal PvP dengan penghitung reaksi.",
            category = ModsSubTab.WORLDS,
            platform = ModPlatform.CURSEFORGE,
            mcVersion = "1.20.1",
            fileSize = "38.2 MB",
            downloads = "Local Save",
            fileNameJar = "saves/Aim_Clutch_Arena",
            isInstalled = true,
            isEnabled = true,
            accentColor = CrimsonRed
        )
    )

    val all52Tools: List<ToolDefinition> = listOf(
        // 1-10: Telemetry & Display
        ToolDefinition(1, "RAM Ring Gauge", "Monitor alokasi heap & native memory real-time", "LIVE 58%", ToolCategory.TELEMETRY_DISPLAY, CyberCyan),
        ToolDefinition(2, "CPU Core Monitor", "Frekuensi 8-core ARM64 & distribusi thread", "8 CORES", ToolCategory.TELEMETRY_DISPLAY, EmeraldGreen),
        ToolDefinition(3, "Battery Thermal Radar", "Sensor suhu baterai & proteksi thermal throttling", "38.5 C", ToolCategory.TELEMETRY_DISPLAY, AmberGold),
        ToolDefinition(4, "Storage Analyzer", "Rincian ruang mod, libraries, assets, dan saves", "48.2 GB", ToolCategory.TELEMETRY_DISPLAY, AmethystPurple),
        ToolDefinition(5, "Live FPS HUD Overlay Toggle", "Tampilkan penghitung FPS, frame-time, dan RAM di dalam game", "HUD ON", ToolCategory.TELEMETRY_DISPLAY, PureWhite),
        ToolDefinition(6, "Ping Radar Graph", "Grafik latensi milidetik real-time ke server utama", "24 ms", ToolCategory.TELEMETRY_DISPLAY, CyberCyan),
        ToolDefinition(7, "Network Speed Meter", "Throughput unduhan paket chunk & aset BMCLAPI/Mojang", "84 Mbps", ToolCategory.TELEMETRY_DISPLAY, EmeraldGreen),
        ToolDefinition(8, "GPU Renderer Switcher", "Pilih MobileGlues, HolyGL4ES, LTW, Zink Vulkan, atau VirGL", "MG VK1.3", ToolCategory.TELEMETRY_DISPLAY, AmberGold),
        ToolDefinition(9, "Resolution Downscaler Slider", "Skala resolusi internal 25% hingga 200% untuk FPS", "100%", ToolCategory.TELEMETRY_DISPLAY, CyberCyan),
        ToolDefinition(10, "120Hz Refresh Rate Unlocker", "Buka kunci batas frame rate layar 90Hz/120Hz/144Hz", "120HZ", ToolCategory.TELEMETRY_DISPLAY, CrimsonRed),

        // 11-20: JVM & Memory
        ToolDefinition(11, "Smart JVM Preset Selector", "Beralih cepat antara Low-End, Balanced, ZGC, dan Aikar", "PRESET", ToolCategory.JVM_MEMORY, AmberGold),
        ToolDefinition(12, "Manual GC Trigger", "Paksa System.gc() & pembersihan objek yatim di heap", "PURGE", ToolCategory.JVM_MEMORY, EmeraldGreen),
        ToolDefinition(13, "Pre-Launch RAM Purge", "Bersihkan proses latar sebelum mem-boot JVM", "AUTO", ToolCategory.JVM_MEMORY, CyberCyan),
        ToolDefinition(14, "Cache & Junk Cleaner", "Hapus shader cache MobileGlues/Zink, log lama, dan temp asset", "CLEAN", ToolCategory.JVM_MEMORY, CrimsonRed),
        ToolDefinition(15, "Crash Log Doctor Analyzer", "Diagnosis otomatis penyebab crash Exit Code 1 / LWJGL mapping", "DOCTOR", ToolCategory.JVM_MEMORY, AmethystPurple),
        ToolDefinition(16, "Live Logcat Terminal Viewer", "Stream stdout/stderr JVM dan LWJGL 3.3.6 secara langsung", "STDOUT", ToolCategory.JVM_MEMORY, PureWhite),
        ToolDefinition(17, "CPU Thread Count Optimizer", "Atur jumlah worker threads untuk chunk builder", "6 THREAD", ToolCategory.JVM_MEMORY, CyberCyan),
        ToolDefinition(18, "Mipmap Level Tweaker", "Kurangi level mipmap tekstur (0-4) untuk hemat VRAM", "LEVEL 2", ToolCategory.JVM_MEMORY, AmberGold),
        ToolDefinition(19, "Chunk Update Limiter", "Batasi pembaruan chunk per frame agar bebas stutter", "LIMIT 3", ToolCategory.JVM_MEMORY, EmeraldGreen),
        ToolDefinition(20, "Entity Distance Scaler", "Sesuaikan jarak render mob & armor stand (50%-200%)", "85%", ToolCategory.JVM_MEMORY, AmethystPurple),

        // 21-30: Game Options (options.txt direct tweaker)
        ToolDefinition(21, "Fullbright Gamma 1000% Toggle", "Suntikkan nilai gamma 10.0 ke options.txt untuk gua terang", "1000%", ToolCategory.GAME_OPTIONS, AmberGold),
        ToolDefinition(22, "FOV Quick Slider (30-110)", "Atur sudut pandang kamera dari Zoom 30 hingga Quake Pro 110", "FOV 90", ToolCategory.GAME_OPTIONS, CyberCyan),
        ToolDefinition(23, "Render Distance Slider", "Atur jarak pandang dunia dari 2 hingga 32 Chunks", "12 CHUNK", ToolCategory.GAME_OPTIONS, EmeraldGreen),
        ToolDefinition(24, "VSync Toggle", "Matikan sinkronisasi vertikal untuk input lag minimum", "OFF", ToolCategory.GAME_OPTIONS, CrimsonRed),
        ToolDefinition(25, "GUI Scale Switcher", "Sesuaikan ukuran HUD inventaris untuk layar landscape", "SCALE 3", ToolCategory.GAME_OPTIONS, PureWhite),
        ToolDefinition(26, "Smooth Lighting Toggle", "Atur pencahayaan halus Ambient Occlusion blok", "MINIMAL", ToolCategory.GAME_OPTIONS, AmethystPurple),
        ToolDefinition(27, "Clouds Off Toggle", "Nonaktifkan render awan untuk menaikkan FPS di ketinggian", "OFF", ToolCategory.GAME_OPTIONS, CyberCyan),
        ToolDefinition(28, "Particles Minimalizer", "Kurangi partikel ledakan & potion saat kombo PvP", "MINIMAL", ToolCategory.GAME_OPTIONS, AmberGold),
        ToolDefinition(29, "Entity Shadows Off", "Hapus bayangan bulat di bawah entitas untuk hemat draw call", "OFF", ToolCategory.GAME_OPTIONS, EmeraldGreen),
        ToolDefinition(30, "Auto-Jump Toggle", "Kontrol cepat status Auto-Jump pada options.txt", "DISABLED", ToolCategory.GAME_OPTIONS, CrimsonRed),

        // 31-40: Server & World Utilities
        ToolDefinition(31, "Direct Server IP Quick-Connect", "Luncurkan game langsung masuk ke alamat IP server tujuan", "DIRECT", ToolCategory.SERVER_WORLD, CyberCyan),
        ToolDefinition(32, "Server MOTD & Player Pinger", "Cek jumlah pemain online, protokol, dan ping server", "PINGER", ToolCategory.SERVER_WORLD, EmeraldGreen),
        ToolDefinition(33, "Nether-Overworld Portal Calculator", "Konverter koordinat X/Y/Z instan (x8 / :8) untuk link portal", "CALC X/Z", ToolCategory.SERVER_WORLD, AmberGold),
        ToolDefinition(34, "Seed & Coordinate Vault", "Simpan daftar seed dunia dan koordinat struktur penting", "VAULT", ToolCategory.SERVER_WORLD, AmethystPurple),
        ToolDefinition(35, "One-Click World ZIP Backup", "Kompres folder dunia ke arsip ZIP terenkripsi waktu", "BACKUP", ToolCategory.SERVER_WORLD, PureWhite),
        ToolDefinition(36, "World Level.dat Repair Tool", "Perbaiki korupsi header NBT level.dat & cadangan level.dat_old", "REPAIR", ToolCategory.SERVER_WORLD, CrimsonRed),
        ToolDefinition(37, "In-Launcher Screenshot Gallery", "Pratinjau & kelola tangkapan layar F2 dengan metadata", "GALLERY", ToolCategory.SERVER_WORLD, CyberCyan),
        ToolDefinition(38, "Skin & Cape Previewer/Downloader", "Tampilkan & unduh skin/cape pemain berdasar username", "2D SKIN", ToolCategory.SERVER_WORLD, EmeraldGreen),
        ToolDefinition(39, "Custom Crosshair Overlay Editor", "Desainer crosshair Canvas interaktif untuk PvP & bridging", "CANVAS", ToolCategory.SERVER_WORLD, AmberGold),
        ToolDefinition(40, "Keybind Macro Creator", "Buat tombol pintas perintah chat & kombinasi tombol", "MACROS", ToolCategory.SERVER_WORLD, AmethystPurple),

        // 41-52: Advanced & System Suite
        ToolDefinition(41, "Mod .jar Enable/Disable Switcher", "Aktif/nonaktifkan file .jar cepat tanpa menghapus file", "JAR SW", ToolCategory.ADVANCED_SYSTEM, EmeraldGreen),
        ToolDefinition(42, "ResourcePack Load Order Sorter", "Atur prioritas tumpukan tekstur dari atas ke bawah", "ORDER", ToolCategory.ADVANCED_SYSTEM, CyberCyan),
        ToolDefinition(43, "ShaderPack Selector", "Ganti shader aktif secara instan sebelum game dimulai", "SHADER", ToolCategory.ADVANCED_SYSTEM, AmethystPurple),
        ToolDefinition(44, "JRE Benchmark Suite", "Uji kecepatan JIT, alokasi matriks, dan skor integritas JRE 8/17/21/25", "BENCH", ToolCategory.ADVANCED_SYSTEM, AmberGold),
        ToolDefinition(45, "Playtime & Session Analytics", "Grafik durasi bermain mingguan & distribusi per instans", "STATS", ToolCategory.ADVANCED_SYSTEM, CyberCyan),
        ToolDefinition(46, "Dynamic Time Greeting HUD", "Konfigurasi pesan sambutan & status waktu di Main Hub", "HUD MSG", ToolCategory.ADVANCED_SYSTEM, PureWhite),
        ToolDefinition(47, "Launcher Config Profile Export/Import", "Ekspor & impor seluruh pengaturan MattzLauncher dalam JSON", "JSON IO", ToolCategory.ADVANCED_SYSTEM, EmeraldGreen),
        ToolDefinition(48, "Audio Engine Restarter", "Muat ulang OpenAL Soft 1.23.1 & perbaiki suara hilang saat ganti output", "OPENAL", ToolCategory.ADVANCED_SYSTEM, AmberGold),
        ToolDefinition(49, "Touch & Gyro Sensitivity Calibrator", "Kalibrasi presisi sensor giroskop dan kurva sentuh layar", "GYRO", ToolCategory.ADVANCED_SYSTEM, CyberCyan),
        ToolDefinition(50, "Custom Virtual Mouse Cursor Designer", "Ubah ukuran, bentuk vektor, dan warna kursor mouse virtual", "CURSOR", ToolCategory.ADVANCED_SYSTEM, AmethystPurple),
        ToolDefinition(51, "Emergency Force-Kill Game Process", "Hentikan paksa proses JVM yang membeku (SIGKILL)", "SIGKILL", ToolCategory.ADVANCED_SYSTEM, CrimsonRed),
        ToolDefinition(52, "Custom Dark Glass Accent Theme Engine", "Ubah warna aksen dinamis (Abu-Abu Glass, White, Cyan, Amber, Emerald, Crimson, Amethyst)", "THEME", ToolCategory.ADVANCED_SYSTEM, PlatinumSilver)
    )
}
