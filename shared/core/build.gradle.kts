import org.gradle.internal.os.OperatingSystem

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
}

android {
    namespace = "com.paligot.confily.core"
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
                export(projects.shared.coreApi)
                export(projects.shared.coreDb)
                export(projects.shared.coreFs)
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
                api(projects.shared.coreApi)
                api(projects.shared.coreDb)
                api(projects.shared.coreFs)
                api(projects.shared.models)
                api(projects.shared.uiModels)
                api(projects.shared.resources)

                implementation(compose.runtime)
                implementation(compose.components.resources)

                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.jetbrains.kotlinx.collections)
                implementation(libs.jetbrains.kotlinx.coroutines)

                implementation(libs.cash.sqldelight.coroutines)

                implementation(libs.lyricist)

                api(libs.settings)
                implementation(libs.settings.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.google.zxing)
                implementation(libs.zxing.android.embedded)
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }
}
