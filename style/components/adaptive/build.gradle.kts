plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.style.components.adaptive"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose.adaptive)
    implementation(libs.androidx.compose.material3.windowsizeclass)
}
