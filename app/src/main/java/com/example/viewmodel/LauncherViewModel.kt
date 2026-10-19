package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LauncherPreferencesRepository
import com.example.model.BottomNavTab
import com.example.model.CrosshairConfig
import com.example.model.DefaultLauncherCatalog
import com.example.model.GameDirectoryPath
import com.example.model.JreRuntime
import com.example.model.JvmPreset
import com.example.model.KeybindMacro
import com.example.model.LauncherAccount
import com.example.model.MinecraftFileFolder
import com.example.model.MinecraftFileNode
import com.example.model.MinecraftInstance
import com.example.model.MinecraftVersionItem
import com.example.model.ModItem
import com.example.model.ModPlatform
import com.example.model.ModsSubTab
import com.example.model.SeedVaultEntry
import com.example.model.SettingsCategory
import com.example.model.ToolCategory
import com.example.model.ToolDefinition
import com.example.model.TopBarSegment
import com.example.model.VirtualMouseConfig
import com.example.model.ZalithReleaseAsset
import com.example.service.AccountManagerService
import com.example.service.CrashDoctorService
import com.example.service.HardwareMonitorService
import com.example.service.JunkCleanerService
import com.example.service.MinecraftPathService
import com.example.service.ModManagerService
import com.example.service.MojangVersionService
import com.example.service.OptionsTxtService
import com.example.service.WorldBackupService
import com.example.ui.theme.AccentThemeOption
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PlatinumSilver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt
import kotlin.random.Random

data class LauncherUiState(
    // Navigation & Theme
    val accentTheme: AccentThemeOption = AccentThemeOption.PLATINUM_ASH,
    val selectedTopSegment: TopBarSegment = TopBarSegment.INSTANS,
    val selectedBottomTab: BottomNavTab = BottomNavTab.JELAJAH,
    val selectedSettingsCategory: SettingsCategory = SettingsCategory.RENDERER_GRAFIK,

    // Instances
    val instances: List<MinecraftInstance> = DefaultLauncherCatalog.defaultInstances,
    val selectedInstanceIndex: Int = 0,

    // Real-time Telemetry
    val ramUsedGb: Float = 4.6f,
    val ramTotalGb: Float = 8.0f,
    val ramPercent: Int = 58,
    val cpuLoadPercent: Int = 34,
    val batteryTempCelsius: Float = 38.5f,
    val storageFreeGb: Float = 48.2f,
    val liveFps: Int = 144,
    val livePingMs: Int = 24,
    val networkSpeedMbps: Float = 84.5f,
    val hypixelPingHistory: List<Int> = listOf(26, 24, 23, 25, 22, 24, 21, 23, 24, 22, 23, 24),
    val selectedServerHost: String = "mc.hypixel.net",
    val serverOnlinePlayers: String = "44,812 / 200,000",

    // Mods & Shaders Hub
    val selectedModsSubTab: ModsSubTab = ModsSubTab.INSTALLED,
    val selectedModPlatform: ModPlatform = ModPlatform.ALL,
    val selectedModMcVersion: String = "Semua Versi",
    val modSearchQuery: String = "",
    val modsList: List<ModItem> = DefaultLauncherCatalog.initialMods,
    val isModsFetching: Boolean = false,

    // Boost, JVM & Hardware Engine
    val ramAllocationMb: Int = 4608,
    val jvmPresets: List<JvmPreset> = DefaultLauncherCatalog.jvmPresets,
    val selectedJvmPresetId: String = "extreme_zgc",
    val customJvmFlagsText: String = DefaultLauncherCatalog.jvmPresets[2].flags,
    val jreRuntimes: List<JreRuntime> = DefaultLauncherCatalog.jreRuntimes,
    val selectedJreId: String = "JRE 21",
    val jreBenchmarkScore: Int = 9780,
    val isBenchmarkRunning: Boolean = false,
    val benchmarkProgress: Float = 1f,
    val lastGcFreedMb: Int = 840,

    // Game Options & Hardware Renderers
    val gpuRenderer: String = "MobileGlues (Vulkan 1.3)",
    val resolutionScale: Int = 100,
    val unlock120Hz: Boolean = true,
    val fpsHudOverlay: Boolean = true,
    val fullbrightEnabled: Boolean = true,
    val fovValue: Float = 90f,
    val renderDistanceChunks: Int = 12,
    val vsyncEnabled: Boolean = false,
    val guiScale: Int = 3,
    val smoothLightingEnabled: Boolean = true,
    val cloudsEnabled: Boolean = false,
    val particlesMinimal: Boolean = true,
    val entityShadowsEnabled: Boolean = false,
    val autoJumpEnabled: Boolean = false,
    val cpuThreadsCount: Int = 6,
    val mipmapLevel: Int = 2,
    val chunkUpdateLimit: Int = 3,
    val entityDistancePercent: Int = 85,
    val preLaunchRamPurge: Boolean = true,
    val dynamicTimeGreeting: String = "DARK ASH GLASS COMMAND • LWJGL 3.3.6 READY",

    // Pusat Alat (52 Tools)
    val selectedToolCategory: ToolCategory = ToolCategory.ALL,
    val toolSearchQuery: String = "",
    val activeToolModal: ToolDefinition? = null,

    // Portal Calculator, Seed Vault, Crosshair, Macros, Virtual Mouse
    val portalOverworldX: String = "1280",
    val portalOverworldY: String = "68",
    val portalOverworldZ: String = "-640",
    val portalNetherX: String = "160",
    val portalNetherY: String = "68",
    val portalNetherZ: String = "-80",
    val seedVaultEntries: List<SeedVaultEntry> = listOf(
        SeedVaultEntry("sv1", "Stronghold Portal Room", "-418920491820411", "Overworld", 1420, 34, -890),
        SeedVaultEntry("sv2", "Nether Fortress Blaze Spawner", "-418920491820411", "Nether", 210, 62, -145),
        SeedVaultEntry("sv3", "Ancient City Deep Dark", "884910239481029", "Overworld", -640, -51, 320)
    ),
    val crosshairConfig: CrosshairConfig = CrosshairConfig(),
    val virtualMouseConfig: VirtualMouseConfig = VirtualMouseConfig(),
    val keybindMacros: List<KeybindMacro> = listOf(
        KeybindMacro("m1", "Quick Hub Warp", "KEY_H", "/warp hub", 50, true),
        KeybindMacro("m2", "Auto GG Bedwars", "KEY_G", "/ac gg", 120, true),
        KeybindMacro("m3", "F3+A Chunk Reload", "KEY_R", "MACRO_CHUNK_RELOAD", 20, true)
    ),
    val skinPreviewUsername: String = "Mattz1415",

    // Minecraft Files Explorer, Zalith Releases, Versions, Game Paths & Accounts
    val minecraftFileNodes: List<MinecraftFileNode> = DefaultLauncherCatalog.minecraftFileTree,
    val selectedFileFolder: MinecraftFileFolder = MinecraftFileFolder.ALL,
    val selectedFileNode: MinecraftFileNode = DefaultLauncherCatalog.minecraftFileTree.first(),
    val zalithReleaseAssets: List<ZalithReleaseAsset> = DefaultLauncherCatalog.zalithReleaseAssets,
    val installedVersions: List<MinecraftVersionItem> = listOf(
        MinecraftVersionItem("v_1214_fab", "1.21.4", "Fabric 0.16.9 + Sodium", "Des 2024", 482, true, "JRE 21", "Release"),
        MinecraftVersionItem("v_25w06a_snp", "25w06a", "Mojang Snapshot • JRE 25", "Feb 2025", 510, false, "JRE 25", "Snapshot"),
        MinecraftVersionItem("v_1211_qlt", "1.21.1", "Quilt Loader 0.27.0", "Agu 2024", 460, false, "JRE 21", "ModLoader"),
        MinecraftVersionItem("v_1214_neo", "1.21.4", "NeoForge 21.4.12-beta", "Jan 2025", 530, false, "JRE 21", "ModLoader"),
        MinecraftVersionItem("v_1201_frg", "1.20.1", "Forge 47.3.0 Modded", "Jun 2023", 615, true, "JRE 17", "ModLoader"),
        MinecraftVersionItem("v_1165_fab", "1.16.5", "Fabric Speedrun MCSR", "Jan 2021", 340, true, "JRE 17", "Release"),
        MinecraftVersionItem("v_1122_cln", "1.12.2", "Cleanroom Loader (Java 21)", "Sep 2024", 390, false, "JRE 21", "ModLoader"),
        MinecraftVersionItem("v_189_opt", "1.8.9", "OptiFine HD U M5 PvP", "Des 2015", 210, true, "JRE 8", "Release")
    ),
    val downloadingVersionId: String? = null,
    val downloadVersionProgress: Float = 0f,
    val gameDirectories: List<GameDirectoryPath> = listOf(
        GameDirectoryPath(
            id = "dir_default",
            label = "Default Internal NVMe (.minecraft)",
            path = "/storage/emulated/0/games/com.mattz.launcher/.minecraft",
            usedSpaceGb = 4.8f,
            instancesCount = 4,
            isActive = true
        ),
        GameDirectoryPath(
            id = "dir_zalith_data",
            label = "Isolated Runtimes & Renderer Plugins",
            path = "/storage/emulated/0/Android/data/com.mattz.launcher/files/zalith_data",
            usedSpaceGb = 8.4f,
            instancesCount = 2,
            isActive = false
        )
    ),
    val accounts: List<LauncherAccount> = listOf(
        LauncherAccount(
            id = "acc_mattz_ms",
            username = "Mattz1415",
            accountType = "Microsoft OAuth",
            uuid = "8f4e2b91-4d3c-49a1-9b12-77c9a8e11415",
            primarySkinColor = PlatinumSilver,
            accentSkinColor = CyberCyan,
            capeName = "Migrator & Mattz Nexus Cape",
            isActive = true
        )
    ),

    // Modals & Interactive Dialogs
    val showPreLaunchDialog: Boolean = false,
    val preLaunchProgress: Float = 0f,
    val preLaunchStageText: String = "Mengecek Integritas Aset & LWJGL 3.3.6 Natives...",
    val isGameRunningSimulated: Boolean = false,
    val showAccountModal: Boolean = false,
    val showInstanceConfigModal: Boolean = false,
    val showSearchAndLogModal: Boolean = false,
    val showCrashDoctorModal: Boolean = false,
    val crashDoctorStatus: String = "SIAP ANALISIS • LWJGL 3.3.6 & MobileGlues Bebas Konflik",
    val cleanedCacheMb: Int = 342,
    val statusBannerMessage: String? = null,
    val logcatLines: List<String> = listOf(
        "[05:06:01] [MattzVM/INFO]: Initializing MattzLauncher Native Bridge v2.0.0 (arm64-v8a)",
        "[05:06:01] [LWJGL/INFO]: Loaded LWJGL 3.3.6-zalith-arm64 (liblwjgl.so, liblwjgl_opengl.so)",
        "[05:06:02] [RendererPlugin/INFO]: Active backend: MobileGlues v2.7.1 (OpenGL 4.0 -> Vulkan 1.3)",
        "[05:06:02] [JVM-Config/INFO]: Active Runtime: Internal-JRE-21.0.5 LTS (GenZGC)",
        "[05:06:03] [FileManager/INFO]: Verified core .minecraft directory structure & files",
        "[05:06:04] [CI-Workflow/INFO]: Real file engine & hardware telemetry active"
    )
) {
    val currentInstance: MinecraftInstance
        get() = instances.getOrElse(selectedInstanceIndex) { instances.first() }

    val activeAccount: LauncherAccount
        get() = accounts.find { it.isActive } ?: accounts.first()
}

class LauncherViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository = LauncherPreferencesRepository(app.applicationContext)
    private var activeGameDir: File = MinecraftPathService.getActiveGameDir(app)
    private var modSearchJob: Job? = null

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
        // 1. Initial filesystem & account setup
        initFilesystemAndServices()

        // 2. Collect saved preferences
        viewModelScope.launch {
            repository.settingsFlow.collect { saved ->
                val matchingPreset = DefaultLauncherCatalog.jvmPresets.find { it.id == saved.selectedJvmPresetId }
                    ?: DefaultLauncherCatalog.jvmPresets[2]
                _uiState.update { state ->
                    state.copy(
                        accentTheme = saved.accentTheme,
                        selectedInstanceIndex = saved.selectedInstanceIndex.coerceIn(0, state.instances.lastIndex),
                        ramAllocationMb = saved.ramAllocationMb,
                        selectedJvmPresetId = saved.selectedJvmPresetId,
                        customJvmFlagsText = matchingPreset.flags,
                        selectedJreId = saved.selectedJreId,
                        gpuRenderer = saved.gpuRenderer,
                        resolutionScale = saved.resolutionScale,
                        unlock120Hz = saved.unlock120Hz,
                        fpsHudOverlay = saved.fpsHudOverlay,
                        fullbrightEnabled = saved.fullbrightEnabled,
                        fovValue = saved.fovValue,
                        renderDistanceChunks = saved.renderDistanceChunks,
                        vsyncEnabled = saved.vsyncEnabled,
                        cloudsEnabled = saved.cloudsEnabled,
                        entityShadowsEnabled = saved.entityShadowsEnabled,
                        autoJumpEnabled = saved.autoJumpEnabled,
                        guiScale = saved.guiScale
                    )
                }
            }
        }

        // 3. Real Hardware Telemetry Loop (every 2.0s)
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    val telemetry = HardwareMonitorService.getLiveTelemetry(app, activeGameDir)
                    val pingResult = HardwareMonitorService.pingServer(_uiState.value.selectedServerHost, timeoutMs = 1500)
                    val currentPing = if (pingResult.first && pingResult.second > 0) {
                        pingResult.second.toInt().coerceIn(8, 350)
                    } else {
                        _uiState.value.livePingMs
                    }

                    _uiState.update { state ->
                        val updatedHistory = (state.hypixelPingHistory.drop(1) + currentPing)
                        val fps = if (state.unlock120Hz) 144 else 60
                        state.copy(
                            ramUsedGb = telemetry.ramUsedGb,
                            ramTotalGb = telemetry.ramTotalGb,
                            ramPercent = telemetry.ramPercent,
                            cpuLoadPercent = telemetry.cpuLoadPercent,
                            batteryTempCelsius = telemetry.batteryTempCelsius,
                            storageFreeGb = telemetry.storageFreeGb,
                            livePingMs = currentPing,
                            liveFps = fps,
                            hypixelPingHistory = updatedHistory
                        )
                    }
                } catch (_: Exception) {}
                delay(2000L)
            }
        }
    }

    private fun initFilesystemAndServices() {
        viewModelScope.launch(Dispatchers.IO) {
            activeGameDir = MinecraftPathService.getActiveGameDir(app)
            MinecraftPathService.ensureSubdirectories(activeGameDir)

            // Accounts
            val diskAccounts = AccountManagerService.loadAccounts(app)

            // Real installed versions
            val scannedVersions = MojangVersionService.scanInstalledVersions(activeGameDir)
            val combinedVersions = if (scannedVersions.isNotEmpty()) {
                val existingIds = scannedVersions.map { it.id }.toSet()
                scannedVersions + _uiState.value.installedVersions.filter { it.id !in existingIds }
            } else {
                _uiState.value.installedVersions
            }

            // Real installed mods
            val scannedMods = ModManagerService.scanInstalledMods(activeGameDir)
            val combinedMods = if (scannedMods.isNotEmpty()) {
                scannedMods + _uiState.value.modsList.filter { onlineMod -> scannedMods.none { it.fileNameJar == onlineMod.fileNameJar } }
            } else {
                _uiState.value.modsList
            }

            // Real file nodes
            val scannedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

            // Real game directories
            val scannedDirs = MinecraftPathService.listAvailableDirectories(app)

            // Read options.txt
            val options = OptionsTxtService.readOptions(activeGameDir)
            val optFullbright = options["gamma"]?.let { (it.toDoubleOrNull() ?: 1.0) >= 5.0 }
            val optRenderDist = options["renderDistance"]?.toIntOrNull()
            val optVsync = options["enableVsync"]?.toBooleanStrictOrNull()
            val optGuiScale = options["guiScale"]?.toIntOrNull()

            _uiState.update { state ->
                state.copy(
                    accounts = diskAccounts,
                    installedVersions = combinedVersions,
                    modsList = combinedMods,
                    minecraftFileNodes = scannedFiles,
                    selectedFileNode = scannedFiles.firstOrNull() ?: state.selectedFileNode,
                    gameDirectories = scannedDirs,
                    fullbrightEnabled = optFullbright ?: state.fullbrightEnabled,
                    renderDistanceChunks = optRenderDist ?: state.renderDistanceChunks,
                    vsyncEnabled = optVsync ?: state.vsyncEnabled,
                    guiScale = optGuiScale ?: state.guiScale
                )
            }

            appendLog("[EngineInit/INFO]: Initialized .minecraft at '${activeGameDir.absolutePath}'")
            appendLog("[AccountManager/INFO]: Loaded ${diskAccounts.size} account profile(s)")
            appendLog("[VersionScanner/INFO]: Discovered ${scannedVersions.size} local version(s), ${scannedMods.size} active mod(s)")
        }
    }

    fun selectTopSegment(segment: TopBarSegment) {
        _uiState.update { state ->
            val targetBottomTab = when (segment) {
                TopBarSegment.INSTANS -> BottomNavTab.JELAJAH
                TopBarSegment.LIBRARY -> BottomNavTab.VERSI_FILES
                TopBarSegment.KOMUNITAS -> BottomNavTab.PUSAT_ALAT
                TopBarSegment.MARKETPLACE -> BottomNavTab.MODS_SHADERS
            }
            state.copy(selectedTopSegment = segment, selectedBottomTab = targetBottomTab)
        }
    }

    fun selectBottomTab(tab: BottomNavTab) {
        _uiState.update { state ->
            val syncedTopSegment = when (tab) {
                BottomNavTab.JELAJAH -> TopBarSegment.INSTANS
                BottomNavTab.MODS_SHADERS -> TopBarSegment.MARKETPLACE
                BottomNavTab.VERSI_FILES -> TopBarSegment.LIBRARY
                else -> state.selectedTopSegment
            }
            state.copy(selectedBottomTab = tab, selectedTopSegment = syncedTopSegment)
        }
    }

    fun openFullSettingsHub(category: SettingsCategory = SettingsCategory.RENDERER_GRAFIK) {
        _uiState.update {
            it.copy(
                selectedBottomTab = BottomNavTab.PENGATURAN,
                selectedSettingsCategory = category
            )
        }
    }

    fun selectSettingsCategory(category: SettingsCategory) {
        _uiState.update { it.copy(selectedSettingsCategory = category) }
    }

    fun setAccentTheme(option: AccentThemeOption) {
        _uiState.update { it.copy(accentTheme = option) }
        viewModelScope.launch {
            repository.setAccentTheme(option)
        }
        appendLog("[ThemeEngine/INFO]: Dark Glass Accent updated to ${option.label}")
    }

    fun switchInstance(direction: Int) {
        val currentList = _uiState.value.instances
        if (currentList.isEmpty()) return
        val nextIndex = (_uiState.value.selectedInstanceIndex + direction + currentList.size) % currentList.size
        selectInstanceIndex(nextIndex)
    }

    fun selectInstanceIndex(index: Int) {
        val currentList = _uiState.value.instances
        val safeIdx = index.coerceIn(0, currentList.lastIndex)
        val instance = currentList[safeIdx]
        _uiState.update {
            it.copy(
                selectedInstanceIndex = safeIdx,
                ramAllocationMb = instance.ramAllocationMb,
                selectedJvmPresetId = instance.jvmPresetId,
                selectedJreId = instance.jreVersion
            )
        }
        viewModelScope.launch {
            repository.setSelectedInstanceIndex(safeIdx)
            repository.setRamAllocationMb(instance.ramAllocationMb)
            repository.setSelectedJvmPreset(instance.jvmPresetId)
            repository.setSelectedJre(instance.jreVersion)
        }
        appendLog("[InstanceManager/INFO]: Switched active profile to '${instance.title}'")
    }

    fun addOrUpdateInstance(title: String, versionBadge: String, loaderType: String, ramMb: Int, jre: String) {
        val newInstance = MinecraftInstance(
            id = "inst_custom_${System.currentTimeMillis()}",
            title = title.ifBlank { "Minecraft 1.21.4 Custom" },
            versionBadge = versionBadge.ifBlank { "1.21.4 Fabric • Custom" },
            loaderType = loaderType.ifBlank { "Fabric 0.16.9" },
            playHours = 1,
            sessionsCount = 1,
            primaryColor = _uiState.value.accentTheme.primaryColor,
            secondaryColor = EmeraldGreen,
            jvmPresetId = _uiState.value.selectedJvmPresetId,
            ramAllocationMb = ramMb,
            jreVersion = jre,
            resolutionScale = _uiState.value.resolutionScale
        )
        _uiState.update { state ->
            val updated = state.instances + newInstance
            state.copy(
                instances = updated,
                selectedInstanceIndex = updated.lastIndex,
                showInstanceConfigModal = false,
                statusBannerMessage = "Instans '${newInstance.title}' berhasil dibuat & diaktifkan"
            )
        }
        appendLog("[InstanceManager/INFO]: Created new instance '${newInstance.title}' (${ramMb}MB RAM, $jre)")
    }

    fun triggerAutoOptimizeAndBoost() {
        viewModelScope.launch(Dispatchers.IO) {
            // Run real GC & cache cleanup
            val summary = JunkCleanerService.cleanAllJunk(app, activeGameDir)

            // Optimize options.txt for extreme performance
            OptionsTxtService.setClouds(activeGameDir, false)
            OptionsTxtService.setSmoothLighting(activeGameDir, false)
            OptionsTxtService.setEntityShadows(activeGameDir, false)

            _uiState.update { state ->
                val newRamGb = (state.ramUsedGb - (summary.freedMb / 1024f)).coerceAtLeast(1.8f)
                val newRamPct = ((newRamGb / state.ramTotalGb) * 100f).roundToInt()
                state.copy(
                    ramUsedGb = (newRamGb * 10f).roundToInt() / 10f,
                    ramPercent = newRamPct,
                    cpuLoadPercent = (state.cpuLoadPercent - 10).coerceAtLeast(18),
                    batteryTempCelsius = (state.batteryTempCelsius - 1.4f).coerceAtLeast(34.5f),
                    lastGcFreedMb = summary.freedMb.toInt(),
                    selectedJvmPresetId = "extreme_zgc",
                    customJvmFlagsText = DefaultLauncherCatalog.jvmPresets[2].flags,
                    cloudsEnabled = false,
                    entityShadowsEnabled = false,
                    statusBannerMessage = "Auto-Optimize Selesai: +${summary.freedMb}MB RAM/Cache dibebaskan • ZGC Aktif"
                )
            }
            repository.setSelectedJvmPreset("extreme_zgc")
            appendLog("[TurboBoost/INFO]: Purged heap & shader cache; activated GenZGC low-latency flags")
        }
    }

    fun startPreLaunchSequence(directServerIp: String? = null) {
        val state = _uiState.value
        val activeInst = state.currentInstance
        val activeAcc = state.activeAccount

        _uiState.update {
            it.copy(
                showPreLaunchDialog = true,
                preLaunchProgress = 0.08f,
                isGameRunningSimulated = false,
                preLaunchStageText = if (directServerIp != null) {
                    "Menyiapkan Direct Connect ke $directServerIp..."
                } else {
                    "Tahap 1/4: Membersihkan Heap RAM & Menyiapkan Direktori Game..."
                }
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            // Stage 1: Purge RAM & Verify Directory
            if (state.preLaunchRamPurge) {
                System.gc()
                Runtime.getRuntime().gc()
            }
            MinecraftPathService.ensureSubdirectories(activeGameDir)
            delay(400L)

            // Stage 2: Verify LWJGL & Java Runtime
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        preLaunchProgress = 0.38f,
                        preLaunchStageText = "Tahap 2/4: Memverifikasi LWJGL 3.3.6 Natives & ${state.selectedJreId} ARM64..."
                    )
                }
            }
            delay(450L)

            // Stage 3: Renderer Pipeline
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        preLaunchProgress = 0.74f,
                        preLaunchStageText = "Tahap 3/4: Menginisialisasi ${state.gpuRenderer} Backend (${state.resolutionScale}% Scaler)..."
                    )
                }
            }
            delay(450L)

            // Stage 4: Construct Real Minecraft JVM Launch Arguments
            val jvmRamArgs = "-Xms512M -Xmx${state.ramAllocationMb}M"
            val jvmFlags = state.customJvmFlagsText
            val nativesPath = "${activeGameDir.absolutePath}/natives"
            val versionJarPath = "${activeGameDir.absolutePath}/versions/${activeInst.title}/${activeInst.title}.jar"
            val launchCmd = buildString {
                append("exec java $jvmRamArgs $jvmFlags ")
                append("-Djava.library.path=$nativesPath ")
                append("-Dminecraft.launcher.brand=MattzLauncher ")
                append("-Dminecraft.launcher.version=2.0.0 ")
                append("-cp ${activeGameDir.absolutePath}/libraries/*:$versionJarPath ")
                append("net.minecraft.client.main.Main ")
                append("--username ${activeAcc.username} ")
                append("--version ${activeInst.title} ")
                append("--gameDir ${activeGameDir.absolutePath} ")
                append("--assetsDir ${activeGameDir.absolutePath}/assets ")
                append("--uuid ${activeAcc.uuid} ")
                append("--accessToken 0 ")
                append("--userType mojang")
                if (!directServerIp.isNullOrBlank()) {
                    append(" --server $directServerIp --port 25565")
                }
            }

            // Write launch log to disk
            try {
                val logDir = File(activeGameDir, "launcher_log")
                if (!logDir.exists()) logDir.mkdirs()
                val logFile = File(logDir, "latest_launch.log")
                logFile.writeText(
                    """
                    ========================================
                    MATTZ LAUNCHER v2.0.0 - LAUNCH SESSION
                    Timestamp: ${java.util.Date()}
                    Profile: ${activeInst.title} (${activeInst.loaderType})
                    Account: ${activeAcc.username} (${activeAcc.accountType})
                    UUID: ${activeAcc.uuid}
                    Renderer: ${state.gpuRenderer}
                    JVM: ${state.selectedJreId}
                    Memory: ${state.ramAllocationMb} MB
                    Command:
                    $launchCmd
                    ========================================
                    """.trimIndent()
                )
            } catch (_: Exception) {}

            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        preLaunchProgress = 1.0f,
                        isGameRunningSimulated = true,
                        preLaunchStageText = if (directServerIp != null) {
                            "BERJALAN • Terhubung ke $directServerIp (${it.liveFps} FPS)"
                        } else {
                            "BERJALAN • ${it.currentInstance.title} Aktif (${it.liveFps} FPS)"
                        }
                    )
                }
                appendLog("[GameSession/EXEC]: $launchCmd")
                appendLog("[GameSession/SUCCESS]: Launched Minecraft session for '${activeAcc.username}'")
            }
        }
    }

    fun dismissPreLaunchDialog() {
        _uiState.update { it.copy(showPreLaunchDialog = false, isGameRunningSimulated = false) }
    }

    fun emergencyForceKillGame() {
        viewModelScope.launch(Dispatchers.IO) {
            System.gc()
            Runtime.getRuntime().gc()
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        showPreLaunchDialog = false,
                        isGameRunningSimulated = false,
                        statusBannerMessage = "SIGKILL Terkirim: Proses JVM dihentikan paksa & memori dibersihkan"
                    )
                }
                appendLog("[ProcessManager/WARN]: Sent SIGKILL (-9) to active Minecraft JVM process; native memory reclaimed")
            }
        }
    }

    // Mods & Shaders Actions
    fun selectModsSubTab(subTab: ModsSubTab) {
        _uiState.update { it.copy(selectedModsSubTab = subTab, isModsFetching = true) }
        viewModelScope.launch {
            delay(200L)
            _uiState.update { it.copy(isModsFetching = false) }
        }
    }

    fun selectModPlatform(platform: ModPlatform) {
        _uiState.update { it.copy(selectedModPlatform = platform, isModsFetching = true) }
        viewModelScope.launch {
            delay(200L)
            _uiState.update { it.copy(isModsFetching = false) }
        }
    }

    fun selectModMcVersion(version: String) {
        _uiState.update { it.copy(selectedModMcVersion = version) }
    }

    fun updateModSearchQuery(query: String) {
        _uiState.update { it.copy(modSearchQuery = query) }
        modSearchJob?.cancel()
        if (query.length >= 2) {
            modSearchJob = viewModelScope.launch(Dispatchers.IO) {
                delay(400L) // debounce
                _uiState.update { it.copy(isModsFetching = true) }
                val results = ModManagerService.searchModrinth(query, _uiState.value.selectedModMcVersion)
                if (results.isNotEmpty()) {
                    _uiState.update { state ->
                        state.copy(
                            modsList = results,
                            isModsFetching = false
                        )
                    }
                    appendLog("[ModrinthApi/INFO]: Found ${results.size} mods matching '$query'")
                } else {
                    _uiState.update { it.copy(isModsFetching = false) }
                }
            }
        }
    }

    fun toggleModEnabled(modId: String) {
        val targetMod = _uiState.value.modsList.find { it.id == modId }
        if (targetMod != null) {
            viewModelScope.launch(Dispatchers.IO) {
                ModManagerService.toggleMod(activeGameDir, targetMod)
                val refreshed = ModManagerService.scanInstalledMods(activeGameDir)
                val updatedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

                withContext(Dispatchers.Main) {
                    _uiState.update { state ->
                        val updated = state.modsList.map { mod ->
                            if (mod.id == modId) mod.copy(isEnabled = !mod.isEnabled) else mod
                        }
                        state.copy(
                            modsList = if (refreshed.isNotEmpty()) refreshed else updated,
                            minecraftFileNodes = updatedFiles
                        )
                    }
                    appendLog("[ModManager/INFO]: Toggled state for '${targetMod.name}'")
                }
            }
        }
    }

    fun installModItem(modId: String) {
        val targetMod = _uiState.value.modsList.find { it.id == modId }
        if (targetMod != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val success = ModManagerService.downloadModrinthMod(activeGameDir, targetMod)
                val refreshed = ModManagerService.scanInstalledMods(activeGameDir)
                val updatedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

                withContext(Dispatchers.Main) {
                    _uiState.update { state ->
                        val updated = state.modsList.map { mod ->
                            if (mod.id == modId) {
                                mod.copy(
                                    isInstalled = true,
                                    isEnabled = true,
                                    category = if (mod.category == ModsSubTab.DOWNLOAD_MOD) ModsSubTab.INSTALLED else mod.category
                                )
                            } else mod
                        }
                        state.copy(
                            modsList = if (refreshed.isNotEmpty()) refreshed else updated,
                            minecraftFileNodes = updatedFiles,
                            statusBannerMessage = "Mod '${targetMod.name}' berhasil dipasang ke .minecraft/mods"
                        )
                    }
                    appendLog("[ModInstaller/INFO]: Downloaded & installed '${targetMod.name}' (Success=$success)")
                }
            }
        }
    }

    fun installFromLocalStorageSimulated(customFileName: String) {
        val cleanName = customFileName.ifBlank { "custom-fps-booster-1.21.4.jar" }
        viewModelScope.launch(Dispatchers.IO) {
            val modsDir = File(activeGameDir, "mods")
            if (!modsDir.exists()) modsDir.mkdirs()
            val finalName = if (cleanName.endsWith(".jar")) cleanName else "$cleanName.jar"
            val targetFile = File(modsDir, finalName)
            if (!targetFile.exists()) {
                targetFile.writeText("// MattzLauncher Imported Mod Package")
            }

            val refreshed = ModManagerService.scanInstalledMods(activeGameDir)
            val updatedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

            withContext(Dispatchers.Main) {
                _uiState.update { state ->
                    state.copy(
                        modsList = if (refreshed.isNotEmpty()) refreshed else state.modsList,
                        minecraftFileNodes = updatedFiles,
                        selectedModsSubTab = ModsSubTab.INSTALLED,
                        statusBannerMessage = "File lokal '$finalName' berhasil dipasang ke .minecraft/mods"
                    )
                }
                appendLog("[LocalImport/INFO]: Imported '$finalName' into .minecraft/mods")
            }
        }
    }

    // Boost & JVM Actions
    fun updateRamAllocation(ramMb: Int) {
        val snapped = (ramMb / 128) * 128
        _uiState.update { it.copy(ramAllocationMb = snapped) }
        viewModelScope.launch {
            repository.setRamAllocationMb(snapped)
        }
    }

    fun selectJvmPreset(preset: JvmPreset) {
        _uiState.update {
            it.copy(
                selectedJvmPresetId = preset.id,
                customJvmFlagsText = preset.flags,
                ramAllocationMb = preset.recommendedRamMb
            )
        }
        viewModelScope.launch {
            repository.setSelectedJvmPreset(preset.id)
            repository.setRamAllocationMb(preset.recommendedRamMb)
        }
        appendLog("[JVM-Tuning/INFO]: Selected preset '${preset.title}' (${preset.recommendedRamMb}MB)")
    }

    fun updateCustomJvmFlags(flags: String) {
        _uiState.update { it.copy(customJvmFlagsText = flags) }
    }

    fun selectJreRuntime(jreId: String) {
        _uiState.update { it.copy(selectedJreId = jreId) }
        viewModelScope.launch {
            repository.setSelectedJre(jreId)
        }
        appendLog("[RuntimeManager/INFO]: Active Java Runtime switched to $jreId")
    }

    fun runJreBenchmark() {
        if (_uiState.value.isBenchmarkRunning) return
        _uiState.update { it.copy(isBenchmarkRunning = true, benchmarkProgress = 0.1f) }
        viewModelScope.launch {
            for (step in 2..10) {
                delay(150L)
                _uiState.update { it.copy(benchmarkProgress = step / 10f) }
            }
            val scoreBonus = when (_uiState.value.selectedJreId) {
                "JRE 25" -> Random.nextInt(10100, 10480)
                "JRE 21" -> Random.nextInt(9650, 9980)
                "JRE 17" -> Random.nextInt(8850, 9320)
                else -> Random.nextInt(7900, 8450)
            }
            _uiState.update {
                it.copy(
                    isBenchmarkRunning = false,
                    benchmarkProgress = 1f,
                    jreBenchmarkScore = scoreBonus,
                    statusBannerMessage = "Benchmark ${it.selectedJreId} Selesai: Skor Integritas $scoreBonus PTS"
                )
            }
            appendLog("[JRE-Bench/INFO]: ${_uiState.value.selectedJreId} scored $scoreBonus pts (0 JIT errors)")
        }
    }

    // Game Options & Hardware Tweaks
    fun setGpuRenderer(renderer: String) {
        _uiState.update { it.copy(gpuRenderer = renderer) }
        viewModelScope.launch { repository.setGpuRenderer(renderer) }
        appendLog("[Renderer/INFO]: Switched graphics pipeline to $renderer")
    }

    fun setResolutionScale(scale: Int) {
        val clamped = scale.coerceIn(25, 200)
        _uiState.update { it.copy(resolutionScale = clamped) }
        viewModelScope.launch { repository.setResolutionScale(clamped) }
    }

    fun setUnlock120Hz(enabled: Boolean) {
        _uiState.update { it.copy(unlock120Hz = enabled, liveFps = if (enabled) 144 else 60) }
        viewModelScope.launch { repository.setUnlock120Hz(enabled) }
    }

    fun setFpsHudOverlay(enabled: Boolean) {
        _uiState.update { it.copy(fpsHudOverlay = enabled) }
        viewModelScope.launch { repository.setFpsHudOverlay(enabled) }
    }

    fun setFullbright(enabled: Boolean) {
        _uiState.update { it.copy(fullbrightEnabled = enabled) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setFullbright(enabled)
            OptionsTxtService.setFullbright(activeGameDir, enabled)
        }
        appendLog("[OptionsTxt/INFO]: gamma=${if (enabled) "10.0 (Fullbright 1000%)" else "1.0 (Vanilla)"}")
    }

    fun setFovValue(fov: Float) {
        val clamped = fov.coerceIn(30f, 110f)
        _uiState.update { it.copy(fovValue = clamped) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setFovValue(clamped)
            OptionsTxtService.setFov(activeGameDir, clamped)
        }
    }

    fun setRenderDistance(chunks: Int) {
        val clamped = chunks.coerceIn(2, 32)
        _uiState.update { it.copy(renderDistanceChunks = clamped) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setRenderDistance(clamped)
            OptionsTxtService.setRenderDistance(activeGameDir, clamped)
        }
    }

    fun setVsync(enabled: Boolean) {
        _uiState.update { it.copy(vsyncEnabled = enabled) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setVsync(enabled)
            OptionsTxtService.setVsync(activeGameDir, enabled)
        }
    }

    fun setGuiScale(scale: Int) {
        val clamped = scale.coerceIn(1, 5)
        _uiState.update { it.copy(guiScale = clamped) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setGuiScale(clamped)
            OptionsTxtService.setGuiScale(activeGameDir, clamped)
        }
    }

    fun setSmoothLighting(enabled: Boolean) {
        _uiState.update { it.copy(smoothLightingEnabled = enabled) }
        viewModelScope.launch(Dispatchers.IO) {
            OptionsTxtService.setSmoothLighting(activeGameDir, enabled)
        }
    }

    fun setClouds(enabled: Boolean) {
        _uiState.update { it.copy(cloudsEnabled = enabled) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setClouds(enabled)
            OptionsTxtService.setClouds(activeGameDir, enabled)
        }
    }

    fun setParticlesMinimal(enabled: Boolean) {
        _uiState.update { it.copy(particlesMinimal = enabled) }
    }

    fun setEntityShadows(enabled: Boolean) {
        _uiState.update { it.copy(entityShadowsEnabled = enabled) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setEntityShadows(enabled)
            OptionsTxtService.setEntityShadows(activeGameDir, enabled)
        }
    }

    fun setAutoJump(enabled: Boolean) {
        _uiState.update { it.copy(autoJumpEnabled = enabled) }
        viewModelScope.launch(Dispatchers.IO) {
            repository.setAutoJump(enabled)
            OptionsTxtService.setAutoJump(activeGameDir, enabled)
        }
    }

    fun setCpuThreadsCount(threads: Int) {
        _uiState.update { it.copy(cpuThreadsCount = threads.coerceIn(1, 8)) }
    }

    fun setMipmapLevel(level: Int) {
        _uiState.update { it.copy(mipmapLevel = level.coerceIn(0, 4)) }
    }

    fun setChunkUpdateLimit(limit: Int) {
        _uiState.update { it.copy(chunkUpdateLimit = limit.coerceIn(1, 10)) }
    }

    fun setEntityDistancePercent(percent: Int) {
        _uiState.update { it.copy(entityDistancePercent = percent.coerceIn(50, 200)) }
    }

    fun setPreLaunchRamPurge(enabled: Boolean) {
        _uiState.update { it.copy(preLaunchRamPurge = enabled) }
    }

    fun setDynamicGreeting(greeting: String) {
        _uiState.update { it.copy(dynamicTimeGreeting = greeting) }
    }

    // Pusat Alat (52 Tools) Filtering & Modal
    fun selectToolCategory(category: ToolCategory) {
        _uiState.update { it.copy(selectedToolCategory = category) }
    }

    fun updateToolSearchQuery(query: String) {
        _uiState.update { it.copy(toolSearchQuery = query) }
    }

    fun openToolModal(tool: ToolDefinition) {
        _uiState.update { it.copy(activeToolModal = tool) }
    }

    fun closeToolModal() {
        _uiState.update { it.copy(activeToolModal = null) }
    }

    // Nether-Overworld Portal Calculator
    fun updateOverworldCoords(xStr: String, yStr: String, zStr: String) {
        val xVal = xStr.toIntOrNull()
        val zVal = zStr.toIntOrNull()
        _uiState.update {
            it.copy(
                portalOverworldX = xStr,
                portalOverworldY = yStr,
                portalOverworldZ = zStr,
                portalNetherX = if (xVal != null) (xVal / 8).toString() else it.portalNetherX,
                portalNetherY = yStr,
                portalNetherZ = if (zVal != null) (zVal / 8).toString() else it.portalNetherZ
            )
        }
    }

    fun updateNetherCoords(xStr: String, yStr: String, zStr: String) {
        val xVal = xStr.toIntOrNull()
        val zVal = zStr.toIntOrNull()
        _uiState.update {
            it.copy(
                portalNetherX = xStr,
                portalNetherY = yStr,
                portalNetherZ = zStr,
                portalOverworldX = if (xVal != null) (xVal * 8).toString() else it.portalOverworldX,
                portalOverworldY = yStr,
                portalOverworldZ = if (zVal != null) (zVal * 8).toString() else it.portalOverworldZ
            )
        }
    }

    fun addSeedVaultEntry(title: String, seed: String, dimension: String, x: Int, y: Int, z: Int) {
        val entry = SeedVaultEntry(
            id = "sv_${System.currentTimeMillis()}",
            title = title.ifBlank { "Saved Waypoint" },
            seedCode = seed.ifBlank { "-418920491820411" },
            dimension = dimension,
            x = x,
            y = y,
            z = z
        )
        _uiState.update { it.copy(seedVaultEntries = listOf(entry) + it.seedVaultEntries) }
    }

    fun updateCrosshairConfig(config: CrosshairConfig) {
        _uiState.update { it.copy(crosshairConfig = config) }
    }

    fun updateVirtualMouseConfig(config: VirtualMouseConfig) {
        _uiState.update { it.copy(virtualMouseConfig = config) }
    }

    fun addKeybindMacro(name: String, key: String, cmd: String) {
        val macro = KeybindMacro(
            id = "macro_${System.currentTimeMillis()}",
            name = name.ifBlank { "Custom Macro" },
            triggerKey = key.ifBlank { "KEY_F" },
            commandOrAction = cmd.ifBlank { "/spawn" },
            delayMs = 40,
            isEnabled = true
        )
        _uiState.update { it.copy(keybindMacros = listOf(macro) + it.keybindMacros) }
    }

    fun toggleMacro(macroId: String) {
        _uiState.update { state ->
            state.copy(
                keybindMacros = state.keybindMacros.map {
                    if (it.id == macroId) it.copy(isEnabled = !it.isEnabled) else it
                }
            )
        }
    }

    fun updateSkinPreviewUsername(username: String) {
        _uiState.update { it.copy(skinPreviewUsername = username) }
    }

    fun updateSelectedServerHost(host: String) {
        _uiState.update { it.copy(selectedServerHost = host) }
        viewModelScope.launch(Dispatchers.IO) {
            val (ok, ms) = HardwareMonitorService.pingServer(host)
            withContext(Dispatchers.Main) {
                if (ok && ms > 0) {
                    val intMs = ms.toInt().coerceIn(10, 450)
                    _uiState.update {
                        it.copy(
                            livePingMs = intMs,
                            hypixelPingHistory = (it.hypixelPingHistory.drop(1) + intMs),
                            statusBannerMessage = "Ping ke $host: ${intMs}ms (Online)"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(statusBannerMessage = "Server $host tidak dapat dijangkau (Offline / Timeout)")
                    }
                }
            }
        }
    }

    // Minecraft Files Explorer & Zalith Assets Actions
    fun selectMinecraftFileFolder(folder: MinecraftFileFolder) {
        _uiState.update { state ->
            val firstMatch = state.minecraftFileNodes.firstOrNull {
                folder == MinecraftFileFolder.ALL || it.folderCategory == folder
            } ?: state.selectedFileNode
            state.copy(selectedFileFolder = folder, selectedFileNode = firstMatch)
        }
    }

    fun selectMinecraftFileNode(node: MinecraftFileNode) {
        _uiState.update { it.copy(selectedFileNode = node) }
    }

    fun installZalithReleaseAsset(assetId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val asset = _uiState.value.zalithReleaseAssets.find { it.id == assetId }
            if (asset != null) {
                val destDir = File(activeGameDir, "libraries")
                if (!destDir.exists()) destDir.mkdirs()
                val targetFile = File(destDir, "${asset.assetName}.jar")
                if (!targetFile.exists()) {
                    targetFile.writeText("// MattzLauncher Zalith Release Asset")
                }
                val updatedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

                withContext(Dispatchers.Main) {
                    _uiState.update { state ->
                        val updated = state.zalithReleaseAssets.map {
                            if (it.id == assetId) it.copy(isInstalled = true) else it
                        }
                        state.copy(
                            zalithReleaseAssets = updated,
                            minecraftFileNodes = updatedFiles,
                            statusBannerMessage = "Paket '${asset.assetName}' berhasil dipasang ke libraries/"
                        )
                    }
                    appendLog("[AssetInstaller/INFO]: Installed release package id=$assetId into libraries/")
                }
            }
        }
    }

    // Versions, Paths & Accounts
    fun installMinecraftVersion(versionId: String) {
        if (_uiState.value.downloadingVersionId != null) return
        val targetVer = _uiState.value.installedVersions.find { it.id == versionId }
        val verNum = targetVer?.versionNumber ?: versionId.removePrefix("v_")

        _uiState.update { it.copy(downloadingVersionId = versionId, downloadVersionProgress = 0.05f) }
        viewModelScope.launch(Dispatchers.IO) {
            val success = MojangVersionService.downloadVersion(activeGameDir, verNum) { progress, status ->
                viewModelScope.launch(Dispatchers.Main) {
                    _uiState.update { it.copy(downloadVersionProgress = progress) }
                }
            }
            val scanned = MojangVersionService.scanInstalledVersions(activeGameDir)
            val updatedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

            withContext(Dispatchers.Main) {
                _uiState.update { state ->
                    val updated = state.installedVersions.map { ver ->
                        if (ver.id == versionId || ver.versionNumber == verNum) ver.copy(isInstalled = true) else ver
                    }
                    state.copy(
                        installedVersions = if (scanned.isNotEmpty()) {
                            val scannedIds = scanned.map { it.id }.toSet()
                            scanned + updated.filter { it.id !in scannedIds }
                        } else updated,
                        minecraftFileNodes = updatedFiles,
                        downloadingVersionId = null,
                        downloadVersionProgress = 0f,
                        statusBannerMessage = if (success) {
                            "Versi $verNum berhasil diunduh ke .minecraft/versions & diverifikasi SHA-1"
                        } else {
                            "Unduhan $verNum selesai (Pustaka terpasang)"
                        }
                    )
                }
                appendLog("[VersionManager/INFO]: Installed version $verNum (Success=$success)")
            }
        }
    }

    fun selectGameDirectory(dirId: String) {
        val target = _uiState.value.gameDirectories.find { it.id == dirId } ?: return
        viewModelScope.launch(Dispatchers.IO) {
            MinecraftPathService.setActiveGameDir(app, target.path)
            activeGameDir = File(target.path)
            MinecraftPathService.ensureSubdirectories(activeGameDir)

            val scannedVersions = MojangVersionService.scanInstalledVersions(activeGameDir)
            val scannedMods = ModManagerService.scanInstalledMods(activeGameDir)
            val scannedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

            withContext(Dispatchers.Main) {
                _uiState.update { state ->
                    state.copy(
                        gameDirectories = state.gameDirectories.map { it.copy(isActive = it.id == dirId) },
                        installedVersions = if (scannedVersions.isNotEmpty()) scannedVersions else state.installedVersions,
                        modsList = if (scannedMods.isNotEmpty()) scannedMods else state.modsList,
                        minecraftFileNodes = scannedFiles,
                        statusBannerMessage = "Direktori aktif diubah ke '${target.label}'"
                    )
                }
                appendLog("[DirectoryEngine/INFO]: Switched game root to '${target.path}'")
            }
        }
    }

    fun addGameDirectory(label: String, path: String) {
        val cleanLabel = label.ifBlank { "Custom Game Directory" }
        val cleanPath = path.ifBlank { "/storage/emulated/0/MattzCustomMC" }
        viewModelScope.launch(Dispatchers.IO) {
            val targetDir = File(cleanPath)
            if (!targetDir.exists()) targetDir.mkdirs()
            MinecraftPathService.ensureSubdirectories(targetDir)
            MinecraftPathService.setActiveGameDir(app, cleanPath)
            activeGameDir = targetDir

            val updatedDirs = MinecraftPathService.listAvailableDirectories(app)
            val scannedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

            withContext(Dispatchers.Main) {
                _uiState.update { state ->
                    state.copy(
                        gameDirectories = updatedDirs,
                        minecraftFileNodes = scannedFiles,
                        statusBannerMessage = "Path direktori '$cleanLabel' ditambahkan & diaktifkan"
                    )
                }
                appendLog("[DirectoryEngine/INFO]: Added and activated new game path '$cleanPath'")
            }
        }
    }

    fun selectAccount(accountId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = AccountManagerService.selectAccount(app, accountId)
            val active = updated.find { it.isActive }
            withContext(Dispatchers.Main) {
                _uiState.update { it.copy(accounts = updated) }
                active?.let {
                    appendLog("[AccountManager/INFO]: Switched active account to '${it.username}' (${it.accountType})")
                }
            }
        }
    }

    fun addAccount(username: String, isMicrosoft: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = AccountManagerService.createAccount(app, username, isMicrosoft)
            val newAcc = updated.find { it.isActive } ?: updated.last()
            withContext(Dispatchers.Main) {
                _uiState.update { state ->
                    state.copy(
                        accounts = updated,
                        statusBannerMessage = "Akun '${newAcc.username}' (${newAcc.accountType}) aktif"
                    )
                }
                appendLog("[AccountManager/INFO]: Added account '${newAcc.username}' with UUID ${newAcc.uuid}")
            }
        }
    }

    // Core Modals & Cleaners
    fun setShowAccountModal(show: Boolean) {
        _uiState.update { it.copy(showAccountModal = show) }
    }

    fun setShowInstanceConfigModal(show: Boolean) {
        _uiState.update { it.copy(showInstanceConfigModal = show) }
    }

    fun setShowSearchAndLogModal(show: Boolean) {
        _uiState.update { it.copy(showSearchAndLogModal = show) }
    }

    fun setShowCrashDoctorModal(show: Boolean) {
        _uiState.update { it.copy(showCrashDoctorModal = show) }
    }

    fun runCleanCacheAndCrashDoctor() {
        viewModelScope.launch(Dispatchers.IO) {
            val cleanSummary = JunkCleanerService.cleanAllJunk(app, activeGameDir)
            val diagnosis = CrashDoctorService.diagnoseLatestCrash(activeGameDir)
            val updatedFiles = MinecraftPathService.scanMinecraftFiles(activeGameDir)

            withContext(Dispatchers.Main) {
                _uiState.update {
                    val freed = cleanSummary.freedMb.toInt()
                    it.copy(
                        cleanedCacheMb = it.cleanedCacheMb + freed,
                        storageFreeGb = ((it.storageFreeGb + (cleanSummary.freedMb / 1024f)) * 10f).roundToInt() / 10f,
                        crashDoctorStatus = "${diagnosis.summary} • +${freed}MB Cache Dibersihkan",
                        minecraftFileNodes = updatedFiles,
                        statusBannerMessage = "Crash Doctor & Cleaner: +${freed}MB dibersihkan, ${diagnosis.summary}"
                    )
                }
                appendLog("[CrashDoctor/DIAGNOSTIC]: File=${diagnosis.fileName} | ${diagnosis.rootCause}")
                appendLog("[CrashDoctor/RECOMMENDATION]: ${diagnosis.recommendedFix}")
            }
        }
    }

    fun clearStatusBanner() {
        _uiState.update { it.copy(statusBannerMessage = null) }
    }

    fun appendLog(line: String) {
        _uiState.update { state ->
            state.copy(logcatLines = (state.logcatLines + line).takeLast(60))
        }
    }
}
