@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

import org.gradle.api.attributes.Attribute
import org.jetbrains.compose.resources.CopyResourcesToAndroidAssetsTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.android.library.compose")
    id("confily.quality")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.style.events"

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.shared.resources)
                implementation(projects.style.components.placeholder)
                implementation(projects.style.theme)

                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.materialIconsExtended)

                api(libs.jetbrains.kotlinx.collections)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.paligot.confily.style.events"
    generateResClass = always
}

// Workaround for Compose MP 1.10.3 + AGP 9 + com.android.kotlin.multiplatform.library:
// see :shared:resources/build.gradle.kts for the full explanation. Briefly: the plugin's
// `componentSources.assets?.addGeneratedSourceDirectory(...)` no-ops on the KMP Android
// library variant, so the copy task's `outputDirectory` is never set and no `android-assets`
// secondary variant is published for project-to-project consumption.
val composeAndroidAssetsDir = layout.buildDirectory.dir("compose-resources-android-assets")

tasks.matching { it.name == "copyAndroidMainComposeResourcesToAndroidAssets" }.configureEach {
    (this as CopyResourcesToAndroidAssetsTask).outputDirectory.convention(composeAndroidAssetsDir)
}

configurations.named("androidRuntimeElements").configure {
    outgoing.variants.maybeCreate("android-assets-compose").apply {
        attributes {
            attribute(Attribute.of("artifactType", String::class.java), "android-assets")
        }
        artifact(composeAndroidAssetsDir) {
            type = "android-assets"
            builtBy("copyAndroidMainComposeResourcesToAndroidAssets")
        }
    }
}
