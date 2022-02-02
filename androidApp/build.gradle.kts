plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose") version "1.0.1"
}

val composeVersion: String by project
val settingsVersion: String by project
android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.paligot.conferences.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

repositories {
    maven(uri("https://jitpack.io"))
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":shared"))
    implementation("com.russhwolf:multiplatform-settings:$settingsVersion")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.4.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.22.0-rc")
    implementation("com.google.accompanist:accompanist-permissions:0.24.1-alpha")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")

    implementation("com.google.mlkit:barcode-scanning:17.0.2")
    implementation("androidx.camera:camera-camera2:1.1.0-beta01")
    implementation("androidx.camera:camera-lifecycle:1.1.0-beta01")
    implementation("androidx.camera:camera-view:1.1.0-beta01")
}