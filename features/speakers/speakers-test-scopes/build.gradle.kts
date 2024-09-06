plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.speakers.test.scopes"
}

kotlin {
    androidTarget()
}
