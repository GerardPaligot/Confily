import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.infos.panes"
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
                implementation(projects.features.infos.infosUi)
                implementation(projects.features.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.events)
                implementation(projects.style.theme)

                implementation(compose.material3)
                implementation(compose.components.resources)

                api(libs.jetbrains.kotlinx.collections)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.style.components.permissions)
                implementation(compose.preview)
            }
        }
    }
}
