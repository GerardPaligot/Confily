import extensions.stringBuildConfigField
import extensions.toProperties

plugins {
    id("conferences4hall.android.application")
    id("conferences4hall.quality")
    id("com.android.application")
    id("androidx.baselineprofile")
}

val versionMajor = 3
val versionMinor = 2
val versionPatch = 1
android {
    namespace = "org.gdglille.devfest.android"
    defaultConfig {
        applicationId = "org.gdglille.devfest.android"
        versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }

    val keystoreFile = project.file("keystore.properties")
    val keystoreProps = keystoreFile.toProperties()
    signingConfigs {
        create("release") {
            keyAlias = keystoreProps.getProperty("keyAlias")
            keyPassword = keystoreProps.getProperty("keyPassword")
            storeFile = file("keystore.release")
            storePassword = keystoreProps.getProperty("keyPassword")
        }
        getByName("debug") {
            keyAlias = "debug"
            keyPassword = "devfest"
            storeFile = file("./keystore.debug")
            storePassword = "devfest"
        }
    }

    val appProps = project.file("app.properties").toProperties()
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            if (keystoreFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            stringBuildConfigField("BASE_URL", appProps)
            stringBuildConfigField("EVENT_ID", appProps)
            stringBuildConfigField("CONTACT_MAIL", appProps)
            stringBuildConfigField("FIREBASE_PROJECT_ID", appProps)
            stringBuildConfigField("FIREBASE_APP_ID", appProps)
            stringBuildConfigField("FIREBASE_API_KEY", appProps)
            stringBuildConfigField("DEFAULT_EVENT", appProps)
        }
        getByName("debug") {
            stringBuildConfigField("BASE_URL", appProps)
            stringBuildConfigField("EVENT_ID", appProps)
            stringBuildConfigField("CONTACT_MAIL", appProps)
            stringBuildConfigField("FIREBASE_PROJECT_ID", appProps)
            stringBuildConfigField("FIREBASE_APP_ID", appProps)
            stringBuildConfigField("FIREBASE_API_KEY", appProps)
            stringBuildConfigField("DEFAULT_EVENT", appProps)
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.themeM3.main.main)
    implementation(projects.themeM3.main.mainDi)
    implementation(projects.themeM3.navigation)
    implementation(projects.widgets.widgetsFeature)
    implementation(projects.widgets.widgetsStyle)
    implementation(projects.shared.core)
    implementation(projects.shared.coreDi)
    implementation(projects.shared.resources)
    implementation(libs.settings)

    implementation(libs.google.material)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3.windowsizeclass)
    implementation(compose.ui)
    implementation(compose.components.resources)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profile)
    implementation(libs.androidx.workmanager.ktx)
    implementation(libs.bundles.androidx.glance)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.workmanager)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    baselineProfile(projects.baselineprofile)
}

baselineProfile {
    automaticGenerationDuringBuild = false
}
