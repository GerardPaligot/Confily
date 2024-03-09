plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.infos.feature"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.themeM3.infos.infosUi)
    implementation(projects.themeM3.navigation)
    implementation(projects.themeM3.style.components.permissions)
    implementation(projects.themeM3.style.events)
    implementation(projects.themeM3.style.theme)

    implementation(libs.koin.androidx.compose)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(compose.material3)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.richtext.commonmark)

    implementation(platform(libs.google.firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
}
