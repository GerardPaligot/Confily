plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.navigation"
}

dependencies {
    implementation(projects.uiResources)

    implementation(libs.androidx.annotation)
}
