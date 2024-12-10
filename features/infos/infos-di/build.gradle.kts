plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.infos.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.infos.infosPresentation)
            }
        }
    }
}
