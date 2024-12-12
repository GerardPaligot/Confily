plugins {
    id("confily.android.sample")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.sample"

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
    implementation(projects.features.speakers.speakersPresentation)
    implementation(projects.features.speakers.speakersDi)
    implementation(projects.features.navigation)
    implementation(projects.shared.core)
    implementation(projects.shared.coreDi)
    implementation(projects.shared.resources)
    implementation(projects.style.theme)

    implementation(libs.androidx.appcompat)

    implementation(libs.bundles.jetbrains.compose.adaptive)
    implementation(libs.jetbrains.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.workmanager.ktx)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    androidTestImplementation(projects.features.speakers.speakersTest)
    androidTestImplementation(projects.androidCore.coreTest)
    androidTestImplementation(projects.androidCore.coreModelsFactory)
    androidTestImplementation(libs.jetbrains.kotlinx.datetime)
    constraints {
        implementation("androidx.tracing:tracing:1.2.0") {
            because("AndroidX Test gets force-downgraded to 1.0.0 and breaks otherwise")
        }
    }
}
