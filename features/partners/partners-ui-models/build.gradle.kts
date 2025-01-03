plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.partners.ui.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.features.socials.socialsUiModels)
    }
}
