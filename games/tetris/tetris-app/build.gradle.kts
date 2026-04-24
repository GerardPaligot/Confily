import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("confily.application")
    id("confily.quality")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.games.tetris.app"
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.animation)
            implementation(projects.games.tetris.tetrisPresentation)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.paligot.confily.games.tetris.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.paligot.confily.games.tetris"
            packageVersion = "1.0.0"
        }
    }
}
