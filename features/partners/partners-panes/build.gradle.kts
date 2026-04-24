plugins {
    id("confily.panes")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.partners.panes"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.partners.partnersUiModels)
                implementation(projects.features.partners.partnersUi)
                implementation(projects.features.partners.partnersSemantics)
                implementation(projects.features.navigation)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.components.video)
                implementation(projects.style.events)
                implementation(projects.style.partners)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
    }
}
