plugins {
    id("dev.kikugie.loom-back-compat")
}

val javaVersion = property("mod.java") as String

version = "${sc.current.version}-${property("mod.version")}"
base.archivesName = "${property("mod.name")}-fabric"

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
}

java.toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)

sourceSets.main {
    java.exclude("**/neoforge/**", "**/forge/**")
}

tasks.processResources {
    val props = listOf("id", "name", "version", "authors", "description", "license")
        .associateWith { project.property("mod.$it") as String } +
        mapOf("minecraft" to project.property("mod.mc_compat") as String, "java" to "JAVA_$javaVersion")
    inputs.properties(props)
    filesMatching(listOf("fabric.mod.json", "*.mixins.json")) { expand(props) }
    exclude("META-INF/*mods.toml")
}
