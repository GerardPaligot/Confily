plugins {
    id("confily.android.wear.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.partners.di"
}

dependencies {
    implementation(projects.wear.wearFeatures.partners.partnersPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
