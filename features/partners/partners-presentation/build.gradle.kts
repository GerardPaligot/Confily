plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.partners.presentation"
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.shared.core)
                implementation(projects.shared.resources)
                implementation(projects.features.partners.partnersPanes)
                implementation(projects.features.partners.partnersRoutes)
                implementation(projects.features.partners.partnersUi)
                implementation(projects.features.speakers.speakersRoutes)
                implementation(projects.features.navigation)
                implementation(projects.style.partners)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
    }
}
