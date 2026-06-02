plugins {
    id("confily.di")
}

kotlin {
    ((this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("android") as com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget).namespace = "com.paligot.confily.main.di"
    sourceSets.commonMain.dependencies {
        implementation(projects.features.main.main)
        implementation(projects.features.eventList.eventListDi)
        implementation(projects.features.infos.infosDi)
        implementation(projects.features.networking.networkingDi)
        implementation(projects.features.partners.partnersDi)
        implementation(projects.features.schedules.schedulesDi)
        implementation(projects.features.speakers.speakersDi)
        implementation(projects.features.quiz.quizDi)
        implementation(projects.shared.coreDi)
    }
}
