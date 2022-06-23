package extensions

import java.io.File
import java.util.*

fun File.toProperties(): Properties {
    val props = Properties()
    if (exists()) {
        props.load(reader())
    }
    return props
}
