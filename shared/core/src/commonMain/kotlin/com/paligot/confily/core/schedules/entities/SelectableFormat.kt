package com.paligot.confily.core.schedules.entities

import com.paligot.confily.models.ui.FormatUi
import kotlin.native.ObjCName

@ObjCName("FormatEntity")
open class Format(val id: String, val name: String, val time: Int)

@ObjCName("SelectableFormatEntity")
class SelectableFormat(
    id: String,
    name: String,
    time: Int,
    val selected: Boolean
) : Format(id, name, time)

fun SelectableFormat.mapToUi() = FormatUi(id = id, name = name, time = time)