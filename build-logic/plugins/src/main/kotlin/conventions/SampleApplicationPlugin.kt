package conventions

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import extensions.configBuildKonfig
import extensions.configureDesugaring
import extensions.configureKotlinAndroid
import extensions.configureKotlinCompiler
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class SampleApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("kotlin-parcelize")
                apply("com.codingfeline.buildkonfig")
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val compose = extensions.getByType<ComposeExtension>()
            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid()
                configureKotlinCompiler(jvmVersion = 21)
                configureDesugaring(this)
                sourceSets.getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
                defaultConfig {
                    applicationId = "com.paligot.confily.android"
                    versionCode = 100
                    versionName = "1.0.0"
                    targetSdk = 35
                }
                signingConfigs {
                    getByName("debug") {
                        keyAlias = "debug"
                        keyPassword = "android"
                        storeFile = rootProject.file("config/keystore.debug")
                        storePassword = "android"
                    }
                }
                testOptions {
                    managedDevices {
                        localDevices.create("pixel8api34") {
                            device = "Pixel 8"
                            apiLevel = 34
                            systemImageSource = "aosp"
                        }
                    }
                }
                packaging {
                    resources.excludes.addAll(
                        listOf(
                            "META-INF/LICENSE.md",
                            "META-INF/LICENSE-notice.md"
                        )
                    )
                }
            }
            extensions.configure<BuildKonfigExtension> {
                configBuildKonfig(target)
            }
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
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
        }
    }
}
