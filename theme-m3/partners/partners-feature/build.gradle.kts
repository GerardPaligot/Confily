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
    implementation(projects.shared.resources)
    implementation(projects.themeM3.partners.partnersPanes)
    implementation(projects.themeM3.partners.partnersUi)
    implementation(projects.themeM3.navigation)
    implementation(projects.style.components.adaptive)
    implementation(projects.style.partners)
    implementation(projects.style.theme)

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

    implementation(platform(libs.google.firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
