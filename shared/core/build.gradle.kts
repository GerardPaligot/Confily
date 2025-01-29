import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

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
            export(projects.shared.coreKvalue)
            export(projects.shared.models)
            export(projects.features.eventList.eventListUiModels)
            export(projects.features.infos.infosUiModels)
            export(projects.features.networking.networkingUiModels)
            export(projects.features.partners.partnersUiModels)
            export(projects.features.schedules.schedulesPanesModels)
            export(projects.features.schedules.schedulesUiModels)
            export(projects.features.speakers.speakersPanesModels)
            export(projects.features.speakers.speakersUiModels)
            export(projects.features.socials.socialsUiModels)
            export(projects.shared.resources)
            // Required https://github.com/cashapp/sqldelight/issues/1442
            linkerOpts.add("-lsqlite3")
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.coreApi)
                api(projects.shared.coreFs)
                api(projects.shared.coreKvalue)
                api(projects.shared.models)
                api(projects.features.eventList.eventListUiModels)
                api(projects.features.infos.infosUiModels)
                api(projects.features.networking.networkingUiModels)
                api(projects.features.partners.partnersUiModels)
                api(projects.features.schedules.schedulesPanesModels)
                api(projects.features.schedules.schedulesUiModels)
                api(projects.features.speakers.speakersPanesModels)
                api(projects.features.speakers.speakersUiModels)
                api(projects.features.socials.socialsUiModels)
                api(projects.shared.resources)

                implementation(compose.runtime)

                api(libs.jetbrains.kotlinx.datetime)
                api(libs.jetbrains.kotlinx.collections)
                api(libs.jetbrains.kotlinx.coroutines)

                implementation(libs.lyricist)
            }
        }
        val mobileMain by creating {
            kotlin.srcDir("src/mobileMain/kotlin")
            dependsOn(commonMain)
            dependencies {
                api(projects.shared.coreDb)
                implementation(libs.cash.sqldelight.coroutines)
            }
        }
        val androidMain by getting {
            dependsOn(mobileMain)
            dependencies {
                implementation(compose.components.resources)
                implementation(libs.google.zxing)
                implementation(libs.zxing.android.embedded)
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
        val desktopMain by getting {
            dependsOn(commonMain)
            dependsOn(mobileMain)
        }
        wasmJsMain {
            dependencies {
                implementation(libs.jetbrains.kotlinx.serialization.json)
            }
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    }
}
