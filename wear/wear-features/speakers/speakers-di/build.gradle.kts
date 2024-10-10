plugins {
    id("confily.android.wear.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.speakers.di"
}

dependencies {
    implementation(projects.wear.wearFeatures.speakers.speakersPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
