plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(libs.android.gradlePlugin)
    implementation(libs.jetbrains.compose.gradlePlugin)
    implementation(libs.jetbrains.compose.compiler.gradlePlugin)
    implementation(libs.jetbrains.kotlin.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "confily.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidSampleApplication") {
            id = "confily.android.sample"
            implementationClass = "AndroidSampleApplicationPlugin"
        }
        register("androidLibrary") {
            id = "confily.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidLibraryCompose") {
            id = "confily.android.library.compose"
            implementationClass = "AndroidLibraryComposePlugin"
        }
        register("multiplatformLibrary") {
            id = "confily.multiplatform.library"
            implementationClass = "MultiplatformLibraryPlugin"
        }
        register("uiLibrary") {
            id = "confily.ui"
            implementationClass = "conventions.UiLibraryPlugin"
        }
        register("panesLibrary") {
            id = "confily.panes"
            implementationClass = "conventions.PanesLibraryPlugin"
        }
        register("presentationLibrary") {
            id = "confily.presentation"
            implementationClass = "conventions.PresentationLibraryPlugin"
        }
        register("androidWearLibrary") {
            id = "confily.android.wear.library"
            implementationClass = "AndroidWearLibraryPlugin"
        }
        register("backendApplication") {
            id = "confily.backend.application"
            implementationClass = "BackendPlugin"
        }
        register("kotlinQuality") {
            id = "confily.quality"
            implementationClass = "KotlinQualityPlugin"
        }
    }
}
