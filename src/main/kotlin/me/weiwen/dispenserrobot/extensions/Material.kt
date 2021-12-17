package me.weiwen.dispenserrobot.extensions

import org.bukkit.Material
import org.bukkit.Tag

val pickaxeMaterials: Set<Material> by lazy {
    Tag.MINEABLE_PICKAXE.values
}

val axeMaterials: Set<Material> by lazy {
    Tag.MINEABLE_AXE.values
}

val shovelMaterials: Set<Material> by lazy {
    Tag.MINEABLE_SHOVEL.values
}

val hoeMaterials: Set<Material> by lazy {
    Tag.MINEABLE_HOE.values
}

val shearMaterials: Set<Material> by lazy {
    Tag.LEAVES.values.plus(
        Tag.WOOL.values
    ).plus(
        listOf(
            Material.COBWEB,
            Material.CAVE_VINES,
            Material.TWISTING_VINES,
            Material.WEEPING_VINES,
            Material.GLOW_LICHEN,
        )
    )
}

val netheriteTools: Set<Material> = setOf(
    Material.NETHERITE_PICKAXE,
    Material.NETHERITE_AXE,
    Material.NETHERITE_SHOVEL,
    Material.NETHERITE_HOE,
)

val goldTools: Set<Material> = setOf(
    Material.GOLDEN_PICKAXE,
    Material.GOLDEN_AXE,
    Material.GOLDEN_SHOVEL,
    Material.GOLDEN_HOE,
)

val diamondTools: Set<Material> = setOf(
    Material.DIAMOND_PICKAXE,
    Material.DIAMOND_AXE,
    Material.DIAMOND_SHOVEL,
    Material.DIAMOND_HOE,
)

val ironTools: Set<Material> = setOf(
    Material.IRON_PICKAXE,
    Material.IRON_AXE,
    Material.IRON_SHOVEL,
    Material.IRON_HOE,
)

val stoneTools: Set<Material> = setOf(
    Material.STONE_PICKAXE,
    Material.STONE_AXE,
    Material.STONE_SHOVEL,
    Material.STONE_HOE,
)

val woodenTools: Set<Material> = setOf(
    Material.WOODEN_PICKAXE,
    Material.WOODEN_AXE,
    Material.WOODEN_SHOVEL,
    Material.WOODEN_HOE,
)

val pickaxes: Set<Material> = setOf(
    Material.DIAMOND_PICKAXE,
    Material.GOLDEN_PICKAXE,
    Material.IRON_PICKAXE,
    Material.STONE_PICKAXE,
    Material.WOODEN_PICKAXE,
    Material.NETHERITE_PICKAXE
)

val axes: Set<Material> = setOf(
    Material.DIAMOND_AXE,
    Material.GOLDEN_AXE,
    Material.IRON_AXE,
    Material.STONE_AXE,
    Material.WOODEN_AXE,
    Material.NETHERITE_AXE
)

val shovels: Set<Material> = setOf(
    Material.DIAMOND_SHOVEL,
    Material.GOLDEN_SHOVEL,
    Material.IRON_SHOVEL,
    Material.STONE_SHOVEL,
    Material.WOODEN_SHOVEL,
    Material.NETHERITE_SHOVEL
)

val hoes: Set<Material> = setOf(
    Material.DIAMOND_HOE,
    Material.GOLDEN_HOE,
    Material.IRON_HOE,
    Material.STONE_HOE,
    Material.WOODEN_HOE,
    Material.NETHERITE_HOE
)

val stoneToolMaterials: Set<Material> by lazy {
    Tag.NEEDS_STONE_TOOL.values
}

val ironToolMaterials: Set<Material> by lazy {
    Tag.NEEDS_IRON_TOOL.values
}

val diamondToolMaterials: Set<Material> by lazy {
    Tag.NEEDS_DIAMOND_TOOL.values
}

fun Material.isBestTool(material: Material): Boolean {
    if (material.hardness == 0.0f) {
        return true
    }
    return material in when (this) {
        in pickaxes -> pickaxeMaterials
        in axes -> axeMaterials
        in shovels -> shovelMaterials
        in hoes -> hoeMaterials
        Material.SHEARS -> shearMaterials
        else -> return false
    }
}

