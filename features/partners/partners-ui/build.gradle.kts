plugins {
    id("confily.ui")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.partners.ui"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.navigation)
                implementation(projects.features.partners.partnersUiModels)
                implementation(projects.features.socials.socialsUi)
                implementation(projects.style.components.markdown)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.events)
                implementation(projects.style.partners)
                implementation(projects.style.schedules)
                implementation(projects.style.theme)

                implementation(compose.materialIconsExtended)
            }
        }
    }
}
