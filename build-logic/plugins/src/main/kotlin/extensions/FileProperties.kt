package extensions

import org.gradle.api.Project
import java.io.File
import java.util.*
import kotlin.text.get
import kotlin.times

fun File.toProperties(): Properties {
    val props = Properties()
    if (exists()) {
        props.load(reader())
    }
    return props
}

val Project.appProps: Properties
    get() = rootProject.file("config/app.properties").toProperties()

val Project.versionProps: Properties
    get() = rootProject.file("config/version.properties").toProperties()

val Properties.versionMajor: Int
    get() = get("VERSION_MAJOR")?.toString()?.toInt() ?: 1

val Properties.versionMinor: Int
    get() = get("VERSION_MINOR")?.toString()?.toInt() ?: 0

val Properties.versionPatch: Int
    get() = get("VERSION_PATCH")?.toString()?.toInt() ?: 0

val Properties.versionBuild: Int
    get() = get("VERSION_BUILD")?.toString()?.toInt() ?: 0

val Properties.versionCode: Int
    get() = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10 + versionBuild

val Properties.versionName: String
    get() = "$versionMajor.$versionMinor.$versionPatch"

val Properties.appId: String
    get() = get("APPLICATION_ID")?.toString()
        ?: TODO("Application should be configured in app.properties file")
