package org.gdglille.devfest.backend.formats

import com.paligot.confily.models.Format
import com.paligot.confily.models.inputs.FormatInput

fun FormatDb.convertToModel() = Format(
    id = id ?: "",
    name = name,
    time = time
)

fun FormatInput.convertToDb(id: String? = null) = FormatDb(
    id = id,
    name = name,
    time = time
)
