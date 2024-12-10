plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.networking.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.networking.networkingPresentation)
            }
        }
    }
}
