plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.schedules.screens"
}

dependencies {
    implementation(projects.shared.core)
    implementation(projects.themeM3.schedules.schedulesUi)
    implementation(projects.themeM3.navigation)
    implementation(projects.themeM3.style.schedules)
    implementation(projects.themeM3.style.speakers)
    implementation(projects.themeM3.style.theme)

    implementation(libs.kotlinx.collections)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.tooling)
}
