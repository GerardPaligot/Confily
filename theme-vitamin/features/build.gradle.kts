plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

dependencies {
    implementation(projects.themeVitamin.ui)
    implementation(projects.uiResources)
    implementation(projects.androidData)
    implementation(projects.shared)

    implementation(libs.androidx.compose.material2)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.navigation)

    implementation(libs.vitamin.compose.foundation)
    implementation(libs.vitamin.compose.appbars)
    implementation(libs.vitamin.compose.tabs)

    implementation(libs.accompanist.systemuicontroller)
}
