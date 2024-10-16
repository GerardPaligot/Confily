plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.main"
}

dependencies {
    api(projects.features.schedules.schedulesUi)
    api(projects.features.speakers.speakersUi)
    api(projects.style.theme)
    implementation(projects.features.schedules.schedulesPresentation)
    implementation(projects.features.speakers.speakersPresentation)
    implementation(projects.features.partners.partnersPresentation)
    implementation(projects.features.networking.networkingPresentation)
    implementation(projects.features.infos.infosPresentation)
    implementation(projects.features.infos.infosPanes)
    implementation(projects.features.eventList.eventListPresentation)
    implementation(projects.features.navigation)
    implementation(projects.style.components.adaptive)
    implementation(projects.shared.core)
    implementation(projects.shared.uiModels)
    implementation(projects.shared.resources)

    implementation(libs.koin.androidx.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3.windowsizeclass)
    implementation(libs.bundles.androidx.compose.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.jetbrains.kotlinx.collections)
    implementation(libs.jetbrains.lifecycle.viewmodel.compose)

    implementation(platform(libs.google.firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
