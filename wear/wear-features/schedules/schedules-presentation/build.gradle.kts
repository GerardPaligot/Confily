plugins {
    id("confily.android.wear.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.schedules.presentation"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.wear.wearFeatures.schedules.schedulesPanes)
    implementation(projects.wear.wearTheme)

    implementation(compose.ui)
    implementation(libs.bundles.androidx.wear)
    implementation(libs.androidx.wear.navigation)

    implementation(libs.koin.androidx.compose)
}
