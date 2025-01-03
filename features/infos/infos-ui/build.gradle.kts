plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.infos.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.infos.infosUiModels)
                implementation(projects.shared.resources)
                implementation(projects.features.navigation)
                implementation(projects.features.socials.socialsUi)
                implementation(projects.style.components.markdown)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.events)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)

                implementation(libs.coil3.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.uiCamera)
            }
        }
    }
}
