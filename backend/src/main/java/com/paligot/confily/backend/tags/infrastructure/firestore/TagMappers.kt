package com.paligot.confily.backend.tags.infrastructure.firestore

import com.paligot.confily.models.Tag
import com.paligot.confily.models.inputs.TagInput

fun TagEntity.convertToModel() = Tag(
    id = id ?: "",
    name = name
)

fun TagInput.convertToEntity(id: String? = null) = TagEntity(
    id = id,
    name = name
)
