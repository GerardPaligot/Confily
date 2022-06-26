package extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureDetekt() {
    this.tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        reports {
            html {
                required.set(true)
                outputLocation.set(file("build/reports/detekt.html"))
            }
            xml {
                required.set(true)
                outputLocation.set(file("build/reports/detekt.xml"))
            }
        }
    }
}
