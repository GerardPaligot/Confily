import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.di"
    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    androidTarget()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SharedDi"
            isStatic = false
            export(libs.settings)
            export(projects.shared.coreApi)
            export(projects.shared.coreDb)
            export(projects.shared.core)
            export(projects.shared.coreKvalue)
            export(projects.shared.models)
            export(projects.shared.uiModels)
            export(projects.shared.resources)
            // Required https://github.com/cashapp/sqldelight/issues/1442
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.core)
                api(projects.shared.coreApi)
                api(projects.shared.coreKvalue)
                api(projects.shared.resources)
                implementation(libs.koin.core)
                implementation(libs.ktor.client.core)
                implementation(libs.squareup.okio)
                implementation(libs.jetbrains.kotlinx.coroutines)
                implementation(libs.lyricist)
            }
        }
        val mobileMain by creating {
            kotlin.srcDir("src/mobileMain/kotlin")
            dependsOn(commonMain)
            dependencies {
                api(projects.shared.coreDb)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            dependsOn(mobileMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        wasmJsMain {
            dependencies {
                // Required https://github.com/russhwolf/multiplatform-settings/issues/209
                implementation(libs.jetbrains.kotlinx.browser)
                implementation(libs.settings.make.observable)
            }
        }
    }
}
