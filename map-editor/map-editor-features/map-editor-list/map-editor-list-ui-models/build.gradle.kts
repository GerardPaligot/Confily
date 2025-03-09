plugins {
    id("confily.ui.models")
}

android {
    namespace = "com.paligot.confily.map.editor.list.ui.models"
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.jetbrains.kotlinx.collections)
    }
}
