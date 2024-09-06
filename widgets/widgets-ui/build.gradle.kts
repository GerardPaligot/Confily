plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
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
