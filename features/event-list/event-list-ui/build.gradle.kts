plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.events.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.eventList.eventListUiModels)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.theme)
            }
        }
    }
}
