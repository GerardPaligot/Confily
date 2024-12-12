plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.networking.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.networking.networkingPanes)
                implementation(projects.features.networking.networkingRoutes)
                implementation(projects.features.networking.networkingUi)
                implementation(projects.features.navigation)
                implementation(projects.style.networking)
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
