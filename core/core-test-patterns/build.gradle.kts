plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.core.test.patterns"
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(compose.desktop.uiTestJUnit4)
        }
    }
}
