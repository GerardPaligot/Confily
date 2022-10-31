plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

dependencies {
    implementation(projects.uiResources)
    implementation(projects.uiCamera)
    implementation(projects.shared)
    implementation(projects.androidData)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.compose.awesome.font)
    implementation(libs.compose.richtext)
    api(libs.compose.openfeedback)

    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.placeholder)
    implementation(libs.accompanist.pager)

    api(libs.coil.compose)
    api(libs.coil.svg)

    implementation(libs.kotlinx.datetime)
}
