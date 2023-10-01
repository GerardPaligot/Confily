package org.gdglille.devfest.backend.third.parties.conferencehall

import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.formats.FormatDb
import org.gdglille.devfest.backend.talks.TalkDb
import org.gdglille.devfest.models.inputs.third.parties.conferencehall.CategoryInput
import org.gdglille.devfest.models.inputs.third.parties.conferencehall.FormatInput

fun Category.convertToDb(categories: List<CategoryInput>): CategoryDb {
    val category = categories.find { it.id == id }
    return CategoryDb(
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
    speakerIds = this.speakers,
)
