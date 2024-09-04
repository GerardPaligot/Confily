plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.infos.di"
}

dependencies {
    implementation(projects.themeM3.infos.infosPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
