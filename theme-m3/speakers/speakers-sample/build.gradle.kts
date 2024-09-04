plugins {
    id("conferences4hall.android.sample")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.speakers.sample"
    defaultConfig {
        testInstrumentationRunner = "com.paligot.confily.core.test.TestJUnitRunner"
    }

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        )
    }
}

dependencies {
    implementation(projects.androidCore.coreSample)
    implementation(projects.themeM3.speakers.speakersFeature)
    implementation(projects.themeM3.speakers.speakersDi)
    implementation(projects.themeM3.navigation)
    implementation(projects.shared.core)
    implementation(projects.shared.coreDi)
    implementation(projects.shared.resources)
    implementation(projects.themeM3.style.theme)

    implementation(libs.androidx.appcompat)

    implementation(platform(libs.androidx.compose.bom))
    implementation(compose.material3)
    implementation(libs.androidx.compose.material3.windowsizeclass)
    implementation(libs.bundles.androidx.compose.adaptive)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.workmanager.ktx)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    androidTestImplementation(projects.themeM3.speakers.speakersTest)
    androidTestImplementation(projects.androidCore.coreTest)
    androidTestImplementation(projects.androidCore.coreModelsFactory)
    androidTestImplementation(libs.jetbrains.kotlinx.datetime)
    constraints {
        implementation("androidx.tracing:tracing:1.2.0") {
            because("AndroidX Test gets force-downgraded to 1.0.0 and breaks otherwise")
        }
    }
}
