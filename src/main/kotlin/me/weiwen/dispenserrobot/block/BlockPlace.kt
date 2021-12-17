package me.weiwen.dispenserrobot.block

import me.weiwen.dispenserrobot.DispenserRobot
import me.weiwen.dispenserrobot.IS_SERVER_PAPER
import me.weiwen.dispenserrobot.extensions.playSoundAt
import me.weiwen.moromoro.Moromoro
import me.weiwen.moromoro.actions.Context
import me.weiwen.moromoro.extensions.customItemKey
import org.bukkit.Bukkit
import org.bukkit.SoundCategory
import org.bukkit.Tag
import org.bukkit.block.Block
import org.bukkit.block.Dispenser
import org.bukkit.block.data.Directional
import org.bukkit.inventory.ItemStack

class BlockPlace(private val plugin: DispenserRobot) {
    fun placeBlock(item: ItemStack, dispenser: Block, block: Block): Boolean {
        val state = dispenser.state as? Dispenser ?: return false
        val facing = (dispenser.blockData as? Directional)?.facing ?: return false

        val moromoroBlock =
            if (plugin.config.hookMoromoro && DispenserRobot.plugin.server.pluginManager.isPluginEnabled("Moromoro")) {
                Moromoro.plugin.itemManager.templates[item.customItemKey]?.block
            } else {
                null
            }

        if (moromoroBlock == null && (!item.type.isBlock || item.type in Tag.SHULKER_BOXES.values)) {
            return false
        }

        if (block.type.isAir || (IS_SERVER_PAPER && !block.isReplaceable)) {
            return !plugin.config.shouldDropBlocks
        }

        if (moromoroBlock != null) {
            if (moromoroBlock.place(
                    Context(
                        null,
                        null,
                        item,
                        null,
                        dispenser,
                        facing
                    )
                )
            ) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                    state.inventory.removeItem(item)
                }, 1L)
                return true
            } else {
                return !plugin.config.shouldDropBlocks
            }
        }

        block.type = item.type
        val soundGroup = block.blockData.soundGroup
        block.playSoundAt(
            soundGroup.placeSound,
            SoundCategory.BLOCKS,
            soundGroup.volume,
            soundGroup.pitch * 0.8f
        )

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            state.inventory.removeItem(item)
        }, 1L)

        return true
    }
}