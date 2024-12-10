plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.speakers.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.speakers.speakersPresentation)
                implementation(libs.lyricist)
            }
        }
    }
}
