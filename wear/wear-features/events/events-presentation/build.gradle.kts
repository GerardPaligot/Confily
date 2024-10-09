plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.events.presentation"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.wear.wearFeatures.events.eventsPanes)
    implementation(projects.wear.wearTheme)

    implementation(compose.ui)
    implementation(libs.bundles.androidx.wear)
    implementation(libs.androidx.wear.navigation)

    implementation(libs.koin.androidx.compose)
}
