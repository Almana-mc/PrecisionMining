plugins {
    id("net.neoforged.moddev")
    id("neoforge-mutex")
}

val modId = property("mod.id") as String
val javaVersion = property("mod.java") as String

version = "${sc.current.version}-${property("mod.version")}"
base.archivesName = "${property("mod.name")}-neoforge"

neoForge {
    version = property("deps.neoforge") as String

    runs {
        register("client") {
            client()
        }
    }

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)

sourceSets.main {
    java.exclude("**/fabric/**", "**/forge/**")
}

tasks.processResources {
    val props = listOf("id", "name", "version", "authors", "description", "license")
        .associateWith { project.property("mod.$it") as String } +
        mapOf("minecraft" to project.property("mod.mc_compat") as String, "java" to "JAVA_$javaVersion")
    inputs.properties(props)
    filesMatching(listOf("META-INF/neoforge.mods.toml", "*.mixins.json")) { expand(props) }
    exclude("fabric.mod.json", "META-INF/mods.toml", "pack.mcmeta")
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}
