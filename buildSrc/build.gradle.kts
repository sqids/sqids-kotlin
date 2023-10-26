plugins {
    kotlin("jvm") version "1.9.0"
    `kotlin-dsl`
}

repositories { mavenCentral() }

dependencies {
    implementation("com.beust:klaxon:5.6")
    implementation("com.squareup:kotlinpoet:1.12.0")
}
