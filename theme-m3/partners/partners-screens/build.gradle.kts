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
    implementation(projects.shared.resources)
    implementation(projects.themeM3.partners.partnersUi)
    implementation(projects.themeM3.partners.partnersSemantics)
    implementation(projects.themeM3.navigation)
    implementation(projects.themeM3.style.components.placeholder)
    implementation(projects.themeM3.style.events)
    implementation(projects.themeM3.style.partners)
    implementation(projects.themeM3.style.theme)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
}
