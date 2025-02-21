plugins {
    id("confily.panes")
}

android {
    namespace = "com.paligot.confily.mapper.list.panes"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.mapper.mapperFeatures.mapList.mapListUi)
                implementation(projects.mapper.mapperFeatures.mapList.mapListUiModels)
                implementation(projects.style.theme)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(libs.coil3.compose)
            }
        }
    }
}
