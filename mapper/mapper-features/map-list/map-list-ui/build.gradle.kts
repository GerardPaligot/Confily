plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.mapper.list.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
            }
        }
    }
}
