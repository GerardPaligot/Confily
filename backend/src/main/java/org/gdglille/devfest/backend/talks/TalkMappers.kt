package org.gdglille.devfest.backend.talks

import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.categories.convertToModel
import org.gdglille.devfest.backend.events.EventDb
import org.gdglille.devfest.backend.events.openFeedbackUrl
import org.gdglille.devfest.models.Speaker
import org.gdglille.devfest.models.Talk
import org.gdglille.devfest.models.inputs.TalkInput

fun TalkDb.convertToModel(
    speakers: List<Speaker>,
    category: CategoryDb?,
    eventDb: EventDb
): Talk = Talk(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    category = category?.name ?: this.category,
    categoryStyle = category?.convertToModel(),
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