fun Material.canHarvest(material: Material): Boolean {
    return this in when (material) {
        in diamondToolMaterials -> diamondTools + netheriteTools
        in ironToolMaterials -> ironTools + diamondTools + netheriteTools
        in stoneToolMaterials -> stoneTools + goldTools + ironTools + diamondTools + netheriteTools
        else -> return true
    }
}

val Material.isTool: Boolean
    get() {
        return this in pickaxes
                || this in axes
                || this in shovels
                || this in hoes
                || this == Material.SHEARS
    }

val Material.stripped: Material?
    get() = when (this) {
        Material.OAK_LOG -> Material.STRIPPED_OAK_LOG
        Material.OAK_WOOD -> Material.STRIPPED_OAK_WOOD
        Material.SPRUCE_LOG -> Material.STRIPPED_SPRUCE_LOG
        Material.SPRUCE_WOOD -> Material.STRIPPED_SPRUCE_WOOD
        Material.BIRCH_LOG -> Material.STRIPPED_BIRCH_LOG
        Material.BIRCH_WOOD -> Material.STRIPPED_BIRCH_WOOD
        Material.JUNGLE_LOG -> Material.STRIPPED_JUNGLE_LOG
        Material.JUNGLE_WOOD -> Material.STRIPPED_JUNGLE_WOOD
        Material.ACACIA_LOG -> Material.STRIPPED_ACACIA_LOG
        Material.ACACIA_WOOD -> Material.STRIPPED_ACACIA_WOOD
        Material.DARK_OAK_LOG -> Material.STRIPPED_DARK_OAK_LOG
        Material.DARK_OAK_WOOD -> Material.STRIPPED_DARK_OAK_WOOD
        Material.CRIMSON_STEM -> Material.STRIPPED_CRIMSON_STEM
        Material.CRIMSON_HYPHAE -> Material.STRIPPED_CRIMSON_HYPHAE
        Material.WARPED_STEM -> Material.STRIPPED_WARPED_STEM
        Material.WARPED_HYPHAE -> Material.STRIPPED_WARPED_HYPHAE
        else -> null
    }

val Material.unstripped: Material?
    get() = when (this) {
        Material.STRIPPED_OAK_LOG -> Material.OAK_LOG
        Material.STRIPPED_OAK_WOOD -> Material.OAK_WOOD
        Material.STRIPPED_SPRUCE_LOG -> Material.SPRUCE_LOG
        Material.STRIPPED_SPRUCE_WOOD -> Material.SPRUCE_WOOD
        Material.STRIPPED_BIRCH_LOG -> Material.BIRCH_LOG
        Material.STRIPPED_BIRCH_WOOD -> Material.BIRCH_WOOD
        Material.STRIPPED_JUNGLE_LOG -> Material.JUNGLE_LOG
        Material.STRIPPED_JUNGLE_WOOD -> Material.JUNGLE_WOOD
        Material.STRIPPED_ACACIA_LOG -> Material.ACACIA_LOG
        Material.STRIPPED_ACACIA_WOOD -> Material.ACACIA_WOOD
        Material.STRIPPED_DARK_OAK_LOG -> Material.DARK_OAK_LOG
        Material.STRIPPED_DARK_OAK_WOOD -> Material.DARK_OAK_WOOD
        Material.STRIPPED_CRIMSON_STEM -> Material.CRIMSON_STEM
        Material.STRIPPED_CRIMSON_HYPHAE -> Material.CRIMSON_HYPHAE
        Material.STRIPPED_WARPED_STEM -> Material.WARPED_STEM
        Material.STRIPPED_WARPED_HYPHAE -> Material.WARPED_HYPHAE
        else -> null
    }

