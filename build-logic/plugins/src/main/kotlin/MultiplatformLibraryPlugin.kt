
import com.android.build.gradle.LibraryExtension
import extensions.configureDesugaring
import extensions.configureKotlinAndroid
import extensions.configureKotlinCompiler
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class MultiplatformLibraryPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureKotlinCompiler(jvmVersion = 17)
                configureDesugaring(this)
                sourceSets.getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")
                defaultConfig.targetSdk = 35
            }
            // FIXME Android Studio build is looking for a testClasses task but it doesn't exist.
            tasks.register("testClasses")
        }
    }
}
