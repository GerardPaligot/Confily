import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "com.paligot.confily.models.ui"
}

kotlin {
    androidTarget()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useCommonJs()
        browser()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "UiModels"
            isStatic = false
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.collections)
            }
        }
    }
}
