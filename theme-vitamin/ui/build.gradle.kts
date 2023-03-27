plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.vitamin.ui"
}

dependencies {
    implementation(projects.uiResources)
    implementation(projects.uiCamera)
    implementation(projects.shared)
    implementation(projects.androidData)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material2)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.compose.awesome.font)
    implementation(libs.compose.richtext)
    api(libs.compose.openfeedback.m2)

    implementation(libs.vitamin.compose.foundation)
    implementation(libs.vitamin.compose.appbars)
    implementation(libs.vitamin.compose.tabs)
    implementation(libs.vitamin.compose.buttons)
    implementation(libs.vitamin.compose.dividers)
    implementation(libs.vitamin.compose.modals)
    implementation(libs.vitamin.compose.textinputs)
    implementation(libs.vitamin.compose.tags)

    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.placeholder)
    implementation(libs.accompanist.pager)

    api(libs.coil.compose)
    api(libs.coil.svg)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections)
}
