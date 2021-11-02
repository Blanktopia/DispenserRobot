package me.weiwen.dispenserrobot

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Level

val IS_SERVER_PAPER: Boolean by lazy {
    try {
        Class.forName("com.destroystokyo.paper.Title")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

@Serializable
data class Config(
    @SerialName("can-place-blocks")
    val canPlaceBlocks: Boolean = true,
    @SerialName("should-drop-blocks")
    val shouldDropBlocks: Boolean = false,

    @SerialName("can-break-blocks")
    val canBreakBlocks: Boolean = true,
    @SerialName("should-drop-tools")
    val shouldDropTools: Boolean = false,

    @SerialName("can-strip-logs")
    val canStripLogs: Boolean = true,
    @SerialName("can-unstrip-logs")
    val canUnstripLogs: Boolean = false,
    @SerialName("can-unwax-copper")
    val canUnwaxCopper: Boolean = true,
    @SerialName("can-scrape-copper")
    val canScrapeCopper: Boolean = true,
    @SerialName("can-till-dirt")
    val canTillDirt: Boolean = true,
    @SerialName("can-path-dirt")
    val canPathDirt: Boolean = true,

    @SerialName("render-distance")
    val renderDistance: Double = 32.0,

    @SerialName("hook-moromoro")
    val hookMoromoro: Boolean = true,
)

fun parseConfig(plugin: JavaPlugin): Config {
    val file = File(plugin.dataFolder, "config.yml")

    if (!file.exists()) {
        plugin.logger.log(Level.INFO, "Config file not found, creating default")
        plugin.dataFolder.mkdirs()
        file.createNewFile()
        file.writeText(Yaml().encodeToString(Config()))
    }

    return try {
        Yaml().decodeFromString(file.readText())
    } catch (e: Exception) {
        plugin.logger.log(Level.SEVERE, e.message)
        Config()
    }
}