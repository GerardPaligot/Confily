plugins {
    id("confily.test")
}

android {
    namespace = "com.paligot.confily.speakers.test"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.speakers.speakersTestScopes)
            api(projects.features.speakers.speakersSemantics)
        }
    }
}
