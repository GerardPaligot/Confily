package com.paligot.confily.backend.sessions

import com.paligot.confily.backend.categories.application.convertToModel
import com.paligot.confily.backend.internals.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.AddressEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.openFeedbackUrl
import com.paligot.confily.backend.speakers.SpeakerDb
import com.paligot.confily.backend.speakers.convertToModel
import com.paligot.confily.models.Session
import com.paligot.confily.models.Talk
import com.paligot.confily.models.TalkV3
import com.paligot.confily.models.inputs.EventSessionInput
import com.paligot.confily.models.inputs.TalkInput

fun SessionDb.convertToModel(eventDb: EventEntity): Session {
    return when (this) {
        is TalkDb -> convertToModelTalkSession(eventDb)
        is EventSessionDb -> convertToModelEventSession()
    }
}

fun TalkDb.convertToModelTalkSession(eventDb: EventEntity): Session = Session.Talk(
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

fun EventSessionInput.convertToDb(
    session: EventSessionDb? = null,
    address: AddressEntity? = null
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
    category: CategoryEntity?,
    format: FormatEntity?,
    eventDb: EventEntity
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

fun TalkDb.convertToModel(eventDb: EventEntity): TalkV3 = TalkV3(
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
