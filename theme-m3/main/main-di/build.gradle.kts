plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.main.di"
}

dependencies {
    implementation(projects.themeM3.main.main)
    implementation(projects.themeM3.eventList.eventListDi)
    implementation(projects.themeM3.infos.infosDi)
    implementation(projects.themeM3.networking.networkingDi)
    implementation(projects.themeM3.partners.partnersDi)
    implementation(projects.themeM3.schedules.schedulesDi)
    implementation(projects.themeM3.speakers.speakersDi)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
