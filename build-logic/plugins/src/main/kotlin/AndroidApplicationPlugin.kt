
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import extensions.appId
import extensions.appProps
import extensions.configBuildKonfig
import extensions.configureDesugaring
import extensions.configureKotlinAndroid
import extensions.configureKotlinCompiler
import extensions.configureSigningConfig
import extensions.versionCode
import extensions.versionName
import extensions.versionProps
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("kotlin-parcelize")
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }
            extensions.configure<BaseAppModuleExtension> {
                defaultConfig {
                    applicationId = appProps.appId
                    versionCode = versionProps.versionCode
                    versionName = versionProps.versionName
                }
                configureSigningConfig(rootProject)
                configureKotlinAndroid()
                configureKotlinCompiler(jvmVersion = 21)
                configureDesugaring(this)
                defaultConfig.targetSdk = 35
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("lintChecks", libs.findLibrary("compose-linter").get())
            }
        }
    }
}