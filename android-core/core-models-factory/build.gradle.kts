plugins {
    id("confily.android.library")
    id("confily.quality")
}

android {
    namespace = "com.paligot.confily.core.models.factory"
}

dependencies {
    api(projects.shared.models)
    implementation(libs.jetbrains.kotlinx.datetime)
}
