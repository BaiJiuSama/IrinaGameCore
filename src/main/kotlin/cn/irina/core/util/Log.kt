package cn.irina.core.util

import org.bukkit.Bukkit

/*
 * @Author Irina
 * @Date 2025/7/2 02:14
 */

object Log {
    fun send(str: String) {
        Bukkit.getConsoleSender().sendMessage(CC.color(str))
    }

    fun send(list: List<String>) {
        for (str in list) send(str)
    }
}