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
include(":shared:core")
include(":shared:models")
include(":shared:ui-models")
include(":backend")
include(":benchmark")
include(":theme-m3:main:mobile")
include(":theme-m3:schedules:schedules-ui")
include(":theme-m3:schedules:schedules-feature")
include(":theme-m3:speakers:speakers-ui")
include(":theme-m3:speakers:speakers-screens")
include(":theme-m3:speakers:speakers-feature")
include(":theme-m3:networking:networking-ui")
include(":theme-m3:networking:networking-feature")
include(":theme-m3:partners:partners-ui")
include(":theme-m3:partners:partners-screens")
include(":theme-m3:partners:partners-feature")
include(":theme-m3:infos:infos-ui")
include(":theme-m3:infos:infos-feature")
include(":theme-m3:event-list:event-list-ui")
include(":theme-m3:event-list:event-list-feature")
include(":theme-m3:navigation")
include(":theme-m3:style:partners")
include(":theme-m3:style:schedules")
include(":theme-m3:style:speakers")
include(":theme-m3:style:theme")
include(":ui-camera")
