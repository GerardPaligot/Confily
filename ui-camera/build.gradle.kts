plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.ui.camera"
}

dependencies {
    implementation(projects.androidData)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation.layout)

    implementation(libs.google.barcode)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    // Required by AndroidX Camera but another dependency generate a conflict with Guava.
    implementation(libs.google.guava)
}
