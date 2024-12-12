import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
    id("kotlinx-serialization")
}

android {
    namespace = "com.paligot.confily.navigation"
}

kotlin {
    androidTarget()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                api(projects.shared.coreNavigation)
                implementation(projects.style.theme)
                api(projects.features.schedules.schedulesRoutes)
                api(projects.features.speakers.speakersRoutes)
                api(projects.features.partners.partnersRoutes)
                api(projects.features.networking.networkingRoutes)
                api(projects.features.infos.infosRoutes)
                api(projects.features.eventList.eventListRoutes)

                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)

                implementation(libs.jetbrains.kotlinx.serialization.json)
            }
        }
    }
}
