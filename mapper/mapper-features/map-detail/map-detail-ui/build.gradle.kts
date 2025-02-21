plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.mapper.detail.ui"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.style.theme)
                implementation(compose.material3)
            }
        }
    }
}
