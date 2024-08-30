
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import extensions.configureDesugaring
import extensions.configureKotlinAndroid
import extensions.stringBuildConfigField
import extensions.toProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidSampleApplicationPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("kotlin-parcelize")
            }
            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid(this)
                configureDesugaring(this)
                defaultConfig {
                    applicationId = "org.gdglille.devfest.android"
                    versionCode = 100
                    versionName = "1.0.0"
                    targetSdk = 34
                }
                signingConfigs {
                    getByName("debug") {
                        keyAlias = "debug"
                        keyPassword = "devfest"
                        storeFile = rootProject.file("androidApp/keystore.debug")
                        storePassword = "devfest"
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
            }
        }
    }
}