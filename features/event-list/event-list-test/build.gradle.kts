plugins {
    id("confily.test")
}

android {
    namespace = "com.paligot.confily.events.test"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.eventList.eventListSemantics)
        }
    }
}
