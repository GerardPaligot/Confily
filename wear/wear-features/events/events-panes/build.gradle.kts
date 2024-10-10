plugins {
    id("confily.android.wear.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.wear.events.panes"
}

dependencies {
    implementation(projects.shared.uiModels)
    implementation(projects.wear.wearResources)

    implementation(compose.ui)
    implementation(libs.bundles.androidx.wear)

    api(libs.jetbrains.kotlinx.collections)
}
