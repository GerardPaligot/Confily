package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class RoutesLibraryPlugin : Plugin<Project> {
    @OptIn(ExperimentalWasmDsl::class)
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("confily.multiplatform.library")
                apply("confily.android.library.compose")
                apply("confily.quality")
                apply("kotlinx-serialization")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val compose = extensions.getByType<ComposeExtension>()
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                wasmJs {
                    useCommonJs()
                    browser()
                }
                sourceSets.commonMain.dependencies {
                    api(project(":shared:core-navigation"))
                    implementation(compose.dependencies.material3)
                    implementation(libs.findLibrary("jetbrains-kotlinx-serialization-json").get())
                }
            }
        }
    }
}
