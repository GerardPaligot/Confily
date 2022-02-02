import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-platform-jvm")
    application
    kotlin("plugin.serialization")
    id("com.google.cloud.tools.appengine") version "2.4.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val kotlinVersion: String by project
val kotlinCoroutinesVersion: String by project
val ktorVersion: String by project
dependencies {
    implementation(project(":models"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-java:$ktorVersion")

    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("com.google.zxing:javase:3.4.1")
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.google.cloud:google-cloud-firestore:3.0.9")
    implementation("com.google.cloud:google-cloud-storage:2.3.0")
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
    mainClass.set("com.paligot.conferences.backend.ServerKt")
}
