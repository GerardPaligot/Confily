plugins {
    id("com.android.application")
    kotlin("android")
}

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-rc01"
    }
    buildFeatures {
        compose = true
    }
}

repositories {
    maven(uri("https://jitpack.io"))
}

val composeVersion: String by project
dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("com.github.Gurupreet:FontAwesomeCompose:1.0.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.22.0-rc")
    implementation("androidx.navigation:navigation-compose:2.4.0-rc01")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
}