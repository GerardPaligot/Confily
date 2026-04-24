plugins {
    id("confily.ui")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.speakers.ui"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.socials.socialsUi)
                implementation(projects.features.speakers.speakersUiModels)
                implementation(projects.style.events)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.components.markdown)
                implementation(projects.style.speakers)
                implementation(projects.style.theme)
            }
        }
    }
}
