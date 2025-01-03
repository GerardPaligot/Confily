plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.speakers.panes.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.features.speakers.speakersUiModels)
        api(projects.features.schedules.schedulesUiModels)
    }
}
