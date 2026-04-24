plugins {
    id("confily.panes")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.schedules.panes"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.schedules.schedulesPanesModels)
                implementation(projects.features.schedules.schedulesUiModels)
                implementation(projects.features.schedules.schedulesUi)
                implementation(projects.features.schedules.schedulesSemantics)
                implementation(projects.features.navigation)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.events)
                implementation(projects.style.schedules)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
    }
}
