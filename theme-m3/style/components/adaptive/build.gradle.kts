plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.style.adaptive"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.adaptive)
    implementation(libs.androidx.compose.material3.windowsizeclass)
}
