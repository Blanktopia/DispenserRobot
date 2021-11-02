package me.weiwen.dispenserrobot.extensions

import org.bukkit.block.Container
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.random.Random

fun ItemStack.damage(damage: Int) {
    val itemMeta = itemMeta ?: return
    if (itemMeta.isUnbreakable) {
        return
    }

    val unbreaking = itemMeta.getEnchantLevel(Enchantment.DURABILITY)

    if (Random.nextDouble(1.0) * (unbreaking + 1) < 1) {
        (itemMeta as? Damageable)?.let {
            it.damage += damage
            this.itemMeta = itemMeta
        }
    }
}

fun ItemStack.damage(damage: Int, container: Container): Boolean {
    val slot = container.inventory.first(this)
    if (slot == -1) {
        return false
    }

    this.damage(damage)
    container.inventory.setItem(slot, this)
    return true
}

