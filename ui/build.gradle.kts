plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.compose") version "1.0.1"
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("br.com.devsrsouza.compose.icons.android:font-awesome:1.0.0")
    implementation(compose.material)
    implementation(compose.materialIconsExtended)
    implementation(compose.uiTooling)
}
