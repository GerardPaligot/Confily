@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.schedules.test"
}

dependencies {
    api(projects.features.schedules.schedulesTestScopes)
    api(projects.androidCore.coreTestPatterns)
    api(libs.androidx.espresso.core)
    api(compose.uiTestJUnit4)
}