val Material.unwaxed: Material?
    get() = when (this) {
        Material.WAXED_COPPER_BLOCK -> Material.COPPER_BLOCK
        Material.WAXED_CUT_COPPER -> Material.CUT_COPPER
        Material.WAXED_CUT_COPPER_SLAB -> Material.CUT_COPPER_SLAB
        Material.WAXED_CUT_COPPER_STAIRS -> Material.CUT_COPPER_STAIRS
        Material.WAXED_EXPOSED_COPPER -> Material.EXPOSED_COPPER
        Material.WAXED_EXPOSED_CUT_COPPER -> Material.EXPOSED_CUT_COPPER
        Material.WAXED_EXPOSED_CUT_COPPER_SLAB -> Material.EXPOSED_CUT_COPPER_SLAB
        Material.WAXED_EXPOSED_CUT_COPPER_STAIRS -> Material.EXPOSED_CUT_COPPER_STAIRS
        Material.WAXED_WEATHERED_COPPER -> Material.WEATHERED_COPPER
        Material.WAXED_WEATHERED_CUT_COPPER -> Material.WEATHERED_CUT_COPPER
        Material.WAXED_WEATHERED_CUT_COPPER_SLAB -> Material.WEATHERED_CUT_COPPER_SLAB
        Material.WAXED_WEATHERED_CUT_COPPER_STAIRS -> Material.WEATHERED_CUT_COPPER_STAIRS
        Material.WAXED_OXIDIZED_COPPER -> Material.OXIDIZED_COPPER
        Material.WAXED_OXIDIZED_CUT_COPPER -> Material.OXIDIZED_CUT_COPPER
        Material.WAXED_OXIDIZED_CUT_COPPER_SLAB -> Material.OXIDIZED_CUT_COPPER_SLAB
        Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS -> Material.OXIDIZED_CUT_COPPER_STAIRS
        else -> null
    }

val Material.scraped: Material?
    get() = when (this) {
        Material.OXIDIZED_COPPER -> Material.WEATHERED_COPPER
        Material.OXIDIZED_CUT_COPPER -> Material.WEATHERED_CUT_COPPER
        Material.OXIDIZED_CUT_COPPER_SLAB -> Material.WEATHERED_CUT_COPPER_SLAB
        Material.OXIDIZED_CUT_COPPER_STAIRS -> Material.WEATHERED_CUT_COPPER_STAIRS
        Material.WEATHERED_COPPER -> Material.EXPOSED_COPPER
        Material.WEATHERED_CUT_COPPER -> Material.EXPOSED_CUT_COPPER
        Material.WEATHERED_CUT_COPPER_SLAB -> Material.EXPOSED_CUT_COPPER_SLAB
        Material.WEATHERED_CUT_COPPER_STAIRS -> Material.EXPOSED_CUT_COPPER_STAIRS
        Material.EXPOSED_COPPER -> Material.COPPER_BLOCK
        Material.EXPOSED_CUT_COPPER -> Material.CUT_COPPER
        Material.EXPOSED_CUT_COPPER_SLAB -> Material.CUT_COPPER_SLAB
        Material.EXPOSED_CUT_COPPER_STAIRS -> Material.CUT_COPPER_STAIRS
        else -> null
    }

val breedItems: Set<Material> =
    setOf(
        Material.WHEAT,
        Material.CARROT,
        Material.GOLDEN_APPLE,
        Material.ENCHANTED_GOLDEN_APPLE,
        Material.GOLDEN_CARROT,
        Material.POTATO,
        Material.BEETROOT,
        Material.WHEAT_SEEDS,
        Material.PUMPKIN_SEEDS,
        Material.MELON_SEEDS,
        Material.BEETROOT_SEEDS,
        Material.BEEF,
        Material.CHICKEN,
        Material.PORKCHOP,
        Material.COOKED_BEEF,
        Material.COOKED_CHICKEN,
        Material.COOKED_PORKCHOP,
        Material.MUTTON,
        Material.COOKED_MUTTON,
        Material.RABBIT,
        Material.COOKED_RABBIT,
        Material.ROTTEN_FLESH,
        Material.COD,
        Material.SALMON,
        Material.TROPICAL_FISH_BUCKET,
        Material.HAY_BLOCK,
        Material.SEAGRASS,
        Material.BAMBOO,
        Material.SWEET_BERRIES,
        Material.GLOW_BERRIES,
    )

val Material.isBreedItem: Boolean
    get() = breedItems.contains(this)