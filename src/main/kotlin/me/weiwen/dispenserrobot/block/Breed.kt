package me.weiwen.dispenserrobot.block

import me.weiwen.dispenserrobot.DispenserRobot
import me.weiwen.dispenserrobot.extensions.isBreedItem
import me.weiwen.dispenserrobot.extensions.spawnParticle
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.block.Dispenser
import org.bukkit.block.data.Directional
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.inventory.ItemStack

class Breed(private val plugin: DispenserRobot) {
    fun breed(event: BlockDispenseEvent, item: ItemStack, dispenser: Block): Boolean {
        val state = dispenser.state as? Dispenser ?: return false
        val facing = (dispenser.blockData as? Directional)?.facing ?: return false

        val seen = mutableSetOf<Entity>()
        while (true) {
            val result = dispenser.world.rayTraceEntities(
                dispenser.location,
                facing.direction,
                plugin.config.breedRange,
                plugin.config.breedRange
            ) {
                !seen.contains(it)
            }
            val entity = result?.hitEntity as? Animals
            if (entity == null) {
                if (!plugin.config.shouldDropBreedItems) {
                    event.isCancelled = event.isCancelled || item.type.isBreedItem
                }
                return false
            } else if (breed(item, entity)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
                    state.inventory.removeItem(item)
                }, 1L)
                event.isCancelled = true
                return true
            }
            seen.add(entity)
        }

    }

    private fun breed(item: ItemStack, entity: Animals): Boolean {
        if (!entity.canBreed()) {
            return false
        }

        if (entity.isLoveMode) {
            return false
        }

        if (!entity.isBreedItem(item)) {
            return false
        }

        entity.loveModeTicks = 600
        entity.spawnParticle(Particle.VILLAGER_HAPPY, 10, 0.0)

        return true
    }
}