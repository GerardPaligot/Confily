plugins {
    id("confily.ui.models")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.speakers.panes.models"
    sourceSets.commonMain.dependencies {
        api(projects.features.speakers.speakersUiModels)
        api(projects.features.schedules.schedulesUiModels)
    }
}
