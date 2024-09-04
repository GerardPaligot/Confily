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
    implementation(projects.features.infos.infosDi)
    implementation(projects.features.networking.networkingDi)
    implementation(projects.features.partners.partnersDi)
    implementation(projects.features.schedules.schedulesDi)
    implementation(projects.features.speakers.speakersDi)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
