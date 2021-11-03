package me.weiwen.dispenserrobot

import me.weiwen.dispenserrobot.block.BlockBreak
import me.weiwen.dispenserrobot.block.BlockStrip
import me.weiwen.dispenserrobot.extensions.blockInFront
import me.weiwen.dispenserrobot.extensions.isTool
import me.weiwen.dispenserrobot.extensions.playSoundAt
import org.bukkit.Bukkit
import org.bukkit.SoundCategory
import org.bukkit.Tag
import org.bukkit.block.Dispenser
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.plugin.java.JavaPlugin

class DispenserRobot : JavaPlugin(), Listener {
    companion object {
        lateinit var plugin: DispenserRobot
            private set
    }

    private val blockStrip: BlockStrip by lazy { BlockStrip(this) }
    private val blockBreak: BlockBreak by lazy { BlockBreak(this, blockStrip) }

    var config: Config = parseConfig(this)

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        plugin.server.pluginManager.registerEvents(this, this)
        logger.info("DispenserRobot is enabled")
    }

    override fun onDisable() {
        logger.info("DispenserRobot is disabled")
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockDispense(event: BlockDispenseEvent) {
        val block = event.block
        val dispenser = block.state as? Dispenser ?: return
        val item = event.item

        val blockInFront = block.blockInFront ?: return

        if (config.canPlaceBlocks) {
            if (item.type.isBlock && item.type !in Tag.SHULKER_BOXES.values) {
                event.isCancelled = true
                if (!blockInFront.type.isAir) {
                    event.isCancelled = !config.shouldDropBlocks
                    return
                }

                blockInFront.type = item.type
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, {
                    dispenser.inventory.removeItem(item)
                }, 1L)

                val soundGroup = blockInFront.blockData.soundGroup
                blockInFront.playSoundAt(
                    soundGroup.placeSound,
                    SoundCategory.BLOCKS,
                    soundGroup.volume,
                    soundGroup.pitch * 0.8f
                )
            }
        }

        if (config.canBreakBlocks) {
            if (item.type.isTool) {
                event.isCancelled = true
                if (blockInFront.type.isAir) {
                    event.isCancelled = !config.shouldDropTools
                    return
                }

                blockBreak.startBreaking(
                    item,
                    block,
                    blockInFront,
                )
            }
        }
    }
}
