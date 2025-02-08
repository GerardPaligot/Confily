plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.test.patterns"
}

kotlin {
    androidTarget()
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(compose.desktop.uiTestJUnit4)
        }
    }
}
