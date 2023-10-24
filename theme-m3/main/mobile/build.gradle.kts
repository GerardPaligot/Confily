plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.main"
}

dependencies {
    api(projects.themeM3.schedules.schedulesUi)
    api(projects.themeM3.speakers.speakersUi)
    api(projects.themeM3.style.theme)
    implementation(projects.themeM3.schedules.schedulesFeature)
    implementation(projects.themeM3.speakers.speakersFeature)
    implementation(projects.themeM3.networking.networkingFeature)
    implementation(projects.themeM3.partners.partnersFeature)
    implementation(projects.themeM3.infos.infosFeature)
    implementation(projects.themeM3.eventList.eventListFeature)
    implementation(projects.themeM3.navigation)
    implementation(projects.shared.uiModels)
    implementation(projects.shared.core)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.navigation)

    implementation(libs.kotlinx.collections)

    implementation(platform(libs.google.firebase))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
