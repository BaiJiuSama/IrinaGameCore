package cn.irina.core.util

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/*
 * @Author Irina
 * @Date 2025/7/3 20:09
 */

object PlaceholderAPIUtil {
    @JvmStatic
    fun parse(player: Player, text: String): String {
        return if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPI.setPlaceholders(player, text)
        } else {
            text
        }
    }
}