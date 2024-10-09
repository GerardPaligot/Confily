plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.schedules.ui"
}

dependencies {
    implementation(projects.wear.wearTheme)

    implementation(compose.ui)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(libs.bundles.androidx.wear)
}
