import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.di"
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
                implementation(projects.features.speakers.speakersPresentation)
                implementation(projects.shared.coreDi)

                implementation(libs.koin.compose.viewmodel)
                implementation(libs.lyricist)
            }
        }
    }
}
