buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.jetbrains.kotlin.gradlePlugin)
        classpath(libs.jetbrains.kotlin.serialization.gradlePlugin)
        classpath(libs.kmp.nativecoroutines.gradlePlugin)
        classpath(libs.cash.sqldelight.gradlePlugin)
        classpath(libs.google.services.gradlePlugin)
        classpath(libs.google.firebase.crashlytics.gradlePlugin)
        classpath(libs.google.ksp.gradlePlugin)
        classpath(libs.android.gradlePlugin)
        classpath(libs.androidx.benchmark.baseline.profile.gradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}