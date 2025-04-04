package conventions

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class UiLibraryPlugin: Plugin<Project> {
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
            extensions.configure<LibraryExtension> {
                dependencies {
                    add("debugImplementation", compose.dependencies.uiTooling)
                }
            }
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                jvm("desktop")
                wasmJs {
                    useCommonJs()
                    browser()
                }
                sourceSets.commonMain.dependencies {
                    implementation(compose.dependencies.material3)
                    implementation(compose.dependencies.components.uiToolingPreview)
                    implementation(libs.findLibrary("jetbrains-kotlinx-collections").get())
                }
                sourceSets.androidMain.dependencies {
                    implementation(compose.dependencies.preview)
                }
                sourceSets["desktopTest"].dependencies {
                    implementation(compose.dependencies.desktop.uiTestJUnit4)
                    implementation(compose.dependencies.desktop.currentOs)
                }
            }
        }
    }
}
