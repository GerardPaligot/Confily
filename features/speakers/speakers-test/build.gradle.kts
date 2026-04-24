plugins {
    id("confily.test")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.speakers.test"
    sourceSets {
        commonMain.dependencies {
            api(projects.features.speakers.speakersTestScopes)
            api(projects.features.speakers.speakersSemantics)
        }
    }
}
