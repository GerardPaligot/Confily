plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.widgets.presentation"
}

dependencies {
    implementation(projects.widgets.widgetsUi)
    implementation(projects.widgets.widgetsPanes)
    implementation(projects.shared.core)
    implementation(projects.shared.resources)

    implementation(libs.bundles.androidx.glance)

    implementation(libs.jetbrains.kotlinx.collections)
    implementation(libs.jetbrains.kotlinx.datetime)

    implementation(libs.lyricist)
}
