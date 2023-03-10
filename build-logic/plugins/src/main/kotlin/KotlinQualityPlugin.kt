
import extensions.configureDetekt
import extensions.configureKtlint
import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinQualityPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jlleitschuh.gradle.ktlint")
                apply("io.gitlab.arturbosch.detekt")
            }
            configureKtlint()
            configureDetekt()
        }
    }
}