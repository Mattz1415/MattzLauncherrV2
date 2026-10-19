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
import com.example.ui.theme.AccentThemeOption
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PlatinumSilver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        ),
        LauncherAccount(
            id = "acc_pvp_alt",
            username = "MattzPvP_God",
            accountType = "Microsoft OAuth",
            uuid = "3a1c9022-6e11-40d8-b451-19f0c2d89901",
            primarySkinColor = AmberGold,
            accentSkinColor = CrimsonRed,
            capeName = "OptiFine Gold Cape",
            isActive = false
        ),
        LauncherAccount(
            id = "acc_offline_dev",
            username = "MattzOfflineDev",
            accountType = "Offline Account",
            uuid = "00000000-0000-3000-8000-000000001415",
            primarySkinColor = AmethystPurple,
            accentSkinColor = CyberCyan,
            capeName = "Nexus Dev Cloak",
            isActive = false
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
        "[05:06:01] [MattzVM/INFO]: Initializing MattzLauncher Native Bridge v1.0.0 (arm64-v8a)",
        "[05:06:01] [LWJGL/INFO]: Loaded LWJGL 3.3.6-zalith-arm64 (liblwjgl.so, liblwjgl_opengl.so)",
        "[05:06:02] [RendererPlugin/INFO]: Active backend: MobileGlues v2.7.1 (OpenGL 4.0 -> Vulkan 1.3)",
        "[05:06:02] [JVM-Config/INFO]: Active Runtime: Internal-JRE-21.0.5 LTS (GenZGC)",
        "[05:06:03] [FileManager/INFO]: Verified 14 core .minecraft files, libraries, and controlmaps",
        "[05:06:04] [CI-Workflow/INFO]: GitHub Actions workflow (.github/workflows/build-apk.yml) ready"
    )
) {
    val currentInstance: MinecraftInstance
        get() = instances.getOrElse(selectedInstanceIndex) { instances.first() }

    val activeAccount: LauncherAccount
        get() = accounts.find { it.isActive } ?: accounts.first()
}

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LauncherPreferencesRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
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

        viewModelScope.launch {
            while (true) {
                delay(1800L)
                _uiState.update { state ->
                    val ramDelta = (Random.nextFloat() - 0.5f) * 0.14f
                    val newRam = (state.ramUsedGb + ramDelta).coerceIn(3.4f, 6.4f)
                    val newRamPct = ((newRam / state.ramTotalGb) * 100f).roundToInt().coerceIn(42, 85)
                    val newCpu = (state.cpuLoadPercent + Random.nextInt(-3, 4)).coerceIn(22, 58)
                    val tempDelta = (Random.nextFloat() - 0.48f) * 0.2f
                    val newTemp = ((state.batteryTempCelsius + tempDelta).coerceIn(35.8f, 41.2f) * 10f).roundToInt() / 10f
                    val newPing = (state.livePingMs + Random.nextInt(-2, 3)).coerceIn(18, 36)
                    val newFps = if (state.unlock120Hz) Random.nextInt(138, 166) else Random.nextInt(58, 61)
                    val updatedHistory = (state.hypixelPingHistory.drop(1) + newPing)
                    state.copy(
                        ramUsedGb = (newRam * 10f).roundToInt() / 10f,
                        ramPercent = newRamPct,
                        cpuLoadPercent = newCpu,
                        batteryTempCelsius = newTemp,
                        livePingMs = newPing,
                        liveFps = newFps,
                        hypixelPingHistory = updatedHistory
                    )
                }
            }
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
        _uiState.update { state ->
            val freedMb = Random.nextInt(620, 1150)
            val newRamGb = (state.ramUsedGb - 0.9f).coerceAtLeast(3.1f)
            val newRamPct = ((newRamGb / state.ramTotalGb) * 100f).roundToInt()
            state.copy(
                ramUsedGb = (newRamGb * 10f).roundToInt() / 10f,
                ramPercent = newRamPct,
                cpuLoadPercent = (state.cpuLoadPercent - 8).coerceAtLeast(18),
                batteryTempCelsius = (state.batteryTempCelsius - 1.2f).coerceAtLeast(35.0f),
                lastGcFreedMb = freedMb,
                selectedJvmPresetId = "extreme_zgc",
                customJvmFlagsText = DefaultLauncherCatalog.jvmPresets[2].flags,
                statusBannerMessage = "Auto-Optimize Selesai: +${freedMb}MB RAM dibebaskan • ZGC Aktif • Suhu Turun 1.2 C"
            )
        }
        viewModelScope.launch {
            repository.setSelectedJvmPreset("extreme_zgc")
        }
        appendLog("[TurboBoost/INFO]: Executed RAM Purge + ZGC Low-Latency profile tuning")
    }

    fun startPreLaunchSequence(directServerIp: String? = null) {
        _uiState.update {
            it.copy(
                showPreLaunchDialog = true,
                preLaunchProgress = 0.08f,
                isGameRunningSimulated = false,
                preLaunchStageText = if (directServerIp != null) {
                    "Menyiapkan Direct Connect ke $directServerIp..."
                } else {
                    "Tahap 1/4: Membersihkan Heap RAM & Proses Latar..."
                }
            )
        }
        viewModelScope.launch {
            delay(450L)
            _uiState.update {
                it.copy(
                    preLaunchProgress = 0.36f,
                    preLaunchStageText = "Tahap 2/4: Memverifikasi LWJGL 3.3.6-zalith & ${it.selectedJreId} ARM64..."
                )
            }
            delay(500L)
            _uiState.update {
                it.copy(
                    preLaunchProgress = 0.72f,
                    preLaunchStageText = "Tahap 3/4: Menginisialisasi ${it.gpuRenderer} Pipeline..."
                )
            }
            delay(500L)
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
            appendLog("[GameSession/INFO]: Launched '${_uiState.value.currentInstance.title}' with ${_uiState.value.ramAllocationMb}MB RAM")
        }
    }

    fun dismissPreLaunchDialog() {
        _uiState.update { it.copy(showPreLaunchDialog = false, isGameRunningSimulated = false) }
    }

    fun emergencyForceKillGame() {
        _uiState.update {
            it.copy(
                showPreLaunchDialog = false,
                isGameRunningSimulated = false,
                ramUsedGb = 3.4f,
                ramPercent = 42,
                statusBannerMessage = "SIGKILL Terkirim: Proses JVM dihentikan paksa & memori dibersihkan"
            )
        }
        appendLog("[ProcessManager/WARN]: Sent SIGKILL (-9) to active Minecraft JVM process")
    }

    // Mods & Shaders Actions
    fun selectModsSubTab(subTab: ModsSubTab) {
        _uiState.update { it.copy(selectedModsSubTab = subTab, isModsFetching = true) }
        viewModelScope.launch {
            delay(300L)
            _uiState.update { it.copy(isModsFetching = false) }
        }
    }

    fun selectModPlatform(platform: ModPlatform) {
        _uiState.update { it.copy(selectedModPlatform = platform, isModsFetching = true) }
        viewModelScope.launch {
            delay(250L)
            _uiState.update { it.copy(isModsFetching = false) }
        }
    }

    fun selectModMcVersion(version: String) {
        _uiState.update { it.copy(selectedModMcVersion = version) }
    }

    fun updateModSearchQuery(query: String) {
        _uiState.update { it.copy(modSearchQuery = query) }
    }

    fun toggleModEnabled(modId: String) {
        _uiState.update { state ->
            val updated = state.modsList.map { mod ->
                if (mod.id == modId) mod.copy(isEnabled = !mod.isEnabled) else mod
            }
            state.copy(modsList = updated)
        }
        appendLog("[ModManager/INFO]: Toggled .jar state for mod id=$modId")
    }

    fun installModItem(modId: String) {
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
                modsList = updated,
                statusBannerMessage = "Paket berhasil diunduh & diaktifkan ke direktori .minecraft"
            )
        }
        appendLog("[ModInstaller/INFO]: Installed package $modId into active instance")
    }

    fun installFromLocalStorageSimulated(customFileName: String) {
        val cleanName = customFileName.ifBlank { "custom-fps-booster-1.21.4.jar" }
        val newMod = ModItem(
            id = "local_${System.currentTimeMillis()}",
            name = cleanName.removeSuffix(".jar").removeSuffix(".zip").replace("-", " ").uppercase(),
            author = "Local Storage Import",
            summary = "Paket lokal diimpor langsung dari penyimpanan internal perangkat.",
            category = ModsSubTab.INSTALLED,
            platform = ModPlatform.MODRINTH,
            mcVersion = "1.21.4",
            fileSize = "1.42 MB",
            downloads = "Local",
            fileNameJar = if (cleanName.endsWith(".jar")) cleanName else "$cleanName.jar",
            isInstalled = true,
            isEnabled = true,
            accentColor = CyberCyan
        )
        _uiState.update { state ->
            state.copy(
                modsList = listOf(newMod) + state.modsList,
                selectedModsSubTab = ModsSubTab.INSTALLED,
                statusBannerMessage = "File lokal '${newMod.fileNameJar}' berhasil dipasang"
            )
        }
        appendLog("[LocalImport/INFO]: Imported '${newMod.fileNameJar}' into .minecraft/mods")
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
                delay(180L)
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
        _uiState.update { it.copy(resolutionScale = scale.coerceIn(25, 200)) }
        viewModelScope.launch { repository.setResolutionScale(scale.coerceIn(25, 200)) }
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
        viewModelScope.launch { repository.setFullbright(enabled) }
        appendLog("[OptionsTxt/INFO]: gamma=${if (enabled) "10.0 (Fullbright 1000%)" else "1.0 (Vanilla)"}")
    }

    fun setFovValue(fov: Float) {
        _uiState.update { it.copy(fovValue = fov.coerceIn(30f, 110f)) }
        viewModelScope.launch { repository.setFovValue(fov.coerceIn(30f, 110f)) }
    }

    fun setRenderDistance(chunks: Int) {
        _uiState.update { it.copy(renderDistanceChunks = chunks.coerceIn(2, 32)) }
        viewModelScope.launch { repository.setRenderDistance(chunks.coerceIn(2, 32)) }
    }

    fun setVsync(enabled: Boolean) {
        _uiState.update { it.copy(vsyncEnabled = enabled) }
        viewModelScope.launch { repository.setVsync(enabled) }
    }

    fun setGuiScale(scale: Int) {
        _uiState.update { it.copy(guiScale = scale.coerceIn(1, 5)) }
        viewModelScope.launch { repository.setGuiScale(scale.coerceIn(1, 5)) }
    }

    fun setSmoothLighting(enabled: Boolean) {
        _uiState.update { it.copy(smoothLightingEnabled = enabled) }
    }

    fun setClouds(enabled: Boolean) {
        _uiState.update { it.copy(cloudsEnabled = enabled) }
        viewModelScope.launch { repository.setClouds(enabled) }
    }

    fun setParticlesMinimal(enabled: Boolean) {
        _uiState.update { it.copy(particlesMinimal = enabled) }
    }

    fun setEntityShadows(enabled: Boolean) {
        _uiState.update { it.copy(entityShadowsEnabled = enabled) }
        viewModelScope.launch { repository.setEntityShadows(enabled) }
    }

    fun setAutoJump(enabled: Boolean) {
        _uiState.update { it.copy(autoJumpEnabled = enabled) }
        viewModelScope.launch { repository.setAutoJump(enabled) }
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
        _uiState.update {
            it.copy(
                selectedServerHost = host,
                livePingMs = Random.nextInt(19, 34),
                statusBannerMessage = "Ping Radar diperbarui ke $host"
            )
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
        _uiState.update { state ->
            val updated = state.zalithReleaseAssets.map {
                if (it.id == assetId) it.copy(isInstalled = true) else it
            }
            state.copy(
                zalithReleaseAssets = updated,
                statusBannerMessage = "Paket rilis berhasil dipasang & diverifikasi ke zalith_data/"
            )
        }
        appendLog("[AssetInstaller/INFO]: Installed release package id=$assetId")
    }

    // Versions, Paths & Accounts
    fun installMinecraftVersion(versionId: String) {
        if (_uiState.value.downloadingVersionId != null) return
        _uiState.update { it.copy(downloadingVersionId = versionId, downloadVersionProgress = 0.1f) }
        viewModelScope.launch {
            for (step in 2..10) {
                delay(150L)
                _uiState.update { it.copy(downloadVersionProgress = step / 10f) }
            }
            _uiState.update { state ->
                val updated = state.installedVersions.map { ver ->
                    if (ver.id == versionId) ver.copy(isInstalled = true) else ver
                }
                state.copy(
                    installedVersions = updated,
                    downloadingVersionId = null,
                    downloadVersionProgress = 0f,
                    statusBannerMessage = "Versi berhasil diunduh ke .minecraft/versions & diverifikasi SHA-1"
                )
            }
        }
    }

    fun selectGameDirectory(dirId: String) {
        _uiState.update { state ->
            state.copy(
                gameDirectories = state.gameDirectories.map { it.copy(isActive = it.id == dirId) }
            )
        }
    }

    fun addGameDirectory(label: String, path: String) {
        val newDir = GameDirectoryPath(
            id = "dir_${System.currentTimeMillis()}",
            label = label.ifBlank { "Custom Game Directory" },
            path = path.ifBlank { "/storage/emulated/0/MattzCustomMC" },
            usedSpaceGb = 0.4f,
            instancesCount = 1,
            isActive = true
        )
        _uiState.update { state ->
            state.copy(
                gameDirectories = state.gameDirectories.map { it.copy(isActive = false) } + newDir,
                statusBannerMessage = "Path direktori '${newDir.label}' ditambahkan & diaktifkan"
            )
        }
    }

    fun selectAccount(accountId: String) {
        _uiState.update { state ->
            state.copy(
                accounts = state.accounts.map { it.copy(isActive = it.id == accountId) }
            )
        }
    }

    fun addAccount(username: String, isMicrosoft: Boolean) {
        val cleanUser = username.ifBlank { "MattzPlayer_${Random.nextInt(100, 999)}" }
        val newAcc = LauncherAccount(
            id = "acc_${System.currentTimeMillis()}",
            username = cleanUser,
            accountType = if (isMicrosoft) "Microsoft OAuth" else "Offline Account",
            uuid = "${Random.nextInt(10000000, 99999999)}-4b1d-4c2a-9e11-000000001415",
            primarySkinColor = if (isMicrosoft) PlatinumSilver else AmberGold,
            accentSkinColor = if (isMicrosoft) CyberCyan else AmethystPurple,
            capeName = if (isMicrosoft) "Microsoft Xbox Live Cape" else "Mattz Local Cloak",
            isActive = true
        )
        _uiState.update { state ->
            state.copy(
                accounts = state.accounts.map { it.copy(isActive = false) } + newAcc,
                statusBannerMessage = "Akun '$cleanUser' (${newAcc.accountType}) aktif"
            )
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
        val freed = Random.nextInt(240, 580)
        _uiState.update {
            it.copy(
                cleanedCacheMb = it.cleanedCacheMb + freed,
                storageFreeGb = ((it.storageFreeGb + freed / 1024f) * 10f).roundToInt() / 10f,
                crashDoctorStatus = "SEHAT 100% • LWJGL 3.3.6 & MobileGlues Aman • +${freed}MB Cache Dibersihkan",
                statusBannerMessage = "Crash Doctor & Cleaner: +${freed}MB Cache dibersihkan, 0 konflik mod"
            )
        }
        appendLog("[CrashDoctor/INFO]: Cleared ${freed}MB shader/mixin cache; verified 0 conflicts")
    }

    fun clearStatusBanner() {
        _uiState.update { it.copy(statusBannerMessage = null) }
    }

    fun appendLog(line: String) {
        _uiState.update { state ->
            state.copy(logcatLines = (state.logcatLines + line).takeLast(40))
        }
    }
}
