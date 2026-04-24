plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.events.presentation"
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.eventList.eventListUi)
                implementation(projects.features.eventList.eventListPanes)
                implementation(projects.features.eventList.eventListRoutes)
                implementation(projects.features.schedules.schedulesRoutes)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(libs.jetbrains.kotlinx.datetime)
            }
        }
    }
}
