package com.paligot.confily.backend.tags

import com.paligot.confily.models.Tag
import com.paligot.confily.models.inputs.TagInput

fun TagDb.convertToModel() = Tag(
    id = id ?: "",
    name = name
)

fun TagInput.convertToDb(id: String? = null) = TagDb(
    id = id,
    name = name
)
