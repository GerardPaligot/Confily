import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.infos.ui"

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
                implementation(projects.shared.resources)
                implementation(projects.features.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.theme)

                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)

                implementation(libs.jetbrains.kotlinx.collections)

                implementation(libs.coil3.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.uiCamera)

                implementation(compose.preview)
            }
        }
    }
}
