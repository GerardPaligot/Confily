plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.speakers.presentation"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.speakers.speakersPanes)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(libs.lyricist)
            }
        }
    }
}
