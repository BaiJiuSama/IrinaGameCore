package cn.irina.core.data

/*
 * @Author Irina
 * @Date 2025/7/3 02:44
 */

data class TeleportPoint(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
)
