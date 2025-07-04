package cn.irina.core.manager

import cn.irina.core.Main
import cn.irina.core.Main.Companion.plugin
import cn.irina.core.data.TeleportPoint
import cn.irina.core.util.CC
import cn.irina.core.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/*
 * @Author Irina
 * @Date 2025/7/3 02:43
 */

object TeleportPointsManager: CoroutineScope {
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Default

    private val prefix = Main.PREFIX
    private val pointsMap = ConcurrentHashMap<String, TeleportPoint>()

    private fun Double.format(decimals: Int): String = String.format("%.${decimals}f", this)
    private fun Float.format(decimals: Int): String = String.format("%.${decimals}f", this)

    lateinit var teleportPointsFile: File
    lateinit var teleportPointsConfig: YamlConfiguration

    init {
        loadFile()
    }

    private fun loadFile() {
        launch {
            // 加载传送点Config
            teleportPointsFile = File(plugin.dataFolder, "TeleportPoints.yml")

            // 如果不存在, 则创建
            if (!teleportPointsFile.exists()) {
                try {
                    teleportPointsFile.createNewFile() // 尝试创建新文件
                    Log.send(CC.color("$prefix&a已创建 &f\"TeleportPoints.yml\""))
                } catch (e: Exception) {
                    Log.send(e.localizedMessage)
                    return@launch
                }
            }

            teleportPointsConfig = YamlConfiguration.loadConfiguration(teleportPointsFile)
            Log.send(CC.color("$prefix&fTeleportPoints.yml &a已加载"))
        }
    }

    fun saveConfig() {
        launch {
            try {
                teleportPointsConfig.save(teleportPointsFile) // 保存配置到文件
                Log.send(CC.color("$prefix&fTeleportPoints.yml &a保存完毕"))
            } catch (e: Exception) {
                Log.send(CC.color("$prefix&c无法保存 &fTeleportPoints.yml"))
                Log.send(e.localizedMessage)
            }
        }
    }


    private fun getConfigPointKeys(): Set<String> {
        return teleportPointsConfig.getKeys(false)
    }

    private fun getTeleportPoint(name: String): TeleportPoint? {
        if (!getConfigPointKeys().contains(name)) {
            Log.send(CC.color("$prefix&c呜诶...没有这个传送点诶......"))
            return null
        }

        val worldName = teleportPointsConfig.getString("$name.World") ?: ""

        val world = Bukkit.getWorld(worldName)
        if (world == null) {
            Log.send(CC.color("$prefix&c呜诶...你的世界名 &f\"$worldName\" &c好像不对哇......"))
            return null
        }

        val x = teleportPointsConfig.getDouble("$name.X")
        val y = teleportPointsConfig.getDouble("$name.Y")
        val z = teleportPointsConfig.getDouble("$name.Z")
        val yaw = teleportPointsConfig.getDouble("$name.Yaw").toFloat()
        val pitch = teleportPointsConfig.getDouble("$name.Pitch").toFloat()

        return TeleportPoint(worldName, x, y, z, yaw, pitch)
    }

    fun loadAllPoints() {
        val pointNames = getConfigPointKeys()
        if (pointNames.isEmpty()) {
            Log.send(CC.color("$prefix&f[&e模块: 传送&f] | &e你的传送点是空的, 真的假的?"))
            return
        }

        var loadIndex = 0
        for (key in pointNames) {
            val point = getTeleportPoint(key)

            if (point == null) {
                Log.send(CC.color("$prefix&f[&e模块: 传送&f] | &c传送点 &f\"$key\" &c为Null!"))
                continue
            }

            pointsMap[key] = point
            Log.send(CC.color("$prefix&f[&e模块: 传送&f] | 加载传送点: &b$key"))
            Log.send(CC.color("$prefix&f[&e模块: 传送&f] | 传送点 &b$key &f参数: &f${point.world}, " +
                    "&b${point.x.format(2)} &f| " +
                    "&b${point.y.format(2)} &f| " +
                    "&b${point.z.format(2)} &f| " +
                    "&b${point.yaw.format(2)} &f| " +
                    "&b${point.pitch.format(2)}",
            ))

            loadIndex++
        }

        Log.send(CC.color("$prefix&f[&e模块: 传送&f] | 总计加载了 &b$loadIndex &f个传送点!"))
    }

    fun getPointsMap(): ConcurrentHashMap<String, TeleportPoint> {
        return pointsMap
    }

    fun getPoint(name: String): TeleportPoint? {
        return pointsMap[name]
    }

    fun setPoint(name: String, loc: Location) {
        val point = TeleportPoint(loc.world!!.name, loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
        pointsMap[name] = point

        setTeleportPointsConfig(name, point)
    }

    fun removePoint(name: String) {
        pointsMap.remove(name)

        setTeleportPointsConfig(name, null)
    }

    private fun setTeleportPointsConfig(name: String, point: TeleportPoint?) {
        launch {
            teleportPointsConfig.set(name, point)
            teleportPointsConfig.set("$name.World", point)
            teleportPointsConfig.set("$name.X", point)
            teleportPointsConfig.set("$name.Y", point)
            teleportPointsConfig.set("$name.Z", point)
            teleportPointsConfig.set("$name.Yaw", point)
            teleportPointsConfig.set("$name.Pitch", point)

            saveConfig()
        }
    }
}