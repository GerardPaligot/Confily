import extensions.stringBuildConfigField
import extensions.toProperties

plugins {
    id("confily.android.application")
    id("org.jetbrains.kotlin.android")
    id("confily.quality")
}

val appProps = rootProject.file("config/app.properties").toProperties()
val versionMajor = appProps["VERSION_MAJOR"]?.toString()?.toInt() ?: 1
val versionMinor = appProps["VERSION_MINOR"]?.toString()?.toInt() ?: 0
val versionPatch = appProps["VERSION_PATCH"]?.toString()?.toInt() ?: 0
android {
    namespace = "com.paligot.confily.wear"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.paligot.confily.wear"
        minSdk = 30
        targetSdk = 35
        versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val appProps = rootProject.file("config/app.properties").toProperties()
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            stringBuildConfigField("BASE_URL", appProps)
        }
        debug {
            applicationIdSuffix = ".debug"
            stringBuildConfigField("BASE_URL", appProps)
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.shared.coreDi)
    implementation(projects.wear.wearFeatures.events.eventsDi)
    implementation(projects.wear.wearFeatures.main.main)
    implementation(projects.wear.wearFeatures.main.mainDi)
    implementation(projects.wear.wearFeatures.partners.partnersDi)
    implementation(projects.wear.wearFeatures.schedules.schedulesDi)
    implementation(projects.wear.wearFeatures.speakers.speakersDi)
    implementation(projects.wear.wearTheme)

    implementation(platform(libs.androidx.compose.bom))
    implementation(compose.ui)
    implementation(compose.preview)
    implementation(compose.materialIconsExtended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.androidx.wear)
    implementation(libs.androidx.wear.navigation)
    debugImplementation(compose.uiTooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.google.horologist.compose.tools)
    implementation(libs.google.horologist.tiles)
    implementation(libs.google.services.wearable)

    implementation(libs.jetbrains.kotlinx.collections)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.coil3.compose)
    implementation(libs.coil3.network.ktor)
    implementation(libs.coil3.svg)
}
