plugins {
    id("confily.panes")
}

android {
    namespace = "com.paligot.confily.mapper.detail.panes"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailUi)
                implementation(projects.mapper.mapperFeatures.mapDetail.mapDetailUiModels)
                implementation(projects.mapper.mapperFeatures.mapList.mapListUi)
                implementation(projects.style.components.map.mapUi)
                implementation(projects.style.theme)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(libs.coil3.compose)
            }
        }
    }
}
