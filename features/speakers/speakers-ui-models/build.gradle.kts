plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.speakers.ui.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.features.socials.socialsUiModels)
    }
}
