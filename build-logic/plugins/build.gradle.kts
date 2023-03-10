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
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "conferences4hall.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "conferences4hall.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidLibraryCompose") {
            id = "conferences4hall.android.library.compose"
            implementationClass = "AndroidLibraryComposePlugin"
        }
        register("multiplatformLibrary") {
            id = "conferences4hall.multiplatform.library"
            implementationClass = "MultiplatformLibraryPlugin"
        }
        register("backendApplication") {
            id = "conferences4hall.backend.application"
            implementationClass = "BackendPlugin"
        }
        register("kotlinQuality") {
            id = "conferences4hall.quality"
            implementationClass = "KotlinQualityPlugin"
        }
    }
}
