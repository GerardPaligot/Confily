plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.speakers.presentation"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.speakers.speakersPanes)
                implementation(projects.features.speakers.speakersSemantics)
                api(projects.features.speakers.speakersRoutes)
                implementation(projects.features.schedules.schedulesRoutes)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(libs.lyricist)
            }
        }
    }
}
