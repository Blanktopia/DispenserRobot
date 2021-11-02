package me.weiwen.dispenserrobot.block

import me.weiwen.dispenserrobot.DispenserRobot
import me.weiwen.dispenserrobot.IS_SERVER_PAPER
import me.weiwen.dispenserrobot.extensions.*
import me.weiwen.moromoro.managers.CustomBlock
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.Container
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.random.Random

class BlockBreak(private val plugin: DispenserRobot, val blockStrip: BlockStrip) {

    fun startBreaking(tool: ItemStack, dispenser: Block, block: Block) {
        val ticks = breakDuration(tool, block)

        if (ticks == 0L) {
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                finishBreaking(tool, dispenser, block, null)
            }, 1)
        } else {
            tickBreaking(tool, dispenser, block, block.type, 0, ticks, Random.nextInt())
        }
    }

    private fun tickBreaking(
        tool: ItemStack,
        dispenser: Block,
        block: Block,
        material: Material,
        progress: Long,
        ticks: Long,
        entityId: Int
    ) {
        if (block.type != material) {
            block.sendBlockDamage(0f, entityId)
            return
        }

        if (dispenser.type != Material.DISPENSER) {
            block.sendBlockDamage(0f, entityId)
            return
        }

        if (!dispenser.isBlockPowered && !dispenser.isBlockIndirectlyPowered) {
            block.sendBlockDamage(0f, entityId)

            if (progress < 2) {
                blockStrip.strip(block, tool, dispenser)
            }
            return
        }

        block.sendBlockDamage((progress.toFloat() / ticks).coerceAtMost(1f), entityId)

        block.world.spawnParticle(
            Particle.BLOCK_CRACK,
            block.x + 0.5,
            block.y + 0.5,
            block.z + 0.6,
            10,
            0.1,
            0.1,
            0.1,
            block.blockData
        )

        if (progress.mod(4) == 0) {
            val soundGroup = block.blockData.soundGroup
            block.playSoundAt(
                soundGroup.hitSound,
                SoundCategory.BLOCKS,
                soundGroup.volume * 0.25f,
                soundGroup.pitch * 0.5f
            )
        }

        if (progress < ticks) {
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                tickBreaking(
                    tool,
                    dispenser,
                    block,
                    material,
                    progress + 1,
                    ticks,
                    entityId
                )
            }, 1L)
        } else {
            finishBreaking(tool, dispenser, block, entityId)
        }
    }

    private fun finishBreaking(tool: ItemStack, dispenser: Block, block: Block, entityId: Int?) {
        entityId?.let { block.sendBlockDamage(0f, it) }

        (dispenser.state as? Container)?.let {
            tool.damage(
                if (tool.type.isBestTool(block.type)) {
                    1
                } else {
                    2
                }, it
            )
        }

        if (plugin.config.hookMoromoro && plugin.server.pluginManager.isPluginEnabled("Moromoro")) {
            val customBlock = CustomBlock.fromBlock(block)
            if (customBlock != null) {
                customBlock.breakNaturally(tool, true)
            } else {
                if (IS_SERVER_PAPER) {
                    block.breakNaturally(tool, true)
                } else {
                    block.breakNaturally(tool)
                }
            }
        } else {
            if (IS_SERVER_PAPER) {
                block.breakNaturally(tool, true)
            } else {
                block.breakNaturally(tool)
            }
        }
    }

    private fun breakDuration(tool: ItemStack, block: Block): Long {
        val speed = when (tool.type) {
            in goldTools -> 12.0
            in netheriteTools -> 9.0
            in diamondTools -> 8.0
            in ironTools -> 6.0
            in stoneTools -> 4.0
            in woodenTools -> 2.0
            Material.SHEARS -> when (block.type) {
                in Tag.WOOL.values -> 5.0
                Material.COBWEB, in Tag.LEAVES.values -> 15.0
                else -> 1.5
            }
            else -> 1.0
        }

        val efficiency = when (val level = tool.getEnchantmentLevel(Enchantment.DIG_SPEED)) {
            0 -> 0
            else -> level * level + 1
        }

        val multiplier = if (tool.type.isBestTool(block.type)) {
            speed + efficiency
        } else {
            1.0
        }

        val hardness = block.type.hardness
        val damage = if (tool.type.canHarvest(block.type)) {
            multiplier / hardness / 30.0
        } else {
            1.0 / hardness / 100.0
        }

        if (damage > 1.0) {
            return 0
        }

        return ceil(1.0 / damage).toLong()
    }
}