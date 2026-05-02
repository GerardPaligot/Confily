plugins {
    id("confily.android.application")
    id("org.jetbrains.kotlin.android")
    id("confily.quality")
    id("androidx.baselineprofile")
}

android {
    namespace = "com.paligot.confily.android"

    buildFeatures {
        buildConfig = true
    }

    dependencies {
        baselineProfile(projects.baselineprofile)
    }
}

baselineProfile {
    automaticGenerationDuringBuild = false
}

dependencies {
    implementation(projects.composeApp)
    implementation(projects.shared.core)
    implementation(projects.shared.coreDi)
    implementation(projects.shared.resources)
    implementation(projects.features.main.main)
    implementation(projects.features.main.mainDi)
    implementation(projects.features.navigation)
    implementation(projects.features.networking.networkingRoutes)
    implementation(projects.features.schedules.schedulesRoutes)
    implementation(projects.widgets.widgetsPresentation)
    implementation(projects.widgets.widgetsStyle)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.profile)
    implementation(libs.androidx.workmanager.ktx)
    implementation(libs.bundles.androidx.glance)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.google.material)

    implementation(libs.jetbrains.navigation.compose)
    implementation(libs.coil3.compose)
    implementation(libs.coil3.network.ktor)
    implementation(libs.coil3.svg)
    implementation(libs.bundles.koin)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.workmanager)
    implementation(libs.lyricist)
    implementation(libs.openfeedback.viewmodel)
}
