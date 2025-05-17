package conventions

import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import extensions.configBuildKonfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.codingfeline.buildkonfig")
            }
            extensions.configure<BuildKonfigExtension> {
                configBuildKonfig(target)
            }
        }
    }
}
