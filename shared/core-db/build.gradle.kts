import org.gradle.internal.os.OperatingSystem

plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
    id("kotlinx-serialization")
    id("app.cash.sqldelight")
}

android {
    namespace = "com.paligot.confily.core.db"
}

kotlin {
    androidTarget()

    js {
        binaries.executable()
        browser()
    }

    if (OperatingSystem.current().isMacOsX) {
        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "SharedDb"
                isStatic = false
                export(libs.settings)
                // Required https://github.com/cashapp/sqldelight/issues/1442
                linkerOpts.add("-lsqlite3")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.coroutines)
                implementation(libs.cash.sqldelight.runtime)
                api(libs.settings)
                implementation(libs.settings.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.cash.sqldelight.android)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.cash.sqldelight.web)
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            }
        }
        if (OperatingSystem.current().isMacOsX) {
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
                dependencies {
                    implementation(libs.cash.sqldelight.native)
                }
            }
        }
    }
}

sqldelight {
    databases {
        create("ConfilyDatabase") {
            packageName.set("com.paligot.confily.db")
        }
    }
}
