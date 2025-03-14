package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SemanticsLibraryPlugin: Plugin<Project> {
    @OptIn(ExperimentalWasmDsl::class)
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("confily.multiplatform.library")
                apply("confily.quality")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                jvm("desktop")
                wasmJs {
                    useCommonJs()
                    browser()
                }
            }
        }
    }
}
