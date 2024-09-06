plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
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
