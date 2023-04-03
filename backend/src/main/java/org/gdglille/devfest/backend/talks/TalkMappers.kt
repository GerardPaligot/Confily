package org.gdglille.devfest.backend.talks

import org.gdglille.devfest.backend.events.EventDb
import org.gdglille.devfest.backend.events.openFeedbackUrl
import org.gdglille.devfest.backend.internals.network.conferencehall.Format
import org.gdglille.devfest.models.Category
import org.gdglille.devfest.models.Speaker
import org.gdglille.devfest.models.Talk
import org.gdglille.devfest.models.inputs.TalkInput

fun org.gdglille.devfest.backend.internals.network.conferencehall.Talk.convertToDb(
    categories: List<org.gdglille.devfest.backend.internals.network.conferencehall.Category>,
    formats: List<Format>
): TalkDb = TalkDb(
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

fun TalkDb.convertToModel(speakers: List<Speaker>, eventDb: EventDb): Talk = Talk(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    category = this.category,
    categoryStyle = eventDb.categories.find { it.name == this.category }?.let {
        Category(name = it.name, color = it.color, icon = it.icon)
    },
    format = this.format,
    language = this.language,
    speakers = speakers,
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    openFeedback = eventDb.openFeedbackUrl()?.let { "$it/$id" } ?: run { null }
)

fun TalkInput.convertToDb(id: String? = null): TalkDb = TalkDb(
    id = id ?: "",
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    category = this.category,
    format = this.format,
    language = this.language,
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    speakerIds = this.speakerIds
)
