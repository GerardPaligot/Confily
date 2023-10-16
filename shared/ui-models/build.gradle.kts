import org.gradle.internal.os.OperatingSystem

plugins {
    id("conferences4hall.multiplatform.library")
    id("conferences4hall.quality")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "org.gdglille.devfest.models.ui"
}

kotlin {
    androidTarget()

    if (OperatingSystem.current().isMacOsX) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.collections)
            }
        }
        val androidMain by getting
        if (OperatingSystem.current().isMacOsX) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
            }
        }
    }
}
