plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.networking.presentation"
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.networking.networkingPanes)
                implementation(projects.features.networking.networkingRoutes)
                implementation(projects.features.networking.networkingUi)
                implementation(projects.features.navigation)
                implementation(projects.style.networking)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
        androidMain {
            dependencies {
                implementation(projects.style.components.permissions)
            }
        }
    }
}
