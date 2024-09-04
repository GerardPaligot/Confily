plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.quality")
}

android {
    namespace = "com.paligot.confily.speakers.test.scopes"
}

kotlin {
    androidTarget()
}
