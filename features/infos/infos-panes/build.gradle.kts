plugins {
    id("confily.panes")
}

android {
    namespace = "com.paligot.confily.infos.panes"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.uiModels)
                implementation(projects.shared.resources)
                implementation(projects.features.infos.infosUi)
                implementation(projects.features.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.events)
                implementation(projects.style.theme)

                implementation(compose.components.resources)

                api(libs.coil3.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.style.components.permissions)
            }
        }
    }
}
