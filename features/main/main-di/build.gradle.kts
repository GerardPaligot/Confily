plugins {
    id("confily.android.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.main.di"
}

dependencies {
    implementation(projects.features.main.main)
    implementation(projects.features.eventList.eventListDi)
    implementation(projects.features.infos.infosDi)
    implementation(projects.features.networking.networkingDi)
    implementation(projects.features.partners.partnersDi)
    implementation(projects.features.schedules.schedulesDi)
    implementation(projects.features.speakers.speakersDi)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
