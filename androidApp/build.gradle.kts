import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose") version "1.0.1"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val composeVersion: String by project
val accompanistVersion: String by project
val settingsVersion: String by project
val datetimeVersion: String by project
val versionMajor = 0
val versionMinor = 1
val versionPatch = 5
android {
    compileSdk = 31
    defaultConfig {
        applicationId = "org.gdglille.devfest.android"
        minSdk = 21
        targetSdk = 31
        versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
    }

    val keystoreProps = project.file("keystore.properties")
    signingConfigs {
        val props = Properties()
        if (keystoreProps.exists()) {
            props.load(keystoreProps.reader())
        }
        create("release") {
            keyAlias = props.getProperty("keyAlias")
            keyPassword = props.getProperty("keyPassword")
            storeFile = file("keystore.release")
            storePassword = props.getProperty("keyPassword")
        }
        getByName("debug") {
            keyAlias = "debug"
            keyPassword = "devfest"
            storeFile = file("./keystore.debug")
            storePassword = "devfest"
        }
    }

    val appProps = project.file("app.properties")
    buildTypes {
        val props = Properties()
        if (appProps.exists()) {
            props.load(appProps.reader())
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            if (keystoreProps.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            buildConfigField("String", "BASE_URL", "\"${props.getProperty("BASE_URL")}\"")
            buildConfigField("String", "EVENT_ID", "\"${props.getProperty("EVENT_ID")}\"")
            buildConfigField("String", "CONTACT_MAIL", "\"${props.getProperty("CONTACT_MAIL")}\"")
            buildConfigField("String", "OPENFEEDBACK_PROJECT_ID", "\"${props.getProperty("OPENFEEDBACK_PROJECT_ID")}\"")
            buildConfigField("String", "FIREBASE_PROJECT_ID", "\"${props.getProperty("FIREBASE_PROJECT_ID")}\"")
            buildConfigField("String", "FIREBASE_APP_ID", "\"${props.getProperty("FIREBASE_APP_ID")}\"")
            buildConfigField("String", "FIREBASE_API_KEY", "\"${props.getProperty("FIREBASE_API_KEY")}\"")
        }
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"${props.getProperty("BASE_URL")}\"")
            buildConfigField("String", "EVENT_ID", "\"${props.getProperty("EVENT_ID")}\"")
            buildConfigField("String", "CONTACT_MAIL", "\"${props.getProperty("CONTACT_MAIL")}\"")
            buildConfigField("String", "OPENFEEDBACK_PROJECT_ID", "\"${props.getProperty("OPENFEEDBACK_PROJECT_ID")}\"")
            buildConfigField("String", "FIREBASE_PROJECT_ID", "\"${props.getProperty("FIREBASE_PROJECT_ID")}\"")
            buildConfigField("String", "FIREBASE_APP_ID", "\"${props.getProperty("FIREBASE_APP_ID")}\"")
            buildConfigField("String", "FIREBASE_API_KEY", "\"${props.getProperty("FIREBASE_API_KEY")}\"")
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
    implementation("androidx.browser:browser:1.4.0")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.4.2")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    implementation(platform("com.google.firebase:firebase-bom:29.3.1"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("com.google.mlkit:barcode-scanning:17.0.2")
    implementation("androidx.camera:camera-camera2:1.1.0-beta03")
    implementation("androidx.camera:camera-lifecycle:1.1.0-beta03")
    implementation("androidx.camera:camera-view:1.1.0-beta03")
    // Required by AndroidX Camera but another dependency generate a conflict with Guava.
    implementation("com.google.guava:guava:31.1-android")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0") {
        isTransitive = false
    }
    implementation("com.google.zxing:core:3.4.1")
}