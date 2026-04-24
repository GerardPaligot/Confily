plugins {
    id("confily.ui")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.socials.ui"
    sourceSets.commonMain.dependencies {
        api(projects.features.socials.socialsUiModels)
        implementation(projects.style.events)
        implementation(projects.style.theme)

        implementation(compose.components.resources)
        implementation(compose.materialIconsExtended)
    }
}
