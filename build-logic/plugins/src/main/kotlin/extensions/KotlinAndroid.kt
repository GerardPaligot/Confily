@file:Suppress("UnstableApiUsage")

package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun CommonExtension.configureKotlinAndroid(
    compileSdk: Int = 35,
    minSdk: Int = 23
) {
    this.compileSdk = compileSdk

    defaultConfig.minSdk = minSdk

    compileOptions.sourceCompatibility = JavaVersion.VERSION_21
    compileOptions.targetCompatibility = JavaVersion.VERSION_21
}

fun Project.configureDesugaring(
    commonExtension: CommonExtension,
) {
    commonExtension.compileOptions.isCoreLibraryDesugaringEnabled = true

    dependencies {
        add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:1.1.5")
    }
}
