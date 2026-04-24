
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class MultiplatformLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                val android = (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as KotlinMultiplatformAndroidLibraryTarget
                android.apply {
                    compileSdk = 35
                    minSdk = 23
                    enableCoreLibraryDesugaring = true
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_21)
                    }
                }
            }
            dependencies {
                add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:1.1.5")
            }
            // FIXME Android Studio build is looking for a testClasses task but it doesn't exist.
            tasks.register("testClasses")
        }
    }
}
