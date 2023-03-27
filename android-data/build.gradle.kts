plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.data"
}

dependencies {
    implementation(projects.shared)
    implementation(projects.uiResources)

    implementation(libs.androidx.lifecycle.vm)

    implementation(platform(libs.google.firebase))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections)

    implementation(libs.zxing)
    implementation(libs.zxing.android) {
        isTransitive = false
    }
}
