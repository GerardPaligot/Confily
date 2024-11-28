import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.presentation"
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
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.speakers.speakersPanes)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.material3)
                implementation(compose.components.resources)

                implementation(libs.jetbrains.kotlinx.collections)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)

                implementation(libs.koin.compose.viewmodel)
                implementation(libs.lyricist)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.style.components.adaptive)

                implementation(project.dependencies.platform(libs.androidx.compose.bom))
                implementation(libs.androidx.compose.material3.windowsizeclass)
                implementation(libs.bundles.androidx.compose.adaptive)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.navigation.compose)
                implementation(compose.preview)

                implementation(project.dependencies.platform(libs.google.firebase.bom))
                implementation("com.google.firebase:firebase-crashlytics-ktx")
            }
        }
    }
}
