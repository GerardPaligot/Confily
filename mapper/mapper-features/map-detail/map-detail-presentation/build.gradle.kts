plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.mapper.detail.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.coreApi)
                implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailPanes)
                implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailRoutes)
                implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailUiModels)
                implementation(projects.style.components.map.mapUi)
                implementation(libs.jetbrains.compose.material3.adaptive.layout)
                implementation(libs.bundles.jetbrains.kotlinx.io)
            }
        }
    }
}
