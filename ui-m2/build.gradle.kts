plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
}

dependencies {
    implementation(projects.uiResources)
    implementation(projects.shared)

    implementation(libs.androidx.compose.material2)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.compose.awesome.font)
    implementation(libs.compose.richtext)
    api(libs.compose.openfeedback)

    implementation(libs.accompanist.placeholder)

    api(libs.coil.compose)
    api(libs.coil.svg)

    implementation(libs.kotlinx.datetime)
}
