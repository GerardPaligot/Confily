plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.events.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.eventList.eventListUi)
                implementation(projects.features.eventList.eventListPanes)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(libs.jetbrains.kotlinx.datetime)
            }
        }
    }
}
