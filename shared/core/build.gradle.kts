import org.gradle.internal.os.OperatingSystem

plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
    id("app.cash.sqldelight")
}

android {
    namespace = "org.gdglille.devfest.android.shared"
}

kotlin {
    androidTarget()

    if (OperatingSystem.current().isMacOsX) {
        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "Shared"
                isStatic = false
                export(libs.settings)
                export(projects.shared.models)
                export(projects.shared.uiModels)
                export(projects.shared.resources)
                // Required https://github.com/cashapp/sqldelight/issues/1442
                linkerOpts.add("-lsqlite3")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.models)
                api(projects.shared.uiModels)
                api(projects.shared.resources)

                implementation(compose.runtime)
                implementation(compose.components.resources)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.negotiation)
                implementation(libs.ktor.serialization.json)

                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.collections)
                implementation(libs.jetbrains.kotlinx.coroutines)

                implementation(libs.cash.sqldelight.runtime)
                implementation(libs.cash.sqldelight.coroutines)
                implementation(libs.squareup.okio)

                implementation(libs.lyricist)

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
                implementation(libs.cash.sqldelight.android)
                implementation(libs.ktor.client.android)
                implementation(libs.google.zxing)
                implementation(libs.zxing.android.embedded)
            }
        }
        val androidUnitTest by getting {
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
                    implementation(libs.cash.sqldelight.native)
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

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }
}

sqldelight {
    databases {
        create("Conferences4HallDatabase") {
            packageName.set("org.gdglille.devfest.db")
        }
        // sourceFolders = listOf("sqldelight")
    }
}
