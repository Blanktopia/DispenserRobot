package me.weiwen.dispenserrobot.extensions

import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.random.Random

fun ItemStack.damage(damage: Int): Boolean {
    val itemMeta = itemMeta ?: return true
    if (itemMeta.isUnbreakable) {
        return true
    }

    val unbreaking = itemMeta.getEnchantLevel(Enchantment.DURABILITY)

    if (Random.nextDouble(1.0) * (unbreaking + 1) < 1) {
        (itemMeta as? Damageable)?.let {
            it.damage += damage
            if (it.damage >= type.maxDurability) {
                return false
            }
            this.itemMeta = itemMeta
        }
    }

    return true
}

fun ItemStack.damage(damage: Int, container: Container): Boolean {
    val slot = container.inventory.first(this)
    if (slot == -1) {
        return false
    }

    if (this.damage(damage)) {
        container.inventory.setItem(slot, this)
    } else {
        container.inventory.setItem(slot, ItemStack(Material.AIR))
    }
    return true
}

