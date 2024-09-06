plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.widgets.ui"
}

dependencies {
    implementation(projects.shared.core)

    implementation(libs.bundles.androidx.glance)

    implementation(libs.jetbrains.kotlinx.collections)
    implementation(libs.jetbrains.kotlinx.datetime)
}
