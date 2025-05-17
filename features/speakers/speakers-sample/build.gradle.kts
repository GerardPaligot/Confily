plugins {
    id("confily.sample")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.sample"

    dependencies {
        debugImplementation(libs.androidx.compose.ui.test.manifest)
        constraints {
            implementation("androidx.tracing:tracing:1.2.0") {
                because("AndroidX Test gets force-downgraded to 1.0.0 and breaks otherwise")
            }
        }
    }
}

kotlin {
    sourceSets {
        val desktopTest by getting
        commonMain.dependencies {
            implementation(projects.features.speakers.speakersPresentation)
            implementation(projects.features.speakers.speakersDi)
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.core.coreModelsFactory)
            implementation(projects.core.coreTestPatterns)
            implementation(projects.core.coreTest)
            implementation(projects.features.speakers.speakersTest)
            implementation(compose.desktop.uiTestJUnit4)
            implementation(libs.settings.test)
        }
    }
}
