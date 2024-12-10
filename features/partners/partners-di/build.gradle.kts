plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.partners.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.partners.partnersPresentation)
            }
        }
    }
}
