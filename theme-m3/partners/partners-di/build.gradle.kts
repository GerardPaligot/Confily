plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.partners.di"
}

dependencies {
    implementation(projects.themeM3.partners.partnersPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
