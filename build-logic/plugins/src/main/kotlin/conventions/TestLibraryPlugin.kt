package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ExperimentalComposeLibrary

class TestLibraryPlugin: Plugin<Project> {
    @OptIn(ExperimentalComposeLibrary::class)
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("confily.android.library")
                apply("confily.android.library.compose")
                apply("confily.quality")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val compose = extensions.getByType<ComposeExtension>()
            dependencies {
                add("api", project(":android-core:core-test-patterns"))
                add("api", libs.findLibrary("androidx-espresso-core").get())
                add("api", compose.dependencies.uiTestJUnit4)
            }
        }
    }
}
