includeBuild("../Moromoro")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            val id = requested.id.id

            if (id.startsWith("org.jetbrains.kotlin"))
                useVersion("2.1.10")

            if(id.startsWith("net.minecrell.plugin-yml.bukkit"))
                useVersion("0.5.1")

            if(id.startsWith("io.github.goooler.shadow"))
                useVersion("8.1.7")
        }
    }
}
