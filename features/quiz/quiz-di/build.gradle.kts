plugins {
    id("confily.di")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.quiz.di"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.features.quiz.quizPresentation)
            }
        }
    }
}
