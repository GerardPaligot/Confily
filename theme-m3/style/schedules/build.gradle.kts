plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.style.schedules"
}

dependencies {
    implementation(projects.themeM3.style.speakers)
    implementation(projects.themeM3.style.theme)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.androidx.compose.icons)

    implementation(libs.jetbrains.kotlinx.collections)
}
