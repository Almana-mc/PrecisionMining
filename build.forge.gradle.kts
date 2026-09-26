plugins {
    id("net.neoforged.moddev.legacyforge")
    id("neoforge-mutex")
}

val modId = property("mod.id") as String
val javaVersion = property("mod.java") as String

version = "${sc.current.version}-${property("mod.version")}"
base.archivesName = "${property("mod.name")}-forge"

legacyForge {
    version = property("deps.forge") as String

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

mixin {
    add(sourceSets.main.get(), "$modId.refmap.json")
    config("$modId.mixins.json")
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)

sourceSets.main {
    java.exclude("**/fabric/**", "**/neoforge/**")
}

tasks.processResources {
    val props = listOf("id", "name", "version", "authors", "description", "license")
        .associateWith { project.property("mod.$it") as String } +
        mapOf("minecraft" to project.property("mod.mc_compat") as String, "java" to "JAVA_$javaVersion")
    val refmapLine = "\"required\": true,\n  \"refmap\": \"${props["id"]}.refmap.json\","
    inputs.properties(props)
    filesMatching(listOf("META-INF/mods.toml", "*.mixins.json")) { expand(props) }
    filesMatching("*.mixins.json") {
        // ponytail: MDG omits refmap key
        filter { it.replace("\"required\": true,", refmapLine) }
    }
    exclude("fabric.mod.json", "META-INF/neoforge.mods.toml")
}

tasks.jar {
    manifest.attributes("MixinConfigs" to "$modId.mixins.json")
}

tasks.named("createMinecraftArtifacts") {
    dependsOn("stonecutterGenerate")
}
