plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.events.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.eventList.eventListPresentation)
            }
        }
    }
}
