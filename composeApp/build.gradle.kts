import extensions.stringBuildConfigField
import extensions.toProperties

plugins {
    id("confily.application")
    id("confily.quality")
    id("androidx.baselineprofile")
}

val versionMajor = 3
val versionMinor = 4
val versionPatch = 1
android {
    namespace = "com.paligot.confily.android"
    defaultConfig {
        applicationId = "com.paligot.confily.android"
        versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }
    sourceSets.getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")

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
            keyPassword = "android"
            storeFile = file("./keystore.debug")
            storePassword = "android"
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
            applicationIdSuffix = ".debug"
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

    dependencies {
        baselineProfile(projects.baselineprofile)
    }
}

baselineProfile {
    automaticGenerationDuringBuild = true
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.main.main)
            implementation(projects.features.main.mainDi)
            implementation(projects.features.navigation)
            implementation(projects.features.schedules.schedulesRoutes)
            implementation(projects.features.networking.networkingRoutes)
            implementation(projects.shared.core)
            implementation(projects.shared.coreDi)
            implementation(projects.shared.resources)

            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.bundles.jetbrains.compose.adaptive)
            implementation(libs.jetbrains.kotlinx.datetime)

            implementation(libs.settings)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor)
            implementation(libs.coil3.svg)
            implementation(libs.bundles.koin)
        }
        androidMain.dependencies {
            implementation(projects.widgets.widgetsPresentation)
            implementation(projects.widgets.widgetsStyle)

            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.browser)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.profile)
            implementation(libs.androidx.workmanager.ktx)
            implementation(libs.bundles.androidx.glance)
            implementation(libs.google.material)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.workmanager)
        }
    }
}
