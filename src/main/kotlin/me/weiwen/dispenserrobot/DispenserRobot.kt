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
    private val cauldron: Cauldron by lazy { Cauldron(this) }

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

        if (block.type != Material.DISPENSER) return

        val blockInFront = block.blockInFront ?: return

        if (when (item.type) {
            Material.BUCKET -> cauldron.collect(event, item, block)
            Material.LAVA_BUCKET -> cauldron.placeLava(event, item, block)
            Material.WATER_BUCKET -> cauldron.placeWater(event, item, block)
            Material.POWDER_SNOW_BUCKET -> cauldron.placePowderSnow(event, item, block)
            else -> false
        }) {
            return
        }

        if (config.canBreed && breed.breed(event, item, block)) {
            return
        }

        if (config.canPlaceBlocks && item.type !in config.placeBlocksBlacklist) {
            if (blockPlace.placeBlock(item, block, blockInFront)) {
                event.isCancelled = true
                return
            }
        }

        if (config.canBreakBlocks) {
            if (item.type.isTool) {
                if (item.type == Material.SHEARS && blockInFront.type == Material.BEEHIVE || blockInFront.type == Material.BEE_NEST) {
                    event.isCancelled = false
                    return
                }

                if (blockInFront.type.isAir || blockInFront.type in config.breakBlocksBlacklist) {
                    event.isCancelled = !config.shouldDropTools && item.type != Material.SHEARS
                    return
                }

                event.isCancelled = true
                blockBreak.startBreaking(item, block, blockInFront)
            }
        }
    }
}
