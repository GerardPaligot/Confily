package extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension

internal fun Project.configureKtlint() {
    configure<KtlintExtension> {
        debug.set(false)
        version.set("0.42.1")
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
        disabledRules.set(arrayListOf("experimental:argument-list-wrapping"))
        filter {
            exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
            // exclude("**/build/**")
            // exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}
