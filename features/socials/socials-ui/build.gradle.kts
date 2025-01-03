plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.socials.ui"
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.features.socials.socialsUiModels)
        implementation(projects.style.events)
        implementation(projects.style.theme)

        implementation(compose.components.resources)
        implementation(compose.materialIconsExtended)
    }
}
