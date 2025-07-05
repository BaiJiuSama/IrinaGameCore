package cn.irina.core.manager

import cn.irina.core.Main
import cn.irina.core.Main.Companion.plugin
import cn.irina.core.util.CC
import cn.irina.core.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/*
 * @Author Irina
 * @Date 2025/7/3 20:17
 */

object ConfigManager: CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Default

    private val prefix = Main.PREFIX

    lateinit var configFile: File
    lateinit var config: YamlConfiguration

    init {
        loadConfig()
    }

    fun loadConfig() {
        launch {
            configFile = File(plugin.dataFolder, "config.yml")

            if (!configFile.exists()) {
                try {
                    configFile.createNewFile()
                    Log.send(CC.color("$prefix&a已创建 &f\"Config.yml\""))
                } catch (e: Exception) {
                    Log.send(e.localizedMessage)
                    return@launch
                }
            }

            config = YamlConfiguration.loadConfiguration(configFile)
            Log.send(CC.color("$prefix&fConfig.yml &a已加载"))
        }
    }

    fun saveConfig(config: YamlConfiguration) {
        launch {
            try {
                config.save(configFile)
                Log.send(CC.color("$prefix&fConfig.yml &a保存完毕"))
            } catch (e: Exception) {
                Log.send(CC.color("$prefix&c无法保存 &fConfig.yml"))
                Log.send(e.localizedMessage)
            }
        }
    }
}