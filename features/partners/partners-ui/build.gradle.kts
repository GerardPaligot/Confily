plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.partners.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.uiModels)
                implementation(projects.features.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.events)
                implementation(projects.style.partners)
                implementation(projects.style.schedules)
                implementation(projects.style.theme)

                implementation(compose.materialIconsExtended)
            }
        }
    }
}
