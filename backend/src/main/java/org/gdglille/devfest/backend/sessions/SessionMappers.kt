package org.gdglille.devfest.backend.sessions

import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.categories.convertToModel
import org.gdglille.devfest.backend.events.EventDb
import org.gdglille.devfest.backend.events.openFeedbackUrl
import org.gdglille.devfest.backend.formats.FormatDb
import org.gdglille.devfest.backend.speakers.SpeakerDb
import org.gdglille.devfest.backend.speakers.convertToModel
import org.gdglille.devfest.models.Info
import org.gdglille.devfest.models.Session
import org.gdglille.devfest.models.Talk
import org.gdglille.devfest.models.TalkV3
import org.gdglille.devfest.models.inputs.TalkInput

fun SessionDb.convertToModel(eventDb: EventDb): Session {
    return when (this) {
        is TalkDb -> convertToModelTalkSession(eventDb)
        is EventSessionDb -> convertToModelEventSession()
    }
}

fun TalkDb.convertToModelTalkSession(eventDb: EventDb): Session = Session.Talk(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    categoryId = this.category,
    formatId = this.format,
    language = this.language,
    speakers = this.speakerIds,
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    openFeedback = eventDb.openFeedbackUrl()?.let { "$it/$id" } ?: run { null }
)

fun EventSessionDb.convertToModelEventSession(): Session = Session.Event(
    id = id,
    title = title,
    description = description
)

fun EventSessionDb.convertToModelInfo(): Info = Info(
    id = id,
    title = title,
    description = description
)

fun TalkDb.convertToModel(
    speakers: List<SpeakerDb>,
    category: CategoryDb?,
    format: FormatDb?,
    eventDb: EventDb
): Talk = Talk(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    category = category?.name ?: this.category,
    categoryStyle = category?.convertToModel(),
    format = format?.name ?: this.format,
    language = this.language,
    speakers = speakers.map { it.convertToModel() },
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    openFeedback = eventDb.openFeedbackUrl()?.let { "$it/$id" } ?: run { null }
)

fun TalkDb.convertToModel(eventDb: EventDb): TalkV3 = TalkV3(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    categoryId = this.category,
    formatId = this.format,
    language = this.language,
    speakers = this.speakerIds,
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
