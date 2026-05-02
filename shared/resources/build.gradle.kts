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
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.resources"

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "Resources"
            isStatic = false
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.components.resources)

                implementation(libs.lyricist)
            }
        }
    }
}

// Workaround for Compose MP 1.10.3 + AGP 9 + com.android.kotlin.multiplatform.library:
// AndroidResources.kt in the Compose plugin calls
//   componentSources.assets?.addGeneratedSourceDirectory(copyTask, ::outputDirectory)
// but for the KMP Android library variant, `componentSources.assets` is null. The safe-call
// silently skips the registration, leaving (a) the copy task's `outputDirectory` unset and
// (b) no `android-assets` secondary variant published on `androidRuntimeElements`. Both
// must be patched for downstream Android apps to package the generated `.cvr` files.
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
