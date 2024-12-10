plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.schedules.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.schedules.schedulesPresentation)
                implementation(libs.lyricist)
            }
        }
    }
}
