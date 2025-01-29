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

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SharedFs"
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
                implementation(libs.jetbrains.kotlinx.datetime)
                implementation(libs.squareup.okio)
            }
        }
    }
}
