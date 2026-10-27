includeBuild("../Moromoro")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            val id = requested.id.id

            if (id.startsWith("org.jetbrains.kotlin"))
                useVersion("2.4.21")

            if(id.startsWith("net.minecrell.plugin-yml.bukkit"))
                useVersion("0.5.1")

            if(id.startsWith("com.gradleup.shadow"))
                useVersion("9.6.1")
        }
    }
}
