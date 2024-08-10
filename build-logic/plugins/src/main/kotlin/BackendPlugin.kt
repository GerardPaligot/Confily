import extensions.configureKotlinCompiler
import org.gradle.api.Plugin
import org.gradle.api.Project

class BackendPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.gradle.application")
            }
            configureKotlinCompiler(jvmVersion = 21)
        }
    }
}
