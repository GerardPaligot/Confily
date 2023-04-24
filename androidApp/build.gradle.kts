import extensions.stringBuildConfigField
import extensions.toProperties

plugins {
    id("conferences4hall.android.application")
    id("conferences4hall.quality")
}

enum class DesignSystem {
    M3, Vitamin
}
val designSystem = DesignSystem.M3
val versionMajor = 2
val versionMinor = 0
val versionPatch = 2
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
        }
        getByName("debug") {
            stringBuildConfigField("BASE_URL", appProps)
            stringBuildConfigField("EVENT_ID", appProps)
            stringBuildConfigField("CONTACT_MAIL", appProps)
            stringBuildConfigField("FIREBASE_PROJECT_ID", appProps)
            stringBuildConfigField("FIREBASE_APP_ID", appProps)
            stringBuildConfigField("FIREBASE_API_KEY", appProps)
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
            proguardFiles("benchmark-rules.pro")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    if (designSystem == DesignSystem.Vitamin) {
        implementation(projects.themeVitamin.features)
    } else if (designSystem == DesignSystem.M3) {
        implementation(projects.themeM3.features)
    }
    implementation(projects.androidData)
    implementation(projects.shared)
    implementation(projects.uiResources)
    implementation(libs.settings)

    implementation(libs.android.material)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.profile)
}
