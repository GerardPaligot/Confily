plugins {
    id("confily.di")
}

android {
    namespace = "com.paligot.confily.map.editor.detail.di"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.coreDi)
                implementation(projects.mapEditor.mapEditorFeatures.mapEditorDetail.mapEditorDetailPresentation)
            }
        }
    }
}
