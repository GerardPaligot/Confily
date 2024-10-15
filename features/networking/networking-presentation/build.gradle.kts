import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.networking.presentation"
}

kotlin {
    androidTarget()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.networking.networkingPanes)
                implementation(projects.features.networking.networkingUi)
                implementation(projects.features.navigation)
                implementation(projects.style.networking)
                implementation(projects.style.theme)

                implementation(compose.material3)
                implementation(compose.components.resources)

                implementation(libs.jetbrains.kotlinx.collections)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)

                implementation(libs.koin.compose.viewmodel)
            }
        }
        androidMain {
            dependencies {
                implementation(projects.style.components.permissions)
            }
        }
    }
}
