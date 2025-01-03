plugins {
    id("confily.ui")
}

android {
    namespace = "com.paligot.confily.speakers.ui"
}

kotlin {
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
