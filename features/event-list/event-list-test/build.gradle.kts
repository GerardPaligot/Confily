plugins {
    id("confily.test")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.events.test"
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.eventList.eventListSemantics)
        }
    }
}
