import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.speakers.ui"
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
                implementation(projects.themeM3.style.events)
                implementation(projects.themeM3.style.components.placeholder)
                implementation(projects.themeM3.style.components.markdown)
                implementation(projects.themeM3.style.speakers)
                implementation(projects.themeM3.style.theme)

                implementation(compose.material3)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(compose.uiTooling)
            }
        }
    }
}
