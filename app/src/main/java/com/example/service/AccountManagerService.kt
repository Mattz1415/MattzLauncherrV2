package com.example.service

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.model.LauncherAccount
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmethystPurple
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PlatinumSilver
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.UUID

object AccountManagerService {

    private const val ACCOUNTS_FILE_NAME = "launcher_accounts.json"

    fun loadAccounts(context: Context): List<LauncherAccount> {
        val file = File(context.filesDir, ACCOUNTS_FILE_NAME)
        if (!file.exists()) {
            val defaults = getDefaultAccounts()
            saveAccounts(context, defaults)
            return defaults
        }

        return try {
            val jsonStr = file.readText()
            val array = JSONArray(jsonStr)
            val list = mutableListOf<LauncherAccount>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    LauncherAccount(
                        id = obj.getString("id"),
                        username = obj.getString("username"),
                        accountType = obj.getString("accountType"),
                        uuid = obj.getString("uuid"),
                        primarySkinColor = colorFromHex(obj.optString("primarySkinColor", "#E2E8F0")),
                        accentSkinColor = colorFromHex(obj.optString("accentSkinColor", "#00F0FF")),
                        capeName = obj.optString("capeName", "Mattz Nexus Cape"),
                        isActive = obj.optBoolean("isActive", false)
                    )
                )
            }
            if (list.isEmpty()) {
                val defaults = getDefaultAccounts()
                saveAccounts(context, defaults)
                defaults
            } else {
                // Ensure at least one account is active
                if (list.none { it.isActive }) {
                    list[0] = list[0].copy(isActive = true)
                }
                list
            }
        } catch (_: Exception) {
            val defaults = getDefaultAccounts()
            saveAccounts(context, defaults)
            defaults
        }
    }

    fun saveAccounts(context: Context, accounts: List<LauncherAccount>) {
        try {
            val array = JSONArray()
            accounts.forEach { acc ->
                val obj = JSONObject().apply {
                    put("id", acc.id)
                    put("username", acc.username)
                    put("accountType", acc.accountType)
                    put("uuid", acc.uuid)
                    put("primarySkinColor", colorToHex(acc.primarySkinColor))
                    put("accentSkinColor", colorToHex(acc.accentSkinColor))
                    put("capeName", acc.capeName)
                    put("isActive", acc.isActive)
                }
                array.put(obj)
            }
            val file = File(context.filesDir, ACCOUNTS_FILE_NAME)
            file.writeText(array.toString(2))
        } catch (_: Exception) {}
    }

    fun createAccount(context: Context, username: String, isMicrosoft: Boolean): List<LauncherAccount> {
        val current = loadAccounts(context).toMutableList()
        val trimmed = username.trim().ifBlank { "Player_${System.currentTimeMillis() % 1000}" }

        // Generate standard Minecraft offline UUID if not Microsoft
        val uuid = if (isMicrosoft) {
            UUID.randomUUID().toString()
        } else {
            UUID.nameUUIDFromBytes("OfflinePlayer:$trimmed".toByteArray(Charsets.UTF_8)).toString()
        }

        val colors = listOf(PlatinumSilver, CyberCyan, AmberGold, EmeraldGreen, AmethystPurple, CrimsonRed)
        val pColor = colors[Math.abs(trimmed.hashCode()) % colors.size]
        val aColor = colors[(Math.abs(trimmed.hashCode()) + 1) % colors.size]

        // Mark previous active accounts false
        val updated = current.map { it.copy(isActive = false) }.toMutableList()

        val newAcc = LauncherAccount(
            id = "acc_${System.currentTimeMillis()}",
            username = trimmed,
            accountType = if (isMicrosoft) "Microsoft OAuth" else "Offline Account",
            uuid = uuid,
            primarySkinColor = pColor,
            accentSkinColor = aColor,
            capeName = if (isMicrosoft) "Migrator & Mattz Nexus Cape" else "Nexus Dev Cloak",
            isActive = true
        )
        updated.add(0, newAcc)
        saveAccounts(context, updated)
        return updated
    }

    fun setActiveAccount(context: Context, accountId: String): List<LauncherAccount> {
        val current = loadAccounts(context)
        val updated = current.map { it.copy(isActive = (it.id == accountId)) }
        saveAccounts(context, updated)
        return updated
    }

    fun selectAccount(context: Context, accountId: String): List<LauncherAccount> =
        setActiveAccount(context, accountId)

    fun removeAccount(context: Context, accountId: String): List<LauncherAccount> {
        val current = loadAccounts(context).filter { it.id != accountId }
        val updated = if (current.none { it.isActive } && current.isNotEmpty()) {
            current.mapIndexed { idx, it -> it.copy(isActive = (idx == 0)) }
        } else current

        saveAccounts(context, updated)
        return updated
    }

    fun deleteAccount(context: Context, accountId: String): List<LauncherAccount> =
        removeAccount(context, accountId)

    private fun getDefaultAccounts(): List<LauncherAccount> {
        val defaultOfflineUuid = UUID.nameUUIDFromBytes("OfflinePlayer:Mattz1415".toByteArray(Charsets.UTF_8)).toString()
        return listOf(
            LauncherAccount(
                id = "acc_mattz_ms",
                username = "Mattz1415",
                accountType = "Offline Account",
                uuid = defaultOfflineUuid,
                primarySkinColor = PlatinumSilver,
                accentSkinColor = CyberCyan,
                capeName = "Migrator & Mattz Nexus Cape",
                isActive = true
            )
        )
    }

    private fun colorToHex(color: Color): String {
        val r = (color.red * 255).toInt().coerceIn(0, 255)
        val g = (color.green * 255).toInt().coerceIn(0, 255)
        val b = (color.blue * 255).toInt().coerceIn(0, 255)
        return String.format("#%02X%02X%02X", r, g, b)
    }

    private fun colorFromHex(hex: String): Color {
        return try {
            val cleanHex = hex.removePrefix("#")
            val colorInt = cleanHex.toLong(16).toInt() or -0x1000000
            Color(colorInt)
        } catch (_: Exception) {
            Color.White
        }
    }
}
