plugins {
    id("confily.android.wear.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.theme"
}

dependencies {
    implementation(projects.wear.wearResources)

    implementation(compose.ui)
    implementation(compose.materialIconsExtended)
    implementation(libs.bundles.androidx.wear)

    implementation(libs.markdown.renderer)
}
