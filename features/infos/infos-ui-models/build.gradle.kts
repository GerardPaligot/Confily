plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.infos.ui.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.features.socials.socialsUiModels)
        api(projects.style.components.map.mapUiModels)
    }
}
