plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.schedules.presentation"
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.schedules.schedulesPanes)
                implementation(projects.features.schedules.schedulesUi)
                api(projects.features.schedules.schedulesRoutes)
                implementation(projects.features.speakers.speakersRoutes)
                implementation(projects.features.navigation)
                implementation(projects.style.schedules)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.lyricist)
            }
        }
    }
}
