plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.vitamin.features"
}

dependencies {
    api(projects.themeVitamin.ui)
    implementation(projects.uiResources)
    implementation(projects.androidData)
    implementation(projects.shared)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material2)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.navigation)

    implementation(libs.vitamin.compose.foundation)
    implementation(libs.vitamin.compose.appbars)
    implementation(libs.vitamin.compose.tabs)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)

    implementation(libs.kotlinx.collections)
}
