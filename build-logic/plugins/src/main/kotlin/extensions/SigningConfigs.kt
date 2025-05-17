package extensions

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project

fun BaseAppModuleExtension.configureSigningConfig(
    project: Project
) {
    val keystoreFile = project.file("config/keystore.properties")
    val keystoreProps = keystoreFile.toProperties()
    signingConfigs {
        create("release") {
            keyAlias = keystoreProps.getProperty("keyAlias")
            keyPassword = keystoreProps.getProperty("keyPassword")
            storeFile = project.file("./config/keystore.release")
            storePassword = keystoreProps.getProperty("keyPassword")
        }
        getByName("debug") {
            keyAlias = "debug"
            keyPassword = "android"
            storeFile = project.file("./config/keystore.debug")
            storePassword = "android"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            if (keystoreFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
}
