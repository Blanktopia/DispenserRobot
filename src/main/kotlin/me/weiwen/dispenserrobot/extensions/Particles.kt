package me.weiwen.dispenserrobot.extensions

import org.bukkit.Particle
import org.bukkit.block.Block

fun Block.spawnParticle(particle: Particle, count: Int, speed: Double) {
    world.spawnParticle(particle, x + 0.5, y + 0.5, z + 0.6, count, 0.4, 0.4, 0.4, speed)
}