plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.style"
}

dependencies {
    implementation(compose.material3)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)
    implementation(compose.materialIconsExtended)

    implementation(libs.google.accompanist.placeholder)

    implementation(libs.jetbrains.kotlinx.collections)
}
