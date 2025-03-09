plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.map.editor.detail.ui"
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
