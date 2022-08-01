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

rootProject.name = "conferences4hall"
include(":androidApp")
include(":android-data")
include(":shared")
include(":models")
include(":backend")
include(":ui-m3")
include(":ui-m2")
include(":benchmark")
include(":theme-vitamin:ui")
include(":theme-vitamin:features")
include(":ui-resources")
include(":ui-camera")
