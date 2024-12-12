plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.main.di"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.features.main.main)
        implementation(projects.features.eventList.eventListDi)
        implementation(projects.features.infos.infosDi)
        implementation(projects.features.networking.networkingDi)
        implementation(projects.features.partners.partnersDi)
        implementation(projects.features.schedules.schedulesDi)
        implementation(projects.features.speakers.speakersDi)
        implementation(projects.shared.coreDi)
    }
}
