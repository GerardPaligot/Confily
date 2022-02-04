plugins {
    id("com.android.library")
    kotlin("android")
}

val composeVersion: String by project
val accompanistVersion: String by project
val coilVersion: String by project
android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil-svg:$coilVersion")
    implementation("com.google.accompanist:accompanist-placeholder-material:$accompanistVersion")
    implementation("br.com.devsrsouza.compose.icons.android:font-awesome:1.0.0")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    // Weird but necessary for the compose preview.
    debugImplementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    debugImplementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    debugImplementation("androidx.savedstate:savedstate-ktx:1.1.0")
    debugImplementation("androidx.core:core-ktx:1.7.0")
}
