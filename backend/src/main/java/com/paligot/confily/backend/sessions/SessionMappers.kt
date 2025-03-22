package com.paligot.confily.backend.sessions

import com.paligot.confily.backend.categories.CategoryDb
import com.paligot.confily.backend.categories.convertToModel
import com.paligot.confily.backend.events.AddressDb
import com.paligot.confily.backend.events.EventDb
import com.paligot.confily.backend.events.convertToModel
import com.paligot.confily.backend.events.openFeedbackUrl
import com.paligot.confily.backend.formats.FormatDb
import com.paligot.confily.backend.speakers.SpeakerDb
import com.paligot.confily.backend.speakers.convertToModel
import com.paligot.confily.models.Info
import com.paligot.confily.models.Session
import com.paligot.confily.models.Talk
import com.paligot.confily.models.TalkV3
import com.paligot.confily.models.inputs.EventSessionInput
import com.paligot.confily.models.inputs.TalkInput

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
    tagIds = this.tags,
    language = this.language,
    speakers = this.speakerIds,
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    openFeedback = eventDb.openFeedbackUrl()?.let { "$it/$id" } ?: run { null }
)

fun EventSessionDb.convertToModelEventSession(): Session = Session.Event(
    id = id,
    title = title,
    description = description,
    address = address?.convertToModel()
)

fun EventSessionDb.convertToModelInfo(): Info = Info(
    id = id,
    title = title,
    description = description
)

fun EventSessionInput.convertToDb(
    session: EventSessionDb? = null,
    address: AddressDb? = null
): EventSessionDb = EventSessionDb(
    id = session?.id ?: "",
    title = if (title != null && title != session?.title) title!! else session?.title ?: "",
    description = if (description != null && description != session?.description) {
        description!!
    } else {
        session?.description ?: ""
    },
    address = address ?: session?.address
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
    tags = this.tags,
    language = this.language,
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    speakerIds = this.speakerIds
)
