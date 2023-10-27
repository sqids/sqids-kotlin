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
    val generated = create("generated")
    main {
        compileClasspath += generated.output
        runtimeClasspath += generated.output
    }
    test {
        compileClasspath += generated.output
        runtimeClasspath += generated.output
    }
}

dependencies {
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("io.kotest:kotest-framework-datatest:5.6.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
}

tasks.named("compileGeneratedKotlin") { dependsOn("blocklist") }

tasks.register<JsonToKotlinArrayTask>("blocklist") {
    group = "build"
    sourceFiles.from(sourceSets.named("blocklist").get().resources)
    outputDirectories.from(sourceSets.named("generated").get().kotlin.sourceDirectories.first())
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(17) }
