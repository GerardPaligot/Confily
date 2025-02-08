plugins {
    id("confily.test")
}

android {
    namespace = "com.paligot.confily.schedules.test"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.features.schedules.schedulesTestScopes)
        }
    }
}
