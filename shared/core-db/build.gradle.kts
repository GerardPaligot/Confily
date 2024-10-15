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
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.cash.sqldelight.android)
            }
        }
        iosMain {
            dependencies {
                implementation(libs.cash.sqldelight.native)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.cash.sqldelight.web)
                implementation(devNpm("copy-webpack-plugin", "9.1.0"))
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
