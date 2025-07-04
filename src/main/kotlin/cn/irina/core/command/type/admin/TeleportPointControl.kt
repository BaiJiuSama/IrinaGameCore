package cn.irina.core.command.type.admin

import cn.irina.core.Main
import cn.irina.core.PermissionsHandler
import cn.irina.core.data.TeleportPoint
import cn.irina.core.util.CC
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.annotation.Optional
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.bukkit.annotation.FallbackPrefix

/*
 * @Author Irina
 * @Date 2025/7/2 22:18
 */

@FallbackPrefix("irina")
@Command("tpc")
@CommandPermission(PermissionsHandler.ADMIN)
class TeleportPointControl {
    private val prefix = Main.PREFIX
    private val teleportPointsManager = Main.teleportManager

    private fun Double.format(decimals: Int): String = String.format("%.${decimals}f", this)
    private fun Float.format(decimals: Int): String = String.format("%.${decimals}f", this)
    private fun Player.sendColoredMessage(str: String) { this.sendMessage(CC.color(str)) }

    companion object {
        private const val HIGHLIGHT_COLOR = "&#00FFD7"
        private const val HEADER_LINE = "&f&m               &r &#00ffffI&#15ffffr&#2bffffi&#40ffffn&#55ffffa&#6affffG&#80ffffa&#95ffffm&#aaffffe&#bfffffC&#d5ffffo&#eaffffr&#ffffffe &7- &8&oTeleportPointControl &f&m               &r"
        private const val DECORATION_LINE = "&f&m                                             &r"
    }

    @CommandPlaceholder
    fun onCommand(actor: BukkitCommandActor) {
        showHelp(actor.requirePlayer())
    }

    @Subcommand("help")
    fun handleHelp(actor: BukkitCommandActor) {
        showHelp(actor.requirePlayer())
    }

    @Subcommand("info")
    fun handleInfo(actor: BukkitCommandActor, @Optional name: String?) {
        val myself = actor.requirePlayer()

        if (name.isNullOrBlank()) {
            myself.sendColoredMessage("$prefix&c请指定传送点名称!")
            return
        }

        val point = teleportPointsManager.getPoint(name)
        if (point == null) {
            myself.sendColoredMessage("$prefix&c目标传送点不存在!")
            return
        }

        pointInfo(name, point).forEach { myself.sendColoredMessage(it) }
    }

    private fun pointInfo(name: String, point: TeleportPoint): List<String> {
        return listOf(
            "",
            DECORATION_LINE,
            "",
            "&f传送点名称: $HIGHLIGHT_COLOR$name",
            "&f世界: ${point.world}",
            "&f坐标: $HIGHLIGHT_COLOR${point.x.format(2)} &f| " +
                    "$HIGHLIGHT_COLOR${point.y.format(2)} &f| " +
                    "$HIGHLIGHT_COLOR${point.z.format(2)} &f| " +
                    "$HIGHLIGHT_COLOR${point.yaw.format(2)} &f| " +
                    "$HIGHLIGHT_COLOR${point.pitch.format(2)}",
            "",
            DECORATION_LINE,
            ""
        )
    }

    @Subcommand("list")
    fun handleList(actor: BukkitCommandActor) {
        pointsList().forEach { actor.requirePlayer().sendColoredMessage(it) }
    }

    private fun pointsList(): List<String> {
        val points = teleportPointsManager.getPointsMap().keys

        return buildList {
            add("")
            add(DECORATION_LINE)
            add("")

            if (points.isEmpty()) {
                add("&f暂无传送点")
            } else {
                points.forEach { add("&f传送点: $HIGHLIGHT_COLOR$it") }
            }

            add("")
            add(DECORATION_LINE)
            add("")
        }
    }

    @Subcommand("add <name>")
    fun handleAdd(actor: BukkitCommandActor, @Optional name: String?) {
        val myself = actor.requirePlayer()

        if (name.isNullOrBlank()) {
            myself.sendColoredMessage("$prefix&c请指定传送点名称!")
            return
        }

        val point = teleportPointsManager.getPoint(name)
        if (point != null) {
            myself.sendColoredMessage("$prefix&c呜诶...已经有这个传送点了诶......")
            return
        }

        teleportPointsManager.setPoint(name.replace(" ", ""), myself.location)
        myself.sendColoredMessage("$prefix&a成功创建传送点 ${name.replace(" ", "")}!")
    }
    
    @Subcommand("reset <name>")
    fun handleReset(actor: BukkitCommandActor, @Optional name: String?) {
        val myself = actor.requirePlayer()

        if (name.isNullOrBlank()) {
            myself.sendColoredMessage("$prefix&c请指定传送点名称!")
            return
        }

        teleportPointsManager.setPoint(name, myself.location)
        myself.sendColoredMessage("$prefix&a成功!")
    }

    @Subcommand("del")
    fun handleDel(actor: BukkitCommandActor, @Optional name: String?) {
        val myself = actor.requirePlayer()

        if (name.isNullOrBlank()) {
            myself.sendColoredMessage("$prefix&c请指定传送点名称!")
            return
        }

        val point = teleportPointsManager.getPoint(name)
        if (point == null) {
            myself.sendColoredMessage("$prefix&c呜诶...这个传送点不存在诶......")
            return
        }

        teleportPointsManager.removePoint(name)
        myself.sendColoredMessage("$prefix&a成功!")
    }

    private fun showHelp(myself: Player) {
        getHelpMessages.forEach { myself.sendColoredMessage(it) }
    }

    private val getHelpMessages = listOf(
        "",
        HEADER_LINE,
        "",
        "&8设置传送点指令仅获取自身位置作为传送点",
        "",
        "&7展示此文本",
        "&f/tpc help | /tpc",
        "",
        "&7查看所有传送点",
        "&f/tpc list",
        "",
        "&7查看目标传送点",
        "&f/tpc info &8<Name>",
        "",
        "&7重设目标传送点",
        "&f/tpc reset &8<Name>",
        "",
        "&7删除目标传送点",
        "&f/tpc del &8<Name>",
        "",
        "&7添加目标传送点",
        "&f/tpc add &8<Name>",
        "",
        HEADER_LINE,
        ""
    )
}