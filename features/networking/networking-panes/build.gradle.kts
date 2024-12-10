plugins {
    id("confily.panes")
}

android {
    namespace = "com.paligot.confily.networking.panes"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.uiModels)
                implementation(projects.shared.resources)
                implementation(projects.features.networking.networkingUi)
                implementation(projects.features.navigation)
                implementation(projects.style.networking)
                implementation(projects.style.theme)

                implementation(compose.components.resources)

                api(libs.coil3.compose)
            }
        }
    }
}
