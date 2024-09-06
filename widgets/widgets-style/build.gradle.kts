plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.widgets.style"
}

dependencies {
    implementation(projects.style.theme)

    implementation(compose.material3)
    implementation(libs.bundles.androidx.glance)
}
