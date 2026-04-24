plugins {
    id("confily.application")
    id("confily.quality")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.app"
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(projects.features.main.main)
            implementation(projects.features.main.mainDi)
            implementation(projects.features.navigation)
            implementation(projects.features.schedules.schedulesRoutes)
            implementation(projects.features.networking.networkingRoutes)
            implementation(projects.shared.core)
            implementation(projects.shared.coreDi)
            implementation(projects.shared.resources)

            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.bundles.jetbrains.compose.adaptive)
            implementation(libs.jetbrains.kotlinx.datetime)

            implementation(libs.settings)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor)
            implementation(libs.coil3.svg)
            implementation(libs.bundles.koin)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
        }
    }
}
