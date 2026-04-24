plugins {
    id("confily.test")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.schedules.test"
    sourceSets {
        commonMain.dependencies {
            api(projects.features.schedules.schedulesTestScopes)
        }
    }
}
