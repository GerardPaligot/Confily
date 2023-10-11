plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.data"
}

dependencies {
    implementation(projects.shared)
}
