plugins {
    id("confily.presentation")
}

android {
    namespace = "com.paligot.confily.map.editor.list.presentation"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.coreApi)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListPanes)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListRoutes)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListUiModels)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailRoutes)
                implementation(libs.bundles.jetbrains.kotlinx.io)
            }
        }
    }
}
