plugins {
    id("confily.application")
    id("confily.android.application")
    id("confily.quality")
    id("androidx.baselineprofile")
}

android {
    namespace = "com.paligot.confily.android"
    sourceSets.getByName("main").manifest.srcFile("src/androidMain/AndroidManifest.xml")

    buildFeatures {
        buildConfig = true
    }

    dependencies {
        baselineProfile(projects.baselineprofile)
    }
}

baselineProfile {
    automaticGenerationDuringBuild = false
}

kotlin {
    androidTarget()
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
        androidMain.dependencies {
            implementation(projects.widgets.widgetsPresentation)
            implementation(projects.widgets.widgetsStyle)

            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.browser)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.profile)
            implementation(libs.androidx.workmanager.ktx)
            implementation(libs.bundles.androidx.glance)
            implementation(libs.google.material)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.workmanager)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.jetbrains.kotlinx.coroutines.swing)
        }
    }
}
