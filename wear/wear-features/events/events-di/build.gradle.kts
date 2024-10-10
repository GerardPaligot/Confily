plugins {
    id("confily.android.wear.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.events.di"
}

dependencies {
    implementation(projects.wear.wearFeatures.events.eventsPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
