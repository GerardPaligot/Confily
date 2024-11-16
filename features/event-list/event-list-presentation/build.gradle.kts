import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.events.presentation"
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
                implementation(projects.features.eventList.eventListUi)
                implementation(projects.features.eventList.eventListPanes)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.material3)
                implementation(compose.components.resources)

                implementation(libs.jetbrains.kotlinx.collections)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
                implementation(libs.jetbrains.kotlinx.datetime)

                implementation(libs.koin.compose.viewmodel)
            }
        }
    }
}
