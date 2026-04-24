plugins {
    id("confily.ui")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.events.ui"
    sourceSets {
        val desktopTest by getting

        val commonMain by getting {
            dependencies {
                implementation(projects.features.eventList.eventListUiModels)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.theme)
            }
        }
        desktopTest.dependencies {
            implementation(projects.features.eventList.eventListTest)
        }
    }
}
