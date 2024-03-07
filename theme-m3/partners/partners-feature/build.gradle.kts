plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.partners.feature"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.themeM3.partners.partnersScreens)
    implementation(projects.themeM3.partners.partnersUi)
    implementation(projects.themeM3.navigation)
    implementation(projects.themeM3.style.partners)
    implementation(projects.themeM3.style.theme)

    implementation(libs.koin.androidx.compose)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(compose.preview)
    implementation(compose.material3)
    debugImplementation(compose.uiTooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(platform(libs.google.firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
