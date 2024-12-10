plugins {
    id("confily.test")
}

android {
    namespace = "com.paligot.confily.speakers.test"
}

dependencies {
    api(projects.features.speakers.speakersTestScopes)
}
