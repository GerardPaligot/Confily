import extensions.appProps
import extensions.stringBuildConfigField

plugins {
    id("confily.android.application")
    id("org.jetbrains.kotlin.android")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear"
    compileSdk = 35

    defaultConfig {
        minSdk = 30
        targetSdk = 35
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            stringBuildConfigField("BASE_URL", appProps)
        }
        debug {
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

    implementation(compose.ui)
    implementation(compose.preview)
    implementation(compose.materialIconsExtended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.androidx.wear)
    implementation(libs.androidx.wear.navigation)
    debugImplementation(compose.uiTooling)

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
