plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.style"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.androidx.compose.icons)

    implementation(libs.google.accompanist.permissions)
    implementation(libs.google.accompanist.placeholder)

    implementation(libs.jetbrains.kotlinx.collections)

    implementation(libs.font.awesome)
    implementation(libs.richtext.commonmark)
}
