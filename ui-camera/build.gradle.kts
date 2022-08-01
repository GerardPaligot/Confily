plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

dependencies {
    implementation(projects.androidData)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation.layout)

    implementation(libs.google.barcode)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    // Required by AndroidX Camera but another dependency generate a conflict with Guava.
    implementation(libs.google.guava)
}
