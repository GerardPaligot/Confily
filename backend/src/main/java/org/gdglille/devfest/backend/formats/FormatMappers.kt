package org.gdglille.devfest.backend.formats

import org.gdglille.devfest.models.Format
import org.gdglille.devfest.models.inputs.FormatInput

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
