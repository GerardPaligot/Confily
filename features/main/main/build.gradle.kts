import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.main"
}

kotlin {
    androidTarget()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    sourceSets.commonMain.dependencies {
        api(projects.features.schedules.schedulesUi)
        api(projects.features.speakers.speakersUi)
        api(projects.style.theme)
        implementation(projects.features.schedules.schedulesRoutes)
        implementation(projects.features.eventList.eventListRoutes)
        implementation(projects.features.schedules.schedulesPresentation)
        implementation(projects.features.speakers.speakersPresentation)
        implementation(projects.features.partners.partnersPresentation)
        implementation(projects.features.networking.networkingPresentation)
        implementation(projects.features.infos.infosPresentation)
        implementation(projects.features.eventList.eventListPresentation)
        implementation(projects.features.navigation)
        implementation(projects.style.components.adaptive)
        implementation(projects.shared.core)
        implementation(projects.shared.coreNavigation)
        implementation(projects.shared.uiModels)
        implementation(projects.shared.resources)

        implementation(compose.material3)
        // Can't use compose.material3AdaptiveNavigationSuite because of a bug in the plugin
        // Check https://mvnrepository.com/artifact/org.jetbrains.compose.material3/material3-adaptive-navigation-suite
        implementation("org.jetbrains.compose.material3:material3-adaptive-navigation-suite:1.7.1")
        implementation(compose.components.resources)

        implementation(libs.jetbrains.kotlinx.collections)
        implementation(libs.jetbrains.lifecycle.viewmodel.compose)
        implementation(libs.jetbrains.navigation.compose)
        implementation(libs.bundles.jetbrains.compose.adaptive)

        implementation(libs.koin.compose.viewmodel)
    }
}
