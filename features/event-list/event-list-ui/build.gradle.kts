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
                implementation(projects.shared.uiModels)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.theme)
            }
        }
    }
}
