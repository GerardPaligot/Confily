plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.models.factory"
}

kotlin {
    androidTarget()
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(projects.shared.models)
            implementation(libs.jetbrains.kotlinx.datetime)
        }
    }
}
