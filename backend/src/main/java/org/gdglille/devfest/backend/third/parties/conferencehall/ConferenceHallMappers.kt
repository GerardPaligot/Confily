package org.gdglille.devfest.backend.third.parties.conferencehall

import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.talks.TalkDb

fun Category.convertToDb() = CategoryDb(
    id = id,
    name = name,
    color = "default",
    icon = ""
)

fun Talk.convertToDb(categories: List<Category>, formats: List<Format>): TalkDb = TalkDb(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    category = categories.find { it.id == this.categories }?.name
        ?: error("Category not found for ${this.id} talk"),
    format = formats.find { it.id == this.formats }?.name
        ?: error("Format not found for ${this.id} talk"),
    language = this.language,
    speakerIds = this.speakers,
)
