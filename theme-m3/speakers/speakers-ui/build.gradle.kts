plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.speakers.ui"
}

dependencies {
    implementation(projects.uiModels)
    implementation(projects.themeM3.style)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.androidx.compose.icons)

    implementation(libs.coil.compose)

    implementation(libs.kotlinx.collections)
}
