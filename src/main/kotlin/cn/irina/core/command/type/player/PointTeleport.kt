package cn.irina.core.command.type.player

import cn.irina.core.Main
import cn.irina.core.util.CC
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.annotation.Optional
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.FallbackPrefix

/*
 * @Author Irina
 * @Date 2025/7/3 16:13
 */

@FallbackPrefix("irina")
@Command("tpp")
class PointTeleport {
    private val prefix = Main.PREFIX
    private val teleportPointsManager = Main.teleportManager

    private fun Player.sendColoredMessage(str: String) { this.sendMessage(CC.color(str)) }

    companion object {
        private const val HEADER_LINE = "&f&m               &r &#00ffffI&#15ffffr&#2bffffi&#40ffffn&#55ffffa&#6affffG&#80ffffa&#95ffffm&#aaffffe&#bfffffC&#d5ffffo&#eaffffr&#ffffffe &7- &8&oTeleport &f&m               &r"
    }

    @CommandPlaceholder
    fun onCommand(actor: BukkitCommandActor, @Optional name: String?) {
        val player = actor.requirePlayer()

        if (name.isNullOrEmpty() || name.uppercase() == "HELP") {
            getHelpMessage.forEach { player.sendColoredMessage(it) }
            return
        }

        val point = teleportPointsManager.getPoint(name)
        if (point == null) {
            player.sendColoredMessage("$prefix&c未知的传送点!")
            return
        }

        val targetLocation = Location(Bukkit.getWorld(point.world), point.x, point.y, point.z, point.yaw, point.pitch)
        player.teleport(targetLocation)
        player.sendColoredMessage("$prefix&a成功!")
    }

    private val getHelpMessage = listOf(
        "",
        HEADER_LINE,
        "",
        "&7传送到目标传送点",
        "&f/tpp &8<Point>",
        "",
        HEADER_LINE,
        ""
    )
}