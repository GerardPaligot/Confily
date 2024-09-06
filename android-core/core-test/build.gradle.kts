plugins {
    id("confily.android.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.test"
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
}
