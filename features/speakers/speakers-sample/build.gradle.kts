plugins {
    id("confily.sample")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.sample"

    dependencies {
        debugImplementation(libs.androidx.compose.ui.test.manifest)
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
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.speakers.speakersPresentation)
            implementation(projects.features.speakers.speakersDi)
        }
    }
}
