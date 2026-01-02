package com.paligot.confily.backend.formats.infrastructure.firestore

import com.paligot.confily.models.Format
import com.paligot.confily.models.inputs.FormatInput

fun FormatEntity.convertToModel() = Format(
    id = id ?: "",
    name = name,
    time = time
)

fun FormatInput.convertToEntity(id: String? = null) = FormatEntity(
    id = id,
    name = name,
    time = time
)
