plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.infos.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.infos.infosUi)
                implementation(projects.features.infos.infosPanes)
                implementation(projects.features.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.events)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
        androidMain {
            dependencies {
                implementation(projects.style.components.permissions)
            }
        }
    }
}
