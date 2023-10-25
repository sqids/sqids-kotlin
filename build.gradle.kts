plugins {
  kotlin("jvm") version "1.9.0"
  `kotlin-dsl`
  id("java-library")
}

group = "org.sqids"

version = "1.0.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies { testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10") }

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(17) }
