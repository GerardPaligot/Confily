plugins {
    id("confily.panes")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.map.editor.detail.panes"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailUi)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailUiModels)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorList.mapEditorListUi)
                implementation(projects.style.components.map.mapUi)
                implementation(projects.style.theme)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(libs.coil3.compose)
            }
        }
    }
}
