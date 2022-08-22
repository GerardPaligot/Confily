import extensions.configureKotlinCompiler
import org.gradle.api.Plugin
import org.gradle.api.Project

class BackendPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-platform-jvm")
                apply("org.gradle.application")
            }
            configureKotlinCompiler()
        }
    }
}
