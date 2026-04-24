package conventions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import extensions.configBuildKonfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.codingfeline.buildkonfig")
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
            extensions.configure<BuildKonfigExtension> {
                configBuildKonfig(target)
            }
        }
    }
}
