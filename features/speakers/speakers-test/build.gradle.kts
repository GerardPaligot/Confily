@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("confily.android.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.test"
}

dependencies {
    api(projects.features.speakers.speakersTestScopes)
    api(projects.androidCore.coreTestPatterns)
    api(libs.androidx.espresso.core)
    api(compose.uiTestJUnit4)
}
