
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import extensions.configureAndroidCompose
import extensions.configureDesugaring
import extensions.configureKotlinAndroid
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
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }
            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid(this)
                configureDesugaring(this)
                configureAndroidCompose(this)
                defaultConfig.targetSdk = 33
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("lintChecks", libs.findLibrary("compose-linter").get())
            }
        }
    }
}