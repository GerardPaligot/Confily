import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.compose.compiler)
    id("confily.quality")
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "speakerApp"
        browser {
            commonWebpackConfig {
                outputFileName = "speakerApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.rootDir.path)
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.style.theme)
            implementation(projects.shared.coreApi)
            implementation(projects.shared.coreDi)
            implementation(projects.shared.uiModels)
            implementation(projects.features.eventList.eventListPresentation)
            implementation(projects.features.schedules.schedulesPresentation)
            implementation(projects.features.speakers.speakersPresentation)
            implementation(projects.features.eventList.eventListDi)
            implementation(projects.features.schedules.schedulesDi)
            implementation(projects.features.speakers.speakersDi)
            implementation(projects.features.navigation)
            implementation(projects.shared.resources)

            implementation(compose.material3)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.components.resources)

            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor)

            implementation(libs.jetbrains.kotlinx.datetime)
            implementation(libs.jetbrains.kotlinx.collections)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.navigation.compose)

            implementation(libs.koin.compose.viewmodel)
        }
    }
}
