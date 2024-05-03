plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.speakers.feature"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.shared.resources)
    implementation(projects.themeM3.speakers.speakersScreens)
    implementation(projects.themeM3.navigation)
    implementation(projects.themeM3.style.theme)
    implementation(projects.themeM3.style.components.adaptive)

    implementation(libs.koin.androidx.compose)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.adaptive)
    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.compose.animation)

    implementation(platform(libs.google.firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
