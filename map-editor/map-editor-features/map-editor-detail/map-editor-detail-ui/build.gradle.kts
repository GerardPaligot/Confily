plugins {
    id("confily.ui")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.map.editor.detail.ui"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.style.theme)
                implementation(compose.material3)
            }
        }
    }
}
