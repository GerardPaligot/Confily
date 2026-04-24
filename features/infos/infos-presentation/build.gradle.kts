plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.infos.presentation"
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.infos.infosUi)
                implementation(projects.features.infos.infosPanes)
                implementation(projects.features.infos.infosRoutes)
                implementation(projects.features.schedules.schedulesRoutes)
                implementation(projects.features.eventList.eventListRoutes)
                implementation(projects.features.navigation)
                implementation(projects.style.components.markdown)
                implementation(projects.style.events)
                implementation(projects.style.theme)
                implementation(projects.games.tetris.tetrisPresentation)

                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
            }
        }
        androidMain {
            dependencies {
                implementation(projects.style.components.permissions)
            }
        }
    }
}
