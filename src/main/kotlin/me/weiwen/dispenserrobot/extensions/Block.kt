package me.weiwen.dispenserrobot.extensions

import org.bukkit.block.Block
import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.util.Vector3i
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockBreakAnimation
import me.weiwen.dispenserrobot.DispenserRobot
import org.bukkit.Bukkit

import org.bukkit.block.data.Directional

val Block.blockInFront: Block?
    get() {
        val data = blockData as? Directional ?: return null
        return getRelative(data.facing)
    }

fun Block.sendBlockDamage(progress: Float, entityId: Int) {
    if (Bukkit.getServer().pluginManager.isPluginEnabled("packetevents")) {
        val destroyStage = if (progress == 0f) {
            -1
        } else {
            (progress * 10).toInt()
        }
        val packet = WrapperPlayServerBlockBreakAnimation(
            entityId,
            Vector3i(location.blockX, location.blockY, location.blockZ),
            destroyStage.toByte()
        )

        val distance = DispenserRobot.plugin.config.renderDistance * DispenserRobot.plugin.config.renderDistance
        Bukkit.getServer().onlinePlayers.forEach { player ->
            if (player.world == world && player.location.distanceSquared(location) < distance) {
                PacketEvents.getAPI().playerManager.sendPacket(player, packet)
            }
        }
    }
}
