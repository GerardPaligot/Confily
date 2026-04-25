plugins {
    id("confily.sample")
    id("confily.quality")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.schedules.sample"
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.schedules.schedulesPresentation)
            implementation(projects.features.schedules.schedulesDi)
        }
        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(projects.core.coreModelsFactory)
            implementation(projects.core.coreTestPatterns)
            implementation(projects.core.coreTest)
            implementation(projects.features.schedules.schedulesTest)
            implementation(compose.desktop.uiTestJUnit4)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
            implementation(libs.jetbrains.kotlinx.datetime)
            implementation(libs.settings.test)
        }
    }
}
