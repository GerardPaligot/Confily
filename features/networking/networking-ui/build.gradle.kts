plugins {
    id("confily.ui")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.networking.ui"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.networking.networkingUiModels)
                implementation(projects.shared.resources)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.style.components.camera)
            }
        }
    }
}
