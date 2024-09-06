plugins {
    id("confily.android.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.di"
}

dependencies {
    implementation(projects.features.speakers.speakersPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
