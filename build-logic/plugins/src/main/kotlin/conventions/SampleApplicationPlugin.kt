package conventions

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import extensions.configBuildKonfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SampleApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.codingfeline.buildkonfig")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val compose = extensions.getByType<ComposeExtension>()
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
                jvm("desktop")
                sourceSets.commonMain.dependencies {
                    implementation(project(":features:navigation"))
                    implementation(project(":shared:core"))
                    implementation(project(":shared:core-di"))
                    implementation(project(":shared:resources"))
                    implementation(project(":style:components:adaptive"))
                    implementation(project(":style:theme"))

                    implementation(compose.dependencies.material3)
                    implementation(libs.findBundle("jetbrains-compose-adaptive").get())
                    implementation(libs.findLibrary("jetbrains-navigation-compose").get())
                    implementation(libs.findBundle("koin").get())
                    implementation(libs.findLibrary("coil3-compose").get())
                    implementation(libs.findLibrary("coil3-network-ktor").get())
                    implementation(libs.findLibrary("coil3-svg").get())
                }
                sourceSets.androidMain.dependencies {
                    implementation(project(":android-core:core-sample"))
                    implementation(libs.findLibrary("androidx-appcompat").get())
                    implementation(libs.findLibrary("androidx-activity-compose").get())
                    implementation(libs.findLibrary("androidx-workmanager-ktx").get())
                    implementation(libs.findLibrary("koin-android").get())
                    implementation(libs.findLibrary("koin-androidx-workmanager").get())
                }
                sourceSets["desktopMain"].dependencies {
                    implementation(compose.dependencies.desktop.currentOs)
                    implementation(libs.findLibrary("jetbrains-kotlinx-coroutines-swing").get())
                }
            }
            extensions.configure<BuildKonfigExtension> {
                configBuildKonfig(target)
            }
            dependencies {
                add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:1.1.5")
            }
            tasks.register("testClasses")
        }
    }
}
