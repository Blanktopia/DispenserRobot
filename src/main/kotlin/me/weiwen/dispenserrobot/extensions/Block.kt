package me.weiwen.dispenserrobot.extensions

import org.bukkit.block.Block
import com.comphenix.protocol.wrappers.BlockPosition
import me.weiwen.dispenserrobot.DispenserRobot
import me.weiwen.dispenserrobot.packets.WrapperPlayServerBlockBreakAnimation
import org.bukkit.Bukkit

import org.bukkit.block.data.Directional

val Block.blockInFront: Block?
    get() {
        val data = blockData as? Directional ?: return null
        return getRelative(data.facing)
    }

fun Block.sendBlockDamage(progress: Float, entityId: Int) {
    if (Bukkit.getServer().pluginManager.isPluginEnabled("ProtocolLib")) {
        val packet = WrapperPlayServerBlockBreakAnimation()
        packet.entityID = entityId
        packet.location = BlockPosition(location.blockX, location.blockY, location.blockZ)
        packet.destroyStage = if (progress == 0f) {
            -1
        } else {
            (progress * 10).toInt()
        }

        val distance = DispenserRobot.plugin.config.renderDistance * DispenserRobot.plugin.config.renderDistance
        Bukkit.getServer().onlinePlayers.forEach { player ->
            if (player.world == world && player.location.distanceSquared(location) < distance) {
                packet.sendPacket(player)
            }
        }
    }
}
