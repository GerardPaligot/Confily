plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.infos.presentation"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.shared.resources)
    implementation(projects.features.infos.infosUi)
    implementation(projects.features.infos.infosPanes)
    implementation(projects.themeM3.navigation)
    implementation(projects.style.components.markdown)
    implementation(projects.style.components.permissions)
    implementation(projects.style.events)
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
