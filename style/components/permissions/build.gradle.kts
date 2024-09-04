plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.style.components.permissions"
}

dependencies {
    implementation(projects.shared.resources)
    implementation(projects.style.theme)

    implementation(compose.material3)
    implementation(compose.components.resources)
    implementation(compose.preview)
    debugImplementation(compose.uiTooling)

    implementation(libs.google.accompanist.permissions)
}
