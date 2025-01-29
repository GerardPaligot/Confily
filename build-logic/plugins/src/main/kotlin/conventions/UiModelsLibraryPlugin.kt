package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class UiModelsLibraryPlugin : Plugin<Project> {
    @OptIn(ExperimentalWasmDsl::class)
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("confily.multiplatform.library")
                apply("confily.quality")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                jvm("desktop")
                wasmJs {
                    useCommonJs()
                    browser()
                }
                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach {
                    it.binaries.framework {
                        baseName = target.name
                        isStatic = false
                    }
                }
                sourceSets.commonMain.dependencies {
                    implementation(libs.findLibrary("jetbrains-kotlinx-collections").get())
                }
            }
        }
    }
}
