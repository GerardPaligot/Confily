import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.kvalue"
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
            baseName = "SharedKValue"
            isStatic = false
            export(libs.settings)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.jetbrains.kotlinx.coroutines)
                api(libs.settings)
                api(libs.settings.coroutines)
            }
        }
    }
}
