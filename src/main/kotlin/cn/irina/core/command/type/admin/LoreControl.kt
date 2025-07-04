package cn.irina.core.command.type.admin

import cn.irina.core.Main
import cn.irina.core.PermissionsHandler
import cn.irina.core.util.CC
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.annotation.Optional
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.bukkit.annotation.FallbackPrefix
import kotlin.collections.listOf
import kotlin.getValue

/*
 * @Author Irina
 * @Date 2025/7/2 03:02
 */

@FallbackPrefix("irina")
@Command("lc")
@CommandPermission(PermissionsHandler.ADMIN)
class LoreControl {
    private val prefix = Main.PREFIX

    @CommandPlaceholder
    fun onCommand(
        actor: BukkitCommandActor
    ) {
        val myself = actor.requirePlayer()

        showHelp(myself)
    }

    @Subcommand("help")
    fun handleHelp(actor: BukkitCommandActor) {
        val myself = actor.requirePlayer()

        showHelp(myself)
    }

    @Subcommand("name")
    fun handleSetName(
        actor: BukkitCommandActor,
        @Optional name: String,
    ) {
        val myself = actor.requirePlayer()

        if (!checkHandItemNotEmpty(myself)) return
        val handItem = myself.inventory.itemInMainHand

        val meta = handItem.itemMeta ?: Bukkit.getItemFactory().getItemMeta(handItem.type)!!
        meta.setDisplayName(CC.color(name))
        handItem.itemMeta = meta

        myself.inventory.setItemInMainHand(handItem)
        myself.sendMessage(CC.color("$prefix&a成功!"))
    }

    @Subcommand("add")
    fun handleAdd(
        actor: BukkitCommandActor,
        @Optional arg: String
    ) {
        val myself = actor.requirePlayer()

        if (!checkHandItemNotEmpty(myself)) return
        val handItem = myself.inventory.itemInMainHand

        val meta = handItem.itemMeta ?: Bukkit.getItemFactory().getItemMeta(handItem.type)!!
        val lore = meta.lore ?: mutableListOf<String>()

        lore.add(CC.color(arg))

        meta.lore = lore
        handItem.itemMeta = meta

        myself.inventory.setItemInMainHand(handItem)
        myself.sendMessage(CC.color("$prefix&a成功!"))
    }

    @Subcommand("set")
    fun handleSet(
        actor: BukkitCommandActor,
        @Optional line: Int,
        @Optional arg: String,
    ) {
        val myself = actor.requirePlayer()

        if (!checkHandItemNotEmpty(myself)) return
        val handItem = myself.inventory.itemInMainHand

        val meta = handItem.clone().itemMeta ?: Bukkit.getItemFactory().getItemMeta(handItem.type)!!
        val lore = meta.lore ?: mutableListOf<String>()

        if (lore.size < line || lore[line - 1] == null) {
            myself.sendMessage(CC.color("$prefix&c你的物品描述并未达到此行数!"))
            return
        }

        lore[line] = CC.color(arg)

        meta.lore = lore
        handItem.itemMeta = meta

        myself.inventory.setItemInMainHand(handItem)
        myself.sendMessage(CC.color("$prefix&a成功!"))
    }

    @Subcommand("del")
    fun handleDel(
        actor: BukkitCommandActor,
        @Optional line: Int
    ) {
        val myself = actor.requirePlayer()

        if (!checkHandItemNotEmpty(myself)) return
        val handItem = myself.inventory.itemInMainHand

        val meta = handItem.clone().itemMeta ?: Bukkit.getItemFactory().getItemMeta(handItem.type)!!
        val lore = meta.lore ?: mutableListOf<String>()

        if (lore.size < line || lore[line - 1] == null) {
            myself.sendMessage(CC.color("$prefix&c你的物品描述并未达到此行数!"))
            return
        }

        lore.removeAt(line - 1)

        meta.lore = lore
        handItem.itemMeta = meta

        myself.inventory.setItemInMainHand(handItem)
        myself.sendMessage(CC.color("$prefix&a成功!"))
    }

    @Subcommand("lastDel")
    fun handleLastDel(
        actor: BukkitCommandActor
    ) {
        val myself = actor.requirePlayer()

        if (!checkHandItemNotEmpty(myself)) return
        val handItem = myself.inventory.itemInMainHand

        val meta = handItem.clone().itemMeta ?: Bukkit.getItemFactory().getItemMeta(handItem.type)!!
        val lore = meta.lore ?: mutableListOf<String>()

        if (lore.isEmpty()) {
            myself.sendMessage(CC.color("$prefix&c你的物品描述并未达到此行数!"))
            return
        }

        lore.removeAt(lore.size - 1)

        meta.lore = lore
        handItem.itemMeta = meta

        myself.inventory.setItemInMainHand(handItem)
        myself.sendMessage(CC.color("$prefix&a成功!"))
    }

    @Subcommand("clear")
    fun handleClear(
        actor: BukkitCommandActor
    ) {
        val myself = actor.requirePlayer()

        if (!checkHandItemNotEmpty(myself)) return
        val handItem = myself.inventory.itemInMainHand

        val meta = Bukkit.getItemFactory().getItemMeta(handItem.type)!!

        handItem.itemMeta = meta

        myself.inventory.setItemInMainHand(handItem)
        myself.sendMessage(CC.color("$prefix&a成功!"))
    }

    private fun checkHandItemNotEmpty(player: Player): Boolean {
        val handItem = player.inventory.itemInMainHand

        if (handItem.type == Material.AIR) {
            player.sendMessage(CC.color("$prefix&c手持物不得为空!"))
            return false
        }

        return true
    }

    private fun showHelp(myself: Player) {
        CC.color(helpList).forEach { myself.sendMessage(it) }
    }

    private val helpList by lazy {
        val barChar = "&f&m               &r &#00ffffI&#15ffffr&#2bffffi&#40ffffn&#55ffffa&#6affffG&#80ffffa&#95ffffm&#aaffffe&#bfffffC&#d5ffffo&#eaffffr&#ffffffe &7- &8&oLoreControl &f&m               &r"

        listOf(
            "",
            barChar,
            "",
            "&7展示此文本",
            "&f/lc help | /lc",
            "",
            "&7编辑手持物名称",
            "&f/lc name &8<String>",
            "",
            "&7将手持物的描述增加一行",
            "&f/lc add &8<String>",
            "",
            "&7设置手持物的某一行描述",
            "&f/lc set &8<Line> <String>",
            "",
            "&7删除手持物的某一行描述",
            "&f/lc del &8<line>",
            "",
            "&7删除手持物的最下一行描述",
            "&f/lc lastDel",
            "",
            "&7一键清除手持物描述",
            "&f/lc clear",
            "",
            barChar,
            ""
        )
    }
}