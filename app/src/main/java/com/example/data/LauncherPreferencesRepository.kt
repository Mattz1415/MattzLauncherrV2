package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.AccentThemeOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.launcherDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "mattz_launcher_prefs"
)

data class PersistedLauncherSettings(
    val accentTheme: AccentThemeOption = AccentThemeOption.PLATINUM_ASH,
    val selectedInstanceIndex: Int = 0,
    val ramAllocationMb: Int = 4608,
    val selectedJvmPresetId: String = "extreme_zgc",
    val selectedJreId: String = "JRE 21",
    val gpuRenderer: String = "MobileGlues (Vulkan 1.3)",
    val resolutionScale: Int = 100,
    val unlock120Hz: Boolean = true,
    val fpsHudOverlay: Boolean = true,
    val fullbrightEnabled: Boolean = true,
    val fovValue: Float = 90f,
    val renderDistanceChunks: Int = 12,
    val vsyncEnabled: Boolean = false,
    val cloudsEnabled: Boolean = false,
    val entityShadowsEnabled: Boolean = false,
    val autoJumpEnabled: Boolean = false,
    val guiScale: Int = 3
)

class LauncherPreferencesRepository(private val context: Context) {

    private object Keys {
        val ACCENT_THEME = stringPreferencesKey("accent_theme")
        val SELECTED_INSTANCE_INDEX = intPreferencesKey("selected_instance_index")
        val RAM_ALLOCATION_MB = intPreferencesKey("ram_allocation_mb")
        val SELECTED_JVM_PRESET = stringPreferencesKey("selected_jvm_preset")
        val SELECTED_JRE = stringPreferencesKey("selected_jre")
        val GPU_RENDERER = stringPreferencesKey("gpu_renderer")
        val RESOLUTION_SCALE = intPreferencesKey("resolution_scale")
        val UNLOCK_120HZ = booleanPreferencesKey("unlock_120hz")
        val FPS_HUD_OVERLAY = booleanPreferencesKey("fps_hud_overlay")
        val FULLBRIGHT = booleanPreferencesKey("fullbright_enabled")
        val FOV_VALUE = floatPreferencesKey("fov_value")
        val RENDER_DISTANCE = intPreferencesKey("render_distance_chunks")
        val VSYNC = booleanPreferencesKey("vsync_enabled")
        val CLOUDS = booleanPreferencesKey("clouds_enabled")
        val ENTITY_SHADOWS = booleanPreferencesKey("entity_shadows_enabled")
        val AUTO_JUMP = booleanPreferencesKey("auto_jump_enabled")
        val GUI_SCALE = intPreferencesKey("gui_scale")
    }

    val settingsFlow: Flow<PersistedLauncherSettings> = context.launcherDataStore.data.map { prefs ->
        PersistedLauncherSettings(
            accentTheme = AccentThemeOption.fromId(prefs[Keys.ACCENT_THEME] ?: AccentThemeOption.PLATINUM_ASH.id),
            selectedInstanceIndex = prefs[Keys.SELECTED_INSTANCE_INDEX] ?: 0,
            ramAllocationMb = prefs[Keys.RAM_ALLOCATION_MB] ?: 4608,
            selectedJvmPresetId = prefs[Keys.SELECTED_JVM_PRESET] ?: "extreme_zgc",
            selectedJreId = prefs[Keys.SELECTED_JRE] ?: "JRE 21",
            gpuRenderer = prefs[Keys.GPU_RENDERER] ?: "MobileGlues (Vulkan 1.3)",
            resolutionScale = prefs[Keys.RESOLUTION_SCALE] ?: 100,
            unlock120Hz = prefs[Keys.UNLOCK_120HZ] ?: true,
            fpsHudOverlay = prefs[Keys.FPS_HUD_OVERLAY] ?: true,
            fullbrightEnabled = prefs[Keys.FULLBRIGHT] ?: true,
            fovValue = prefs[Keys.FOV_VALUE] ?: 90f,
            renderDistanceChunks = prefs[Keys.RENDER_DISTANCE] ?: 12,
            vsyncEnabled = prefs[Keys.VSYNC] ?: false,
            cloudsEnabled = prefs[Keys.CLOUDS] ?: false,
            entityShadowsEnabled = prefs[Keys.ENTITY_SHADOWS] ?: false,
            autoJumpEnabled = prefs[Keys.AUTO_JUMP] ?: false,
            guiScale = prefs[Keys.GUI_SCALE] ?: 3
        )
    }

    suspend fun setAccentTheme(option: AccentThemeOption) {
        context.launcherDataStore.edit { it[Keys.ACCENT_THEME] = option.id }
    }

    suspend fun setSelectedInstanceIndex(index: Int) {
        context.launcherDataStore.edit { it[Keys.SELECTED_INSTANCE_INDEX] = index }
    }

    suspend fun setRamAllocationMb(ramMb: Int) {
        context.launcherDataStore.edit { it[Keys.RAM_ALLOCATION_MB] = ramMb }
    }

    suspend fun setSelectedJvmPreset(presetId: String) {
        context.launcherDataStore.edit { it[Keys.SELECTED_JVM_PRESET] = presetId }
    }

    suspend fun setSelectedJre(jreId: String) {
        context.launcherDataStore.edit { it[Keys.SELECTED_JRE] = jreId }
    }

    suspend fun setGpuRenderer(renderer: String) {
        context.launcherDataStore.edit { it[Keys.GPU_RENDERER] = renderer }
    }

    suspend fun setResolutionScale(scale: Int) {
        context.launcherDataStore.edit { it[Keys.RESOLUTION_SCALE] = scale }
    }

    suspend fun setUnlock120Hz(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.UNLOCK_120HZ] = enabled }
    }

    suspend fun setFpsHudOverlay(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.FPS_HUD_OVERLAY] = enabled }
    }

    suspend fun setFullbright(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.FULLBRIGHT] = enabled }
    }

    suspend fun setFovValue(fov: Float) {
        context.launcherDataStore.edit { it[Keys.FOV_VALUE] = fov }
    }

    suspend fun setRenderDistance(chunks: Int) {
        context.launcherDataStore.edit { it[Keys.RENDER_DISTANCE] = chunks }
    }

    suspend fun setVsync(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.VSYNC] = enabled }
    }

    suspend fun setClouds(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.CLOUDS] = enabled }
    }

    suspend fun setEntityShadows(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.ENTITY_SHADOWS] = enabled }
    }

    suspend fun setAutoJump(enabled: Boolean) {
        context.launcherDataStore.edit { it[Keys.AUTO_JUMP] = enabled }
    }

    suspend fun setGuiScale(scale: Int) {
        context.launcherDataStore.edit { it[Keys.GUI_SCALE] = scale }
    }
}
