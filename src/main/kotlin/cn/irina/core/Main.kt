package cn.irina.core

import cn.irina.core.command.CommandHandler
import cn.irina.core.manager.ConfigManager
import cn.irina.core.manager.TeleportPointsManager
import cn.irina.core.util.CC
import cn.irina.core.util.Log
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.io.File

// paper你M没了。

class Main : JavaPlugin() {
    companion object {
        val PREFIX = CC.color("&8[&#00ffffI&#40ffffR&#80ffffI&#bfffffN&#ffffffA&8] &f|&r ")

        @JvmStatic
        lateinit var instance: Main
            private set

        @JvmStatic
        lateinit var plugin: JavaPlugin
            private set

        lateinit var teleportManager: TeleportPointsManager
        lateinit var configManager: ConfigManager
    }

    init {
        instance = this
        plugin = this

        teleportManager = TeleportPointsManager
        configManager = ConfigManager
    }

    val depends = listOf(
        "PlaceholderAPI"
    )

    override fun onEnable() {
        instance = this

        Log.send("$PREFIX&f游戏核心启动中...")

        depends.forEach {
            if (Bukkit.getPluginManager().getPlugin(it) != null) return@forEach
            Log.send("$PREFIX&c前置 &f$it &c缺失, 将自动关闭此插件!")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        initFiles()
        initListener()
        initCommandHandler()
        initTeleportPoints()
    }

    override fun onDisable() {
        Log.send("$PREFIX&f游戏核心关闭中...")

        unLoadListener()
        CommandHandler.unLoad()
    }

    private fun initFiles() {
        if (!dataFolder.exists()) dataFolder.mkdirs()

        saveResource("TeleportPoints.yml", false)
        saveResource("config.yml", true)
    }

    private fun initTeleportPoints() {
        teleportManager.loadAllPoints()
    }

    private fun initCommandHandler() {
        CommandHandler.load()
    }

    private fun initListener() {
        val classes = Reflections("cn.irina.core").getSubTypesOf(Listener::class.java) ?: return

        if (classes.isEmpty()) {
            Log.send("$PREFIX&f[&e模块: 事件&f] &7| &e未检测到事件类, 已跳过...")
            return
        }

        for (clazz in classes) {
            if (!Listener::class.java.isAssignableFrom(clazz!!)) continue

            Log.send("$PREFIX&f[&e模块: 事件&f] &7| 注册事件: &e${clazz.simpleName}")
            Bukkit.getPluginManager().registerEvents(clazz.getDeclaredConstructor().newInstance() as Listener, this)
        }
    }

    private fun unLoadListener() {
        HandlerList.unregisterAll(this)
    }
}
