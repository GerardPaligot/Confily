package com.paligot.confily.core.schedules.entities

import com.paligot.confily.schedules.ui.models.TagUi
import kotlin.native.ObjCName

@ObjCName("TagEntity")
open class Tag(
    val id: String,
    val name: String
)

fun Tag.mapToTagUi() = TagUi(id = id, name = name)
