plugins {
    id("confily.android.wear.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.speakers.panes"
}

dependencies {
    implementation(projects.features.speakers.speakersPanesModels)
    implementation(projects.wear.wearFeatures.schedules.schedulesUi)
    implementation(projects.wear.wearResources)
    implementation(projects.wear.wearTheme)

    implementation(compose.ui)
    implementation(libs.bundles.androidx.wear)

    implementation(libs.coil3.compose)

    api(libs.jetbrains.kotlinx.collections)
}
