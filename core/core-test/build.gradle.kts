plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.test"
}

kotlin {
    androidTarget()
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.coreDb)
            implementation(projects.shared.coreDi)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.kotlinx.coroutines.test)
            implementation(libs.settings.test)
            implementation(libs.bundles.koin)
        }
        androidMain.dependencies {
            api(projects.androidCore.coreSample)
            api(libs.androidx.workmanager.test)
            api(libs.koin.androidx.workmanager)
            api(libs.koin.test)
            api(libs.koin.test.junit4)
        }
    }
}
