@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.android.library.compose")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.theme.m3.schedules.test"
}

dependencies {
    api(projects.themeM3.schedules.schedulesTestScopes)
    api(projects.androidCore.coreTestPatterns)
    api(libs.androidx.espresso.core)
    api(compose.uiTestJUnit4)
}
