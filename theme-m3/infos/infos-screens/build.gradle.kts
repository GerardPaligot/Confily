import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.infos.screens"
}

kotlin {
    androidTarget()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.uiModels)
                implementation(projects.shared.resources)
                implementation(projects.themeM3.infos.infosUi)
                implementation(projects.themeM3.navigation)
                implementation(projects.themeM3.style.components.markdown)
                implementation(projects.themeM3.style.events)
                implementation(projects.themeM3.style.theme)

                implementation(compose.material3)
                implementation(compose.components.resources)

                api(libs.jetbrains.kotlinx.collections)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.themeM3.style.components.permissions)
                implementation(compose.preview)
            }
        }
    }
}
