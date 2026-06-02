plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.quiz.presentation"
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.models)
                implementation(projects.shared.resources)
                implementation(projects.features.quiz.quizPanes)
                implementation(projects.features.quiz.quizRoutes)
                implementation(projects.features.quiz.quizUi)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.jetbrains.kotlinx.coroutines.test)
            }
        }
    }
}
