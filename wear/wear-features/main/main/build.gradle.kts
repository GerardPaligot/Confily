plugins {
    id("confily.android.wear.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.main"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.wear.wearFeatures.events.eventsPresentation)
    implementation(projects.wear.wearFeatures.partners.partnersPresentation)
    implementation(projects.wear.wearFeatures.schedules.schedulesPresentation)
    implementation(projects.wear.wearFeatures.speakers.speakersPresentation)
    implementation(projects.wear.wearTheme)

    implementation(compose.ui)
    implementation(libs.bundles.androidx.wear)
    implementation(libs.androidx.wear.navigation)

    implementation(libs.koin.androidx.compose)
}
