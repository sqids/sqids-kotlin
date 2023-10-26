plugins {
    kotlin("jvm") version "1.9.0"
    `kotlin-dsl`
    id("java-library")
}

group = "org.sqids"

version = "1.0.0-SNAPSHOT"

repositories { mavenCentral() }

sourceSets {
    create("blocklist") {
        java.setSrcDirs(emptyList<String>())
        kotlin.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(listOf("sqids-blocklist/output"))
    }
    create("generated")
    main {
        val generated = sourceSets.named("generated").get()
        compileClasspath += generated.output
        runtimeClasspath += generated.output
    }
}

dependencies { testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10") }

tasks.named("compileGeneratedKotlin") { dependsOn("blocklist") }

tasks.register<JsonToKotlinArrayTask>("blocklist") {
    group = "build"
    sourceFiles.from(sourceSets.named("blocklist").get().resources)
    outputDirectories.from(sourceSets.named("generated").get().kotlin.sourceDirectories.first())
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(17) }
