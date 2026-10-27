@file:Suppress("SpellCheckingInspection")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.4.21"
    kotlin("plugin.serialization") version "2.4.21"
    id("net.minecrell.plugin-yml.bukkit")
    id("com.gradleup.shadow")
}

group = "me.weiwen.dispenserrobot"
version = "1.4.0"

repositories {
    mavenLocal()
    mavenCentral()

    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven("https://repo.purpurmc.org/snapshots")
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }

    // bStats
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }

    // packetevents
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }

    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib", "2.4.21"))

    // Deserialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.12.0")
    implementation("com.charleskorn.kaml:kaml:0.104.0")

    // Purpur
    compileOnly("org.purpurmc.purpur", "purpur-api", "26.3.build.2654-stable")

    // Vault
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7.1")

    // bStats
    implementation("org.bstats", "bstats-bukkit", "1.8")

    // packetevents
    compileOnly("com.github.retrooper", "packetevents-spigot", "2.14.0")

    // Moromoro
    compileOnly("me.weiwen.moromoro", "Moromoro", "1.2.0-SNAPSHOT")
}

bukkit {
    main = "me.weiwen.dispenserrobot.DispenserRobot"
    name = "DispenserRobot"
    version = "1.4.0"
    description = "Add more capabilities to dispenser"
    apiVersion = "26.3"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "weiwen.me"

    softDepend = listOf("packetevents", "Moromoro")
}

tasks.withType<KotlinCompile> {
    compilerOptions.freeCompilerArgs.set(listOf(
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xuse-experimental=org.jetbrains.kotlinx.serialization.ExperimentalSerializationApi"
    ))
}

tasks.withType<ShadowJar> {
    relocate("org.bstats", "me.weiwen.dispenserrobot.bstats")
}
