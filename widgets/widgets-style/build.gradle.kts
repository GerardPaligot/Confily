plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.widgets.style"
}

dependencies {
    implementation(projects.themeM3.style.theme)

    implementation(compose.material3)
    implementation(libs.bundles.androidx.glance)
}
