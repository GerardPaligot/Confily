plugins {
    id("confily.test")
}

android {
    namespace = "com.paligot.confily.schedules.test"
}

dependencies {
    api(projects.features.schedules.schedulesTestScopes)
}
