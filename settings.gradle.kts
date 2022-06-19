pluginManagement {
    repositories {
        gradlePluginPortal()
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
include(":shared")
include(":models")
include(":backend")
include(":ui-m3")
include(":ui-m2")
include(":ui-resources")
