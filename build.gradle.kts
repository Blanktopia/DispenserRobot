@file:Suppress("SpellCheckingInspection")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("net.minecrell.plugin-yml.bukkit")
    id("com.github.johnrengelman.shadow")
}

group = "me.weiwen.dispenserrobot"
version = "1.2.1"

repositories {
    jcenter()
    mavenCentral()

    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://papermc.io/repo/repository/maven-public") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }

    // bStats
    maven { url = uri("https://repo.codemc.org/repository/maven-public") }

    // ProtocolLib
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }

    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib", "1.6.0"))

    // Deserialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("com.charleskorn.kaml:kaml:0.33.0")

    // Paper
    compileOnly("io.papermc.paper", "paper-api", "1.18.1-R0.1-SNAPSHOT")

    // Spigot
    compileOnly("org.spigotmc", "spigot", "1.18.1-R0.1-SNAPSHOT")

    // bStats
    implementation("org.bstats", "bstats-bukkit", "1.8")

    // ProtocolLib
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.7.0-SNAPSHOT")

    // Moromoro
    compileOnly("me.weiwen.moromoro", "Moromoro", "1.0.0-SNAPSHOT")
}

bukkit {
    main = "me.weiwen.dispenserrobot.DispenserRobot"
    name = "DispenserRobot"
    version = "1.2.1"
    description = "Add more capabilities to dispenser"
    apiVersion = "1.16"
    author = "Goh Wei Wen <goweiwen@gmail.com>"
    website = "weiwen.me"

    softDepend = listOf("ProtocolLib", "Moromoro")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.languageVersion = "1.6"
    kotlinOptions.freeCompilerArgs = listOf(
        "-Xopt-in=kotlin.RequiresOptIn",
        "-Xuse-experimental=org.jetbrains.kotlinx.serialization.ExperimentalSerializationApi"
    )
}

tasks.withType<ShadowJar> {
    classifier = null

    relocate("org.bstats", "me.weiwen.dispenserrobot.bstats")
}
