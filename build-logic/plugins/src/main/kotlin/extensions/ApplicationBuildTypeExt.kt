package extensions

import com.android.build.api.dsl.ApplicationBuildType
import java.util.*

fun ApplicationBuildType.stringBuildConfigField(key: String, properties: Properties) =
    stringBuildConfigField(key, "\"${properties.getProperty(key)}\"")

fun ApplicationBuildType.stringBuildConfigField(key: String, value: String) =
    buildConfigField("String", key, value)