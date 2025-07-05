package cn.irina.core.command.type.admin

import cn.irina.core.Main
import cn.irina.core.PermissionsHandler
import cn.irina.core.util.CC
import org.bukkit.GameMode
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.annotation.Default
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.bukkit.annotation.FallbackPrefix

/*
 * @Author Irina
 * @Date 2025/7/5 10:38
 */

@FallbackPrefix("irina")
@Command("gm")
@CommandPermission(PermissionsHandler.ADMIN)
class GameModeControl {
    companion object {
        private const val HEADER_LINE = "&f&m               &r &#00ffffI&#15ffffr&#2bffffi&#40ffffn&#55ffffa&#6affffG&#80ffffa&#95ffffm&#aaffffe&#bfffffC&#d5ffffo&#eaffffr&#ffffffe &7- &8&oGameModeControl &f&m               &r"
    }

    private val prefix = Main.PREFIX
    private fun Player.sendColoredMessage(str: String) { this.sendMessage(CC.color(str)) }
    private fun BukkitCommandActor.sendColoredMessage(str: String) { this.sender().sendMessage(CC.color(str)) }

    @CommandPlaceholder
    fun changeMode(actor: BukkitCommandActor, mode: Int?, @Default("me") player: Player) {
        if (mode == null) {
            sendHelp(actor.requirePlayer())
            return
        }

        val gameMode = getModeByNum(mode)
        if (gameMode == null) {
            actor.sendColoredMessage("$prefix&c非法的Mode数值! &f< 0 / 1 / 2 / 3 >")
            return
        }

        player.gameMode = gameMode
        actor.sendColoredMessage("$prefix&a成功!")
        if (actor.requirePlayer() != player) player.sendColoredMessage("$prefix&f您的游戏模式已被 &b${actor.name()} &f变更")
    }

    private fun getModeByNum(num: Int): GameMode? {
        return when (num) {
            0 -> GameMode.SURVIVAL
            1 -> GameMode.CREATIVE
            2 -> GameMode.ADVENTURE
            3 -> GameMode.SPECTATOR
            else -> null
        }
    }

    private fun sendHelp(player: Player) {
        CC.color(helpMessage).forEach { player.sendColoredMessage(it) }
    }
    private val helpMessage = listOf(
        "",
        HEADER_LINE,
        "",
        "&7切换游戏模式",
        "&f/gm &8< 0 / 1 / 2 / 3 >",
        "",
        "&7切换他人游戏模式",
        "&f/gm &8< 0 / 1 / 2 / 3 > <player>",
        "",
        HEADER_LINE,
        ""
    )
}