plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.mapper.detail.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.coreDi)
                implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailPresentation)
            }
        }
    }
}
