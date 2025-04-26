import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import extensions.toProperties

plugins {
    id("confily.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    id("confily.quality")
    alias(libs.plugins.buildkonfig)
    id("androidx.baselineprofile")
}

val appProps = rootProject.file("config/app.properties").toProperties()
val versionMajor = appProps["VERSION_MAJOR"]?.toString()?.toInt() ?: 1
val versionMinor = appProps["VERSION_MINOR"]?.toString()?.toInt() ?: 0
val versionPatch = appProps["VERSION_PATCH"]?.toString()?.toInt() ?: 0
val versionBuild = appProps["VERSION_BUILD"]?.toString()?.toInt() ?: 0
android {
    namespace = "com.paligot.confily.android"

    defaultConfig {
        applicationId = "com.paligot.confily.android"
        versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10 + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }
    sourceSets.getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")

    val keystoreFile = rootProject.file("config/keystore.properties")
    val keystoreProps = keystoreFile.toProperties()
    signingConfigs {
        create("release") {
            keyAlias = keystoreProps.getProperty("keyAlias")
            keyPassword = keystoreProps.getProperty("keyPassword")
            storeFile = rootProject.file("./config/keystore.release")
            storePassword = keystoreProps.getProperty("keyPassword")
        }
        getByName("debug") {
            keyAlias = "debug"
            keyPassword = "android"
            storeFile = rootProject.file("./config/keystore.debug")
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

    buildFeatures {
        buildConfig = true
    }

    dependencies {
        baselineProfile(projects.baselineprofile)
    }
}

buildkonfig {
    packageName = "com.paligot.confily"

    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", appProps["BASE_URL"] as String)
        buildConfigField(STRING, "DEFAULT_EVENT", appProps["DEFAULT_EVENT"] as String)
        buildConfigField(STRING, "DEFAULT_LANGUAGE", appProps["DEFAULT_LANGUAGE"] as String)
        buildConfigField(STRING, "FIREBASE_PROJECT_ID", appProps["FIREBASE_PROJECT_ID"] as String)
        buildConfigField(STRING, "FIREBASE_APP_ID", appProps["FIREBASE_APP_ID"] as String)
        buildConfigField(STRING, "FIREBASE_API_KEY", appProps["FIREBASE_API_KEY"] as String)
        buildConfigField(STRING, "VERSION_CODE", "$versionMajor.$versionMinor.$versionPatch")
    }
}

baselineProfile {
    automaticGenerationDuringBuild = false
}

kotlin {
    androidTarget()
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

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
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
        }
    }
}
