package cn.irina.core.command

import cn.irina.core.Main
import cn.irina.core.command.exceptions.CommandExceptionHandler
import cn.irina.core.command.type.admin.GameModeControl
import cn.irina.core.command.type.admin.LoreControl
import cn.irina.core.command.type.admin.TeleportPointControl
import cn.irina.core.command.type.player.PointTeleport
import cn.irina.core.util.Log
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import revxrsal.commands.bukkit.BukkitLamp

/*
 * @Author Irina
 * @Date 2025/7/2 02:48
 */

object CommandHandler {
    private val prefix = Main.PREFIX
    private val plugin = Main.plugin

    private val lamp = BukkitLamp.builder(plugin)
        .exceptionHandler(CommandExceptionHandler())
        .suggestionProviders { providers ->
            providers.addProvider(Player::class.java) { _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
        }
        .build()

    fun load() {
        val commands = listOf(
            LoreControl(),
            GameModeControl(),

            TeleportPointControl(),
            PointTeleport()
        )

        if (commands.isEmpty()) {
            Log.send("$prefix&f[&e模块: 指令&f] &7| &e未检测到指令类, 已跳过...")
            return
        }

        Log.send("$prefix&f[&e模块: 指令&f] &7| &f检测到 &b${commands.size} &f个指令类, 注册中...")

        try {
            for (cmd in commands) {
                lamp.register(cmd)
            }
            Log.send("$prefix&f[&e模块: 指令&f] &7| &f注册完成!")
        } catch (e: Exception) {
            e.printStackTrace()
        } }

    fun unLoad() {
        lamp.unregisterAllCommands()
        Log.send("$prefix&f[&e模块: 指令] | 卸载完成!")
    }
}