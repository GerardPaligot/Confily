plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.mapper.detail.ui.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.style.components.map.mapUiModels)
    }
}
