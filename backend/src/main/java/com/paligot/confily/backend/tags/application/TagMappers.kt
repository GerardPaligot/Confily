package com.paligot.confily.backend.tags.application

import com.paligot.confily.backend.internals.infrastructure.firestore.TagEntity
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
