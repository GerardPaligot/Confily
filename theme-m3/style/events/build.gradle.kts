plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.style.events"
}

dependencies {
    implementation(projects.themeM3.style.theme)

    implementation(compose.material3)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(compose.materialIconsExtended)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(libs.font.awesome)
}
