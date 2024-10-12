package com.paligot.confily.core.schedules

import com.paligot.confily.db.Category
import com.paligot.confily.db.Format
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.FormatUi

fun CategoryUi.convertToEntity(eventId: String, selected: Boolean) = Category(
    id = id,
    name = name,
    color = color ?: "",
    icon = icon ?: "",
    selected = selected,
    event_id = eventId
)

fun FormatUi.convertToEntity(eventId: String, selected: Boolean) = Format(
    id = id,
    name = name,
    time = time.toLong(),
    selected = selected,
    event_id = eventId
)
