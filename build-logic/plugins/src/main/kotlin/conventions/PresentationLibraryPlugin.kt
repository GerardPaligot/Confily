package conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class PresentationLibraryPlugin: Plugin<Project> {
    @OptIn(ExperimentalWasmDsl::class)
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("confily.multiplatform.library")
                apply("confily.android.library.compose")
                apply("confily.quality")
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
                    implementation(compose.dependencies.material3)
                    implementation(libs.findLibrary("jetbrains-kotlinx-collections").get())
                    implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-compose").get())
                    implementation(libs.findLibrary("koin-compose-viewmodel").get())
                }
                sourceSets.androidMain.dependencies {
                    implementation(project(":style:components:adaptive"))

                    implementation(project.dependencies.platform(libs.findLibrary("androidx-compose-bom").get()))
                    implementation(libs.findLibrary("androidx-compose-material3-windowsizeclass").get())
                    implementation(libs.findBundle("androidx-compose-adaptive").get())
                    implementation(libs.findLibrary("androidx-activity-compose").get())
                    implementation(libs.findLibrary("androidx-navigation-compose").get())

                    implementation(project.dependencies.platform(libs.findLibrary("google-firebase-bom").get()))
                    implementation("com.google.firebase:firebase-crashlytics-ktx")
                }
            }
        }
    }
}
