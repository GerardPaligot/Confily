plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.partners.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.partners.partnersPanes)
                implementation(projects.features.partners.partnersRoutes)
                implementation(projects.features.partners.partnersUi)
                implementation(projects.features.navigation)
                implementation(projects.style.partners)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
    }
}
