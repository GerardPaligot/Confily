import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
    id("kotlinx-serialization")
}

android {
    namespace = "com.paligot.confily.core.navigation"
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
                implementation(projects.shared.resources)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(libs.jetbrains.kotlinx.collections)
                implementation(libs.jetbrains.kotlinx.serialization.json)
            }
        }
    }
}
