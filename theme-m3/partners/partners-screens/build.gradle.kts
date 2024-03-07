plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.partners.screens"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.themeM3.partners.partnersUi)
    implementation(projects.themeM3.navigation)
    implementation(projects.themeM3.style.partners)
    implementation(projects.themeM3.style.theme)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.tooling)
}
