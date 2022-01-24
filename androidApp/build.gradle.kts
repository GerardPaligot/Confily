plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose") version "1.0.1"
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
}

repositories {
    maven(uri("https://jitpack.io"))
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.22.0-rc")
    implementation("androidx.navigation:navigation-compose:2.4.0-rc01")
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(compose.uiTooling)
}