pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
    id("dev.kikugie.loom-back-compat") version "0.4.3"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) {
            for (loader in loaders) version("$version-$loader", version).buildscript("build.$loader.gradle.kts")
        }

        match("26.2", "fabric", "neoforge")
        match("26.1.2", "fabric", "neoforge")
        match("1.21.1", "fabric", "neoforge")
        match("1.20.1", "fabric", "forge")
        match("1.20", "fabric", "forge")
        vcsVersion = "26.2-neoforge"
    }
}

rootProject.name = "PrecisionMining"
