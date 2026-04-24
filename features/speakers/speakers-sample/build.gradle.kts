plugins {
    id("confily.sample")
    id("confily.quality")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.speakers.sample"
    sourceSets {
        val desktopTest by getting
        commonMain.dependencies {
            implementation(projects.features.speakers.speakersPresentation)
            implementation(projects.features.speakers.speakersDi)
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.core.coreModelsFactory)
            implementation(projects.core.coreTestPatterns)
            implementation(projects.core.coreTest)
            implementation(projects.features.speakers.speakersTest)
            implementation(compose.desktop.uiTestJUnit4)
            implementation(libs.settings.test)
        }
    }
}
