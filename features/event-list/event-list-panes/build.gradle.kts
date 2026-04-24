plugins {
    id("confily.panes")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.events.panes"
    sourceSets {
        val desktopTest by getting

        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.features.eventList.eventListUiModels)
                implementation(projects.features.eventList.eventListUi)
                implementation(projects.features.eventList.eventListSemantics)
                implementation(projects.features.navigation)
                implementation(projects.style.theme)

                implementation(compose.components.resources)
            }
        }
        desktopTest.dependencies {
            implementation(projects.features.eventList.eventListTest)
        }
    }
}
