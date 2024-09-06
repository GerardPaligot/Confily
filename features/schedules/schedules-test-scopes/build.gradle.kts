plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.schedules.test.scopes"
}

kotlin {
    androidTarget()
}
