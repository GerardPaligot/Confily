plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.map.editor.list.presentation"
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
