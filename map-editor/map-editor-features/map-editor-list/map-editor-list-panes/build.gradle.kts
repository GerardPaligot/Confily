plugins {
    id("confily.panes")
}

android {
    namespace = "com.paligot.confily.map.editor.list.panes"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListUi)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListUiModels)
                implementation(projects.style.theme)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(libs.coil3.compose)
            }
        }
    }
}
