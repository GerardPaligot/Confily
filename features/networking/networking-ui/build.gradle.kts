plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.networking.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.networking.networkingUiModels)
                implementation(projects.shared.resources)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.style.components.camera)
            }
        }
    }
}
