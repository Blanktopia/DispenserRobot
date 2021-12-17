package me.weiwen.dispenserrobot

import me.weiwen.dispenserrobot.block.*
import me.weiwen.dispenserrobot.extensions.blockInFront
import me.weiwen.dispenserrobot.extensions.isTool
import org.bstats.bukkit.Metrics
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class DispenserRobot : JavaPlugin(), Listener {
    companion object {
        lateinit var plugin: DispenserRobot
            private set
    }

    private val blockStrip: BlockStrip by lazy { BlockStrip(this) }
    private val blockBreak: BlockBreak by lazy { BlockBreak(this, blockStrip) }
    private val blockPlace: BlockPlace by lazy { BlockPlace(this) }
    private val breed: Breed by lazy { Breed(this) }

    var config: Config = parseConfig(this)

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        plugin.server.pluginManager.registerEvents(this, this)

        Metrics(this, 13621);

        logger.info("DispenserRobot is enabled")
    }

    override fun onDisable() {
        logger.info("DispenserRobot is disabled")
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockDispense(event: BlockDispenseEvent) {
        val block = event.block
        val item = event.item

        val blockInFront = block.blockInFront ?: return

        if (config.canBreed && breed.breed(event, item, block)) {
            return
        }

        if (config.canPlaceBlocks) {
            if (blockPlace.placeBlock(item, block, blockInFront)) {
                event.isCancelled = true
                return
            }
        }

        if (config.canBreakBlocks) {
            if (item.type.isTool) {
                if (blockInFront.type.isAir) {
                    event.isCancelled = !config.shouldDropTools && item.type != Material.SHEARS
                    return
                }

                event.isCancelled = true
                blockBreak.startBreaking(item, block, blockInFront)
            }
        }
    }
}
