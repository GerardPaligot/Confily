@file:Suppress("UnstableApiUsage")

package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

private val CommonExtension<*, *, *, *, *, *>.hasKotlinOptionsExt: Boolean
    get() = (this is ExtensionAware) && this.extensions.findByName("kotlinOptions") != null

fun CommonExtension<*, *, *, *, *, *>.configureKotlinAndroid(
    compileSdk: Int = 35,
    minSdk: Int = 23
) {
    this.compileSdk = compileSdk

    defaultConfig {
        this.minSdk = minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    // KMP modules don't have an access to the `kotlinOptions` extension
    if (hasKotlinOptionsExt) {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_21.toString()
        }
    }
}

fun Project.configureDesugaring(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }
    }

    dependencies {
        add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:1.1.5")
    }
}

private fun CommonExtension<*, *, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
