package cn.irina.core.listeners

import cn.irina.core.Main
import cn.irina.core.util.CC
import cn.irina.core.util.PlaceholderAPIUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/*
 * @Author Irina
 * @Date 2025/7/3 20:06
 */

class PlayerListener: Listener {
    private val prefix = Main.PREFIX
    private val config = Main.configManager.config

    private fun Player.sendColoredMessage(str: String) { this.sendMessage(CC.color(str)) }

    @EventHandler
    fun onJoin(evt: PlayerJoinEvent) {
        config.getString("Message.JoinAnnouncer")?.let {
            evt.joinMessage = null
            Bukkit.broadcastMessage(CC.color(PlaceholderAPIUtil.parse(evt.player, it)))
        }
    }

    @EventHandler
    fun onQuit(evt: PlayerQuitEvent) {
        config.getString("Message.QuitAnnouncer")?.let {
            evt.quitMessage = null
            Bukkit.broadcastMessage(CC.color(PlaceholderAPIUtil.parse(evt.player, it)))
        }
        evt.player.respawnLocation
    }
}