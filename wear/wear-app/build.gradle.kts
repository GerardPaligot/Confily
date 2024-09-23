import extensions.stringBuildConfigField
import extensions.toProperties

plugins {
    id("confily.android.application")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.paligot.confily.wear"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val appProps = rootProject.file("androidApp/app.properties").toProperties()
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
    implementation(projects.shared.core)
    implementation(projects.shared.coreDi)
    implementation(projects.shared.uiModels)

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
    implementation(libs.markdown.renderer)
}
