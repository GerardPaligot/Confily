plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.navigation"
}

dependencies {
    implementation(projects.themeM3.style.theme)

    implementation(libs.androidx.annotation)
    implementation(compose.materialIconsExtended)
}
