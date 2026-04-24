plugins {
    id("confily.panes")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.infos.panes"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.infos.infosUiModels)
                implementation(projects.features.infos.infosUi)
                implementation(projects.features.socials.socialsUi)
                implementation(projects.features.navigation)
                implementation(projects.style.components.map.mapUi)
                implementation(projects.style.components.markdown)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.events)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)

                implementation(compose.components.resources)

                api(libs.coil3.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.style.components.permissions)
            }
        }
    }
}
