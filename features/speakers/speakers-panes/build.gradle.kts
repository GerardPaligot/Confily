plugins {
    id("confily.panes")
}

android {
    namespace = "com.paligot.confily.speakers.panes"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.uiModels)
                implementation(projects.shared.resources)
                implementation(projects.features.schedules.schedulesUi)
                implementation(projects.features.speakers.speakersUi)
                implementation(projects.features.speakers.speakersSemantics)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.schedules)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
    }
}
