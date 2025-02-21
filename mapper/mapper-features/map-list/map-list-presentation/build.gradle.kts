plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.mapper.list.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.coreApi)
                implementation(projects.mapper.mapperFeatures.mapList.mapListPanes)
                implementation(projects.mapper.mapperFeatures.mapList.mapListRoutes)
                implementation(projects.mapper.mapperFeatures.mapList.mapListUiModels)
                implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailRoutes)
                implementation(libs.bundles.jetbrains.kotlinx.io)
            }
        }
    }
}
