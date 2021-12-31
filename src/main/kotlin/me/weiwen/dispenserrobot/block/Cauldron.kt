package me.weiwen.dispenserrobot.block

import me.weiwen.dispenserrobot.DispenserRobot
import me.weiwen.dispenserrobot.extensions.blockInFront
import me.weiwen.dispenserrobot.extensions.playSoundAt
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.block.Dispenser
import org.bukkit.block.data.Levelled
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.inventory.ItemStack

class Cauldron(private val plugin: DispenserRobot) {
    fun collect(event: BlockDispenseEvent, item: ItemStack, dispenser: Block): Boolean {
        val state = dispenser.state as? Dispenser ?: return false
        val block = dispenser.blockInFront ?: return false
        val levelled = block.blockData as? Levelled

        if (levelled != null && levelled.level != levelled.maximumLevel) {
            event.isCancelled = !plugin.config.shouldDropBuckets
            return false
        }

        val bucketMaterial = when (block.type) {
            Material.WATER_CAULDRON -> {
                if (plugin.config.canCollectWaterFromCauldron) {
                    block.playSoundAt(Sound.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    Material.WATER_BUCKET
                } else {
                    event.isCancelled = !plugin.config.shouldDropBuckets
                    return false
                }
            }
            Material.LAVA_CAULDRON -> {
                if (plugin.config.canCollectLavaFromCauldron) {
                    block.playSoundAt(Sound.ITEM_BUCKET_FILL_LAVA, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    Material.LAVA_BUCKET
                } else {
                    event.isCancelled = !plugin.config.shouldDropBuckets
                    return false
                }
            }
            Material.POWDER_SNOW_BUCKET -> {
                if (plugin.config.canCollectPowderSnowFromCauldron) {
                    block.playSoundAt(Sound.ITEM_BUCKET_FILL_POWDER_SNOW, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    Material.POWDER_SNOW_BUCKET
                } else {
                    event.isCancelled = !plugin.config.shouldDropBuckets
                    return false
                }
            }
            else -> {
                event.isCancelled = !plugin.config.shouldDropBuckets
                return false
            }
        }

        event.isCancelled = true
        block.type = Material.CAULDRON

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            state.inventory.removeItem(item)
            state.inventory.addItem(ItemStack(bucketMaterial))
        }, 1L)

        return true
    }

    private fun place(
        event: BlockDispenseEvent,
        item: ItemStack,
        dispenser: Block,
        cauldron: Material,
        placeSound: Sound
    ): Boolean {
        val state = dispenser.state as? Dispenser ?: return false
        val block = dispenser.blockInFront

        if (block == null || block.type != Material.CAULDRON) {
            event.isCancelled = !plugin.config.shouldDropBuckets
            return false
        }

        event.isCancelled = true
        block.type = cauldron
        block.blockData = block.blockData.apply {
            if (this is Levelled) {
                level = maximumLevel
            }
        }

        block.playSoundAt(placeSound, SoundCategory.BLOCKS, 1.0f, 1.0f)

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            state.inventory.removeItem(item)
            state.inventory.addItem(ItemStack(Material.BUCKET))
        }, 1L)

        return true
    }

    fun placeWater(event: BlockDispenseEvent, item: ItemStack, dispenser: Block): Boolean {
        return place(event, item, dispenser, Material.WATER_CAULDRON, Sound.ITEM_BUCKET_EMPTY)
    }

    fun placeLava(event: BlockDispenseEvent, item: ItemStack, dispenser: Block): Boolean {
        return place(event, item, dispenser, Material.LAVA_CAULDRON, Sound.ITEM_BUCKET_EMPTY_LAVA)
    }

    fun placePowderSnow(event: BlockDispenseEvent, item: ItemStack, dispenser: Block): Boolean {
        return place(event, item, dispenser, Material.POWDER_SNOW_CAULDRON, Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW)
    }
}