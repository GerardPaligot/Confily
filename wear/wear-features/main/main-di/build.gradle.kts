plugins {
    id("confily.android.wear.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.main.di"
}

dependencies {
    implementation(projects.wear.wearFeatures.main.main)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
