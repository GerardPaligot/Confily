plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.map.editor.detail.ui.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.style.components.map.mapUiModels)
    }
}
