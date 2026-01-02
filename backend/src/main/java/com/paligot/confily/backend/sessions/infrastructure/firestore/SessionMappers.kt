
package com.paligot.confily.backend.sessions.infrastructure.firestore

import com.paligot.confily.backend.addresses.infrastructure.firestore.AddressEntity
import com.paligot.confily.backend.addresses.infrastructure.firestore.convertToModel
import com.paligot.confily.backend.categories.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.categories.infrastructure.firestore.convertToModel
import com.paligot.confily.backend.events.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.events.infrastructure.firestore.openFeedbackUrl
import com.paligot.confily.backend.formats.infrastructure.firestore.FormatEntity
import com.paligot.confily.backend.speakers.application.convertToModel
import com.paligot.confily.backend.speakers.infrastructure.firestore.SpeakerEntity
import com.paligot.confily.models.Session
import com.paligot.confily.models.Talk
import com.paligot.confily.models.TalkV3
import com.paligot.confily.models.inputs.EventSessionInput
import com.paligot.confily.models.inputs.TalkSessionInput

fun SessionEntity.convertToModel(eventDb: EventEntity): Session {
    return when (this) {
        is TalkSessionEntity -> convertToModelTalkSession(eventDb)
        is EventSessionEntity -> convertToModelEventSession()
    }
}

fun TalkSessionEntity.convertToModelTalkSession(eventDb: EventEntity): Session = Session.Talk(
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

fun EventSessionEntity.convertToModelEventSession(): Session = Session.Event(
    id = id,
    title = title,
    description = description,
    address = address?.convertToModel()
)

fun EventSessionInput.convertToEntity(
    session: EventSessionEntity? = null,
    address: AddressEntity? = null
): EventSessionEntity = EventSessionEntity(
    id = session?.id ?: "",
    title = if (title != null && title != session?.title) title!! else session?.title ?: "",
    description = if (description != null && description != session?.description) {
        description!!
    } else {
        session?.description ?: ""
    },
    address = address ?: session?.address
)

fun TalkSessionEntity.convertToModel(
    speakers: List<SpeakerEntity>,
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

fun TalkSessionEntity.convertToModel(eventDb: EventEntity): TalkV3 = TalkV3(
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

fun TalkSessionInput.convertToEntity(id: String? = null): TalkSessionEntity = TalkSessionEntity(
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
