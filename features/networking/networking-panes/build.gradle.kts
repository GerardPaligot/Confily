plugins {
    id("confily.panes")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.networking.panes"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.networking.networkingUiModels)
                implementation(projects.features.networking.networkingUi)
                implementation(projects.features.navigation)
                implementation(projects.style.networking)
                implementation(projects.style.theme)

                implementation(compose.components.resources)

                api(libs.coil3.compose)
            }
        }
    }
}
