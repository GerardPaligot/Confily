plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.schedules.panes.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.features.schedules.schedulesUiModels)
        api(projects.features.speakers.speakersUiModels)
    }
}
