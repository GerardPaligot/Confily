plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.schedules.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.schedules.schedulesUiModels)
                implementation(projects.features.speakers.speakersUi)
                implementation(projects.features.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.schedules)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.openfeedback.viewmodel)
            }
        }
    }
}
