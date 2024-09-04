plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.partners.di"
}

dependencies {
    implementation(projects.features.partners.partnersPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
