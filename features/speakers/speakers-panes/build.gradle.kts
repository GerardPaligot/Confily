plugins {
    id("confily.panes")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.speakers.panes"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.speakers.speakersPanesModels)
                implementation(projects.features.speakers.speakersUiModels)
                implementation(projects.features.schedules.schedulesUi)
                implementation(projects.features.speakers.speakersUi)
                implementation(projects.features.speakers.speakersSemantics)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.schedules)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
    }
}
