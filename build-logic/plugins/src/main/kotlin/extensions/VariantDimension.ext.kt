package extensions

import com.android.build.api.dsl.VariantDimension
import java.util.Properties

fun VariantDimension.stringBuildConfigField(key: String, properties: Properties) =
    stringBuildConfigField(key, "\"${properties.getProperty(key)}\"")

fun VariantDimension.stringBuildConfigField(key: String, value: String) =
    buildConfigField("String", key, value)
