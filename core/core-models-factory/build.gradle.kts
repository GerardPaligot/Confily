plugins {
    id("confily.multiplatform.library")
    id("confily.quality")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.core.models.factory"
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            api(projects.shared.models)
            implementation(libs.jetbrains.kotlinx.datetime)
        }
    }
}
