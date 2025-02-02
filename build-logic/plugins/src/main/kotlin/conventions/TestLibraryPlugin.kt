package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class TestLibraryPlugin: Plugin<Project> {
    @OptIn(ExperimentalComposeLibrary::class)
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("confily.multiplatform.library")
                apply("confily.android.library.compose")
                apply("confily.quality")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val compose = extensions.getByType<ComposeExtension>()
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                jvm("desktop")
                sourceSets.commonMain.dependencies {
                    api(compose.dependencies.desktop.uiTestJUnit4)
                }
                sourceSets.androidMain.dependencies {
                    api(project(":android-core:core-test-patterns"))
                    api(libs.findLibrary("androidx-espresso-core").get())
                    api(compose.dependencies.uiTestJUnit4)
                }
            }
        }
    }
}
