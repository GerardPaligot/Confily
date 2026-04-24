plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.map.editor.detail.presentation"
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.coreApi)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailPanes)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailRoutes)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailUiModels)
                implementation(projects.style.components.map.mapUi)
                implementation(libs.jetbrains.compose.material3.adaptive.layout)
                implementation(libs.bundles.jetbrains.kotlinx.io)
            }
        }
    }
}
