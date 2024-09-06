plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.networking.presentation"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.shared.resources)
    implementation(projects.features.networking.networkingPanes)
    implementation(projects.features.networking.networkingUi)
    implementation(projects.features.navigation)
    implementation(projects.style.components.permissions)
    implementation(projects.style.networking)
    implementation(projects.style.theme)

    implementation(libs.koin.androidx.compose)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(platform(libs.google.firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
