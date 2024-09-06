plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.widgets.style"
}

dependencies {
    implementation(projects.style.theme)

    implementation(compose.material3)
    implementation(libs.bundles.androidx.glance)
}
