package cn.irina.core.util

import org.bukkit.ChatColor
import java.util.ArrayList
import java.util.regex.Pattern

/*
 * @Author Irina
 * @Date 2025/7/2 02:13
 */

object CC {
    fun color(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(text))
    }

    fun color(lines: List<String>): List<String> {
        val toReturn: MutableList<String> = ArrayList<String>()

        for (line in lines)
            toReturn.add(color(line))

        return toReturn
    }

    private fun translateHexColorCodes(message: String): String {
        val colorChar = ChatColor.COLOR_CHAR

        val matcher = Pattern.compile("&#([A-Fa-f0-9]{6})").matcher(message)
        val buffer = StringBuffer(message.length + 4 * 8)

        while (matcher.find()) {
            val group = matcher.group(1)

            matcher.appendReplacement(
                buffer, (colorChar.toString() + "x"
                        + colorChar + group[0] + colorChar + group[1]
                        + colorChar + group[2] + colorChar + group[3]
                        + colorChar + group[4] + colorChar + group[5])
            )
        }

        return matcher.appendTail(buffer).toString()
    }
}