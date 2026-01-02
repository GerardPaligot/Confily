import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.withType

plugins {
    id("confily.backend.application")
    id("confily.quality")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    implementation(projects.shared.models)
    implementation(libs.jetbrains.kotlinx.coroutines)
    implementation(libs.jetbrains.kotlinx.datetime)
    implementation(libs.bundles.jetbrains.exposed)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.statuspages)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.conditional)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.client.java)

    implementation(libs.ktor.serialization.json)

    implementation(libs.apache.xmlgraphics.batik.transcoder)
    implementation(libs.apache.xmlgraphics.batik.codec)

    implementation(libs.logback)

    implementation(libs.google.cloud.firestore)
    implementation(libs.google.cloud.storage)
    implementation(libs.google.cloud.secret)
    implementation(libs.google.api.client)
    implementation(libs.google.auth.client)
    implementation(libs.google.drive)

    testImplementation(kotlin("test"))
    testImplementation(libs.mockk)
}

val mainKlass = "com.paligot.confily.backend.ServerKt"
application {
    this.mainClass = mainKlass
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = mainKlass
    }
}
