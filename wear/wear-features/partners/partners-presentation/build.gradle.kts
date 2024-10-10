plugins {
    id("confily.android.wear.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.partners.presentation"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.wear.wearFeatures.partners.partnersPanes)
    implementation(projects.wear.wearTheme)

    implementation(compose.ui)
    implementation(libs.bundles.androidx.wear)
    implementation(libs.androidx.wear.navigation)

    implementation(libs.koin.androidx.compose)
}
