plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.ui"
}

dependencies {
    implementation(projects.uiResources)
    implementation(projects.uiCamera)
    implementation(projects.shared)
    implementation(projects.androidData)
    implementation(projects.themeM3.schedules.schedulesUi)
    implementation(projects.themeM3.speakers.speakersUi)
    implementation(projects.themeM3.networking.networkingUi)
    implementation(projects.themeM3.partners.partnersUi)
    implementation(projects.themeM3.navigation)
    implementation(projects.themeM3.style)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.compose.awesome.font)
    implementation(libs.compose.richtext)
    api(libs.compose.openfeedback.m3)

    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.placeholder)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.flowlayout)

    api(libs.coil.compose)
    api(libs.coil.svg)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections)
}
