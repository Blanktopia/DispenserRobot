package me.weiwen.dispenserrobot.block

import me.weiwen.dispenserrobot.DispenserRobot
import me.weiwen.dispenserrobot.extensions.*
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Container
import org.bukkit.block.data.Bisected
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Openable
import org.bukkit.block.data.Orientable
import org.bukkit.block.data.Waterlogged
import org.bukkit.block.data.type.Door
import org.bukkit.block.data.type.Slab
import org.bukkit.block.data.type.Stairs
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.inventory.ItemStack

class BlockStrip(private val plugin: DispenserRobot) {
    private fun stripLog(block: Block): Boolean {
        val stripped = block.type.stripped ?: return false
        val data = block.blockData
        block.type = stripped
        val newData = block.blockData
        if (data is Orientable && newData is Orientable) {
            newData.axis = data.axis
            block.blockData = newData
        }
        block.playSoundAt(Sound.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f)
        return true
    }

    private fun unstripLog(block: Block): Boolean {
        val unstripped = block.type.unstripped ?: return false
        val data = block.blockData
        block.type = unstripped
        val newData = block.blockData
        if (data is Orientable && newData is Orientable) {
            newData.axis = data.axis
            block.blockData = newData
        }
        block.playSoundAt(Sound.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f)
        return true
    }

    private fun unwaxCopper(block: Block): Boolean {
        val unwaxed = block.type.unwaxed ?: return false
        val data = block.blockData
        block.type = unwaxed
        val newData = block.blockData
        if (data is Slab && newData is Slab) {
            newData.type = data.type
            block.blockData = newData
        }
        if (data is Stairs && newData is Stairs) {
            newData.shape = data.shape
            block.blockData = newData
        }
        if (data is Waterlogged && newData is Waterlogged) {
            newData.isWaterlogged = data.isWaterlogged
            block.blockData = newData
        }
        if (data is Bisected && newData is Bisected) {
            newData.half = data.half
            block.blockData = newData
        }
        if (data is Directional && newData is Directional) {
            newData.facing = data.facing
            block.blockData = newData
        }
        if (data is Openable && newData is Openable) {
            newData.isOpen = data.isOpen
            block.blockData = newData
        }
        if (data is Door && newData is Door) {
            newData.hinge = data.hinge
            block.blockData = newData
        }
        block.playSoundAt(Sound.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f)
        block.spawnParticle(Particle.WAX_OFF, 10, 0.0)
        return true
    }

    private fun scrapeCopper(block: Block): Boolean {
        val scrapped = block.type.scraped ?: return false
        val data = block.blockData
        block.type = scrapped
        val newData = block.blockData
        if (data is Slab && newData is Slab) {
            newData.type = data.type
            block.blockData = newData
        }
        if (data is Stairs && newData is Stairs) {
            newData.shape = data.shape
            block.blockData = newData
        }
        if (data is Waterlogged && newData is Waterlogged) {
            newData.isWaterlogged = data.isWaterlogged
            block.blockData = newData
        }
        if (data is Bisected && newData is Bisected) {
            newData.half = data.half
            block.blockData = newData
        }
        if (data is Directional && newData is Directional) {
            newData.facing = data.facing
            block.blockData = newData
        }
        if (data is Openable && newData is Openable) {
            newData.isOpen = data.isOpen
            block.blockData = newData
        }
        if (data is Door && newData is Door) {
            newData.hinge = data.hinge
            block.blockData = newData
        }
        block.playSoundAt(Sound.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0f, 1.0f)
        block.spawnParticle(Particle.SCRAPE, 10, 0.0)
        return true
    }

    private fun tillDirt(block: Block): Boolean {
        if (block.type != Material.DIRT &&
            block.type != Material.GRASS_BLOCK &&
            block.type != Material.DIRT_PATH
        ) {
            return false
        }
        if (!block.getRelative(BlockFace.UP).type.isAir) {
            return false
        }
        block.type = Material.FARMLAND
        block.playSoundAt(Sound.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
        return true
    }

    private fun pathDirt(block: Block): Boolean {
        if (block.type != Material.DIRT &&
            block.type != Material.GRASS_BLOCK &&
            block.type != Material.COARSE_DIRT &&
            block.type != Material.PODZOL &&
            block.type != Material.ROOTED_DIRT
        ) {
            return false
        }
        if (!block.getRelative(BlockFace.UP).type.isAir) {
            return false
        }
        block.type = Material.DIRT_PATH
        block.playSoundAt(Sound.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0f, 1.0f)
        return true
    }

    fun strip(block: Block, tool: ItemStack, dispenser: Block) {
        if (plugin.config.canStripLogs && tool.type in axes && stripLog(block)) {
            (dispenser.state as? Container)?.let { tool.damage(1, it) }
            return
        }

        if (plugin.config.canUnstripLogs && tool.type in axes && unstripLog(block)) {
            (dispenser.state as? Container)?.let { tool.damage(1, it) }
            return
        }

        if (plugin.config.canUnwaxCopper && tool.type in axes && unwaxCopper(block)) {
            (dispenser.state as? Container)?.let { tool.damage(1, it) }
            return
        }

        if (plugin.config.canScrapeCopper && tool.type in axes && scrapeCopper(block)) {
            (dispenser.state as? Container)?.let { tool.damage(1, it) }
            return
        }

        if (plugin.config.canTillDirt && tool.type in hoes && tillDirt(block)) {
            (dispenser.state as? Container)?.let { tool.damage(1, it) }
            return
        }

        if (plugin.config.canPathDirt && tool.type in shovels && pathDirt(block)) {
            (dispenser.state as? Container)?.let { tool.damage(1, it) }
            return
        }
    }
}