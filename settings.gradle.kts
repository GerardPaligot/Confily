pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.google.cloud.tools.appengine")) {
                useModule("com.google.cloud.tools:appengine-gradle-plugin:${requested.version}")
            }
        }
    }
}

plugins {
    // This plugin is used because we can't configure a jvm toolchain version that isn't installed
    // on the local machine without this plugin. e.g. JVM 21 installed but JVM toolchain 17
    // configured in the project.
    // https://youtrack.jetbrains.com/issue/KTIJ-24981/Gradle-8.-project-sync-fails-with-an-error-No-matching-toolchains-found-for-requested-specification-if-there-is-no-necessary-JDK
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "conferences4hall"
include(":androidApp")
include(":android-core:core-models-factory")
include(":android-core:core-sample")
include(":android-core:core-test")
include(":android-core:core-test-patterns")
include(":features:speakers:speakers-ui")
include(":features:speakers:speakers-sample")
include(":features:speakers:speakers-panes")
include(":features:speakers:speakers-presentation")
include(":features:speakers:speakers-semantics")
include(":features:speakers:speakers-di")
include(":features:speakers:speakers-test")
include(":features:speakers:speakers-test-scopes")
include(":shared:core")
include(":shared:core-di")
include(":shared:models")
include(":shared:resources")
include(":shared:ui-models")
include(":backend")
include(":theme-m3:main:main")
include(":theme-m3:main:main-di")
include(":theme-m3:schedules:schedules-ui")
include(":theme-m3:schedules:schedules-sample")
include(":theme-m3:schedules:schedules-panes")
include(":theme-m3:schedules:schedules-presentation")
include(":theme-m3:schedules:schedules-di")
include(":theme-m3:schedules:schedules-test")
include(":theme-m3:schedules:schedules-test-scopes")
include(":theme-m3:schedules:schedules-semantics")
include(":theme-m3:networking:networking-ui")
include(":theme-m3:networking:networking-screens")
include(":theme-m3:networking:networking-feature")
include(":theme-m3:networking:networking-di")
include(":theme-m3:partners:partners-ui")
include(":theme-m3:partners:partners-screens")
include(":theme-m3:partners:partners-feature")
include(":theme-m3:partners:partners-semantics")
include(":theme-m3:partners:partners-di")
include(":theme-m3:infos:infos-ui")
include(":theme-m3:infos:infos-feature")
include(":theme-m3:infos:infos-screens")
include(":theme-m3:infos:infos-di")
include(":theme-m3:event-list:event-list-ui")
include(":theme-m3:event-list:event-list-feature")
include(":theme-m3:event-list:event-list-screens")
include(":theme-m3:event-list:event-list-di")
include(":theme-m3:navigation")
include(":style:components:adaptive")
include(":style:components:markdown")
include(":style:components:permissions")
include(":style:components:placeholder")
include(":style:events")
include(":style:networking")
include(":style:partners")
include(":style:schedules")
include(":style:speakers")
include(":style:theme")
include(":ui-camera")
include(":webApps:speakerApp")
include(":widgets:widgets-feature")
include(":widgets:widgets-screens")
include(":widgets:widgets-style")
include(":widgets:widgets-ui")
include(":baselineprofile")
