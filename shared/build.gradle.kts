import org.gradle.internal.os.OperatingSystem

plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.quality")
    id("kotlinx-serialization")
    id("com.rickclephas.kmp.nativecoroutines")
    id("com.squareup.sqldelight")
}

android {
    namespace = "org.gdglille.devfest.android.shared"
}

kotlin {
    android()

    if (OperatingSystem.current().isMacOsX) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "shared"
                export(libs.settings)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.models)
                implementation(libs.kotlinx.coroutines)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.negotiation)
                implementation(libs.ktor.serialization.json)

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.collections)

                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.okio)

                api(libs.settings)
                implementation(libs.settings.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
                implementation(libs.sqldelight.android)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        if (OperatingSystem.current().isMacOsX) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
                dependencies {
                    implementation(libs.ktor.client.ios)
                    implementation(libs.sqldelight.native)
                }
            }
            val iosX64Test by getting
            val iosArm64Test by getting
            val iosSimulatorArm64Test by getting
            val iosTest by creating {
                dependsOn(commonTest)
                iosX64Test.dependsOn(this)
                iosArm64Test.dependsOn(this)
                iosSimulatorArm64Test.dependsOn(this)
            }
        }
    }
}

sqldelight {
    database("Conferences4HallDatabase") {
        packageName = "org.gdglille.devfest.db"
        sourceFolders = listOf("sqldelight")
    }
}
