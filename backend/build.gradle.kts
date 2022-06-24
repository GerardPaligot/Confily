import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-platform-jvm")
    application
    kotlin("plugin.serialization")
    id("com.google.cloud.tools.appengine") version "2.4.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("conferences4hall.quality")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation(projects.models)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.datetime)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.statuspages)
    implementation(libs.ktor.server.cors)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.client.java)

    implementation(libs.ktor.serialization.json)

    implementation(libs.logback)

    implementation(libs.google.cloud.firestore)
    implementation(libs.google.cloud.storage)
}

appengine {
    stage {
        setArtifact("build/libs/${project.name}-all.jar")
    }
    deploy {
        projectId = "GCLOUD_CONFIG"
        version = "GCLOUD_CONFIG"
    }
}

application {
    mainClass.set("org.gdglille.devfest.backend.ServerKt")
}
