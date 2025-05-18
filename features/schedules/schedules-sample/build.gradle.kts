plugins {
    id("confily.sample")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.schedules.sample"

    dependencies {
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
            implementation(projects.features.schedules.schedulesPresentation)
            implementation(projects.features.schedules.schedulesDi)
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.core.coreModelsFactory)
            implementation(projects.core.coreTestPatterns)
            implementation(projects.core.coreTest)
            implementation(projects.features.schedules.schedulesTest)
            implementation(compose.desktop.uiTestJUnit4)
            implementation(libs.settings.test)
        }
    }
}
