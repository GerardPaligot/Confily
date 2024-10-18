import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
    id("kotlinx-serialization")
}

android {
    namespace = "com.paligot.confily.core.fs"
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
            baseName = "SharedFs"
            isStatic = false
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.squareup.okio)
            }
        }
    }
}
