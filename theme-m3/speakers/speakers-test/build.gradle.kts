@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.speakers.test"
}

dependencies {
    api(projects.themeM3.speakers.speakersTestScopes)
    api(projects.androidCore.coreTestPatterns)
    api(libs.androidx.espresso.core)
    api(compose.uiTestJUnit4)
}
