plugins {
    id("confily.presentation")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.games.tetris.presentation"
    sourceSets {
        commonMain {
            dependencies {
                api(projects.games.tetris.tetrisUi)
            }
        }
    }
}
