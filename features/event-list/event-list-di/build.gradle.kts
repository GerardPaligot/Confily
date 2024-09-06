plugins {
    id("confily.android.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.events.di"
}

dependencies {
    implementation(projects.features.eventList.eventListPresentation)
    implementation(projects.shared.coreDi)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
}
