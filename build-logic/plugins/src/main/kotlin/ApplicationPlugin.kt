import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("confily.android.application")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val compose = extensions.getByType<ComposeExtension>()
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                jvm("desktop")
                sourceSets["desktopMain"].dependencies {
                    implementation(compose.dependencies.desktop.currentOs)
                    implementation(libs.findLibrary("jetbrains-kotlinx-coroutines-swing").get())
                }
            }
        }
    }
}
