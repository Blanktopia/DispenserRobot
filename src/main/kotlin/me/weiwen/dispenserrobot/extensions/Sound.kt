package me.weiwen.dispenserrobot.extensions

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.Block

fun Block.playSoundAt(sound: Sound, category: SoundCategory, volume: Float, pitch: Float) {
    world.playSound(Location(world, x + 0.5, y + 0.5, z + 0.5), sound, category, volume, pitch)
}