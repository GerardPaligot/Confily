package com.paligot.confily.core.schedules

import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.FormatUi

fun CategoryUi.convertToEntity(eventId: String, selected: Boolean) = CategoryDb(
    id = id,
    name = name,
    color = color ?: "",
    icon = icon ?: "",
    selected = selected,
    eventId = eventId
)

fun FormatUi.convertToEntity(eventId: String, selected: Boolean) = FormatDb(
    id = id,
    name = name,
    time = time.toLong(),
    selected = selected,
    eventId = eventId
)
