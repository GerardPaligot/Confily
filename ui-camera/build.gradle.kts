plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.ui.camera"
}

dependencies {
    implementation(compose.ui)
    implementation(compose.foundation)

    api(libs.google.mlkit.barcode.scanning)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
}
