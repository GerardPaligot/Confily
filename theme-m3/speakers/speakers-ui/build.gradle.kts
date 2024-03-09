plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.speakers.ui"
}

dependencies {
    implementation(projects.shared.uiModels)
    implementation(projects.themeM3.style.markdown)
    implementation(projects.themeM3.style.speakers)
    implementation(projects.themeM3.style.theme)

    implementation(compose.material3)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(compose.materialIconsExtended)

    implementation(libs.coil.compose)

    implementation(libs.jetbrains.kotlinx.collections)
}
