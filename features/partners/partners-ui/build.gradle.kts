import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.partners.ui"

    dependencies {
        debugImplementation(compose.uiTooling)
    }
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
                implementation(projects.themeM3.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.events)
                implementation(projects.style.partners)
                implementation(projects.style.theme)

                implementation(compose.material3)
                implementation(compose.materialIconsExtended)

                implementation(libs.jetbrains.kotlinx.collections)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }
    }
}
