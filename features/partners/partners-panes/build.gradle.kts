plugins {
    id("confily.panes")
}

android {
    namespace = "com.paligot.confily.partners.panes"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.partners.partnersUiModels)
                implementation(projects.features.partners.partnersUi)
                implementation(projects.features.partners.partnersSemantics)
                implementation(projects.features.navigation)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.events)
                implementation(projects.style.partners)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
    }
}
