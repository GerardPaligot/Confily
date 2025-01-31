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
        register("application") {
            id = "confily.application"
            implementationClass = "ApplicationPlugin"
        }
        register("sampleApplication") {
            id = "confily.sample"
            implementationClass = "conventions.SampleApplicationPlugin"
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
        register("uiModelsLibrary") {
            id = "confily.ui.models"
            implementationClass = "conventions.UiModelsLibraryPlugin"
        }
        register("panesLibrary") {
            id = "confily.panes"
            implementationClass = "conventions.PanesLibraryPlugin"
        }
        register("presentationLibrary") {
            id = "confily.presentation"
            implementationClass = "conventions.PresentationLibraryPlugin"
        }
        register("routesLibrary") {
            id = "confily.routes"
            implementationClass = "conventions.RoutesLibraryPlugin"
        }
        register("diLibrary") {
            id = "confily.di"
            implementationClass = "conventions.DiLibraryPlugin"
        }
        register("semanticsLibrary") {
            id = "confily.semantics"
            implementationClass = "conventions.SemanticsLibraryPlugin"
        }
        register("testScopesLibrary") {
            id = "confily.test.scopes"
            implementationClass = "conventions.TestScopesLibraryPlugin"
        }
        register("testLibrary") {
            id = "confily.test"
            implementationClass = "conventions.TestLibraryPlugin"
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
