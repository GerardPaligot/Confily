plugins {
    id("confily.android.wear.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.schedules.di"
}

dependencies {
    implementation(projects.wear.wearFeatures.schedules.schedulesPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.lyricist)
}
