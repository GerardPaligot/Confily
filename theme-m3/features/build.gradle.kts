plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.features"
}

dependencies {
    api(projects.themeM3.ui)
    implementation(projects.uiResources)
    implementation(projects.androidData)
    implementation(projects.shared)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.lifecycle)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.navigation)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.pager)

    implementation(libs.kotlinx.collections)
}
