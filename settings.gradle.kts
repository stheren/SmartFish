rootProject.name = "SmartFish"
include("java")

pluginManagement {
    val kotlinVersion: String by settings
    val shadowVersion: String by settings
    val javafxVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.github.johnrengelman.shadow") version shadowVersion
        id("org.openjfx.javafxplugin") version javafxVersion
    }
}
