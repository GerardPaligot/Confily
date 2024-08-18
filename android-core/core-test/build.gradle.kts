plugins {
    id("conferences4hall.android.library")
    id("conferences4hall.quality")
}

android {
    namespace = "org.gdglille.devfest.android.core.test"
}

dependencies {
    api(projects.androidCore.coreSample)
    api(projects.shared.core)
    api(projects.shared.coreDi)
    api(libs.androidx.rules)
    api(libs.androidx.runner)
    api(libs.androidx.workmanager.test)
    api(libs.koin.android)
    api(libs.koin.androidx.workmanager)
    api(libs.koin.test)
    api(libs.koin.test.junit4)
    api(libs.jetbrains.kotlinx.collections)
    api(libs.jetbrains.kotlinx.coroutines.test)
    api(projects.themeM3.schedules.schedulesTest)
}
