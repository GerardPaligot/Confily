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
include(":benchmark")
include(":theme-m3:ui")
include(":theme-m3:features")
include(":theme-m3:schedules:schedules-ui")
include(":theme-m3:schedules:schedules-feature")
include(":theme-m3:speakers:speakers-ui")
include(":theme-m3:speakers:speakers-feature")
include(":theme-m3:networking:networking-ui")
include(":theme-m3:networking:networking-feature")
include(":theme-m3:partners:partners-ui")
include(":theme-m3:partners:partners-feature")
include(":theme-m3:navigation")
include(":theme-m3:style")
include(":theme-vitamin:ui")
include(":theme-vitamin:features")
include(":ui-resources")
include(":ui-camera")
