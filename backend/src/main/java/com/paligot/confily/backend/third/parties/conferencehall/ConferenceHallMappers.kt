package com.paligot.confily.backend.third.parties.conferencehall

import com.paligot.confily.backend.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.formats.FormatDb
import com.paligot.confily.backend.sessions.TalkDb
import com.paligot.confily.models.inputs.conferencehall.CategoryInput
import com.paligot.confily.models.inputs.conferencehall.FormatInput

fun Category.convertToDb(categories: List<CategoryInput>): CategoryEntity {
    val category = categories.find { it.id == id }
    return CategoryEntity(
        id = id,
        name = name,
        color = category?.color ?: "default",
        icon = category?.icon ?: ""
    )
}

fun Format.convertToDb(formats: List<FormatInput>) = FormatDb(
    id = id,
    name = name,
    time = formats.find { it.id == this.id }?.time ?: 0
)

fun Talk.convertToDb(): TalkDb = TalkDb(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    category = this.categories,
    format = this.formats,
    language = this.language,
    speakerIds = this.speakers
)
