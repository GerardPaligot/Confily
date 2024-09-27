package com.paligot.confily.core.database.mappers

import com.paligot.confily.db.EventSession
import com.paligot.confily.db.TalkSession
import com.paligot.confily.db.TalkSessionWithSpeakers
import com.paligot.confily.models.ScheduleItemV4
import com.paligot.confily.models.Session
import kotlin.reflect.KClass
import com.paligot.confily.db.Session as SessionDb

fun <T : Session> ScheduleItemV4.convertToDb(eventId: String, type: KClass<T>): SessionDb = SessionDb(
    id = this.id,
    order_ = order.toLong(),
    room = this.room,
    date = this.date,
    start_time = this.startTime,
    end_time = this.endTime,
    session_talk_id = if (type == Session.Talk::class) sessionId else null,
    session_event_id = if (type == Session.Event::class) sessionId else null,
    event_id = eventId,
    is_favorite = false
)

fun Session.Talk.convertToDb(eventId: String): TalkSession = TalkSession(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract_ = this.abstract,
    language = this.language,
    slide_url = this.linkSlides,
    replay_url = this.linkReplay,
    category_id = this.categoryId,
    format_id = this.formatId,
    open_feedback_url = this.openFeedback,
    event_id = eventId
)

fun Session.Talk.convertToDb(eventId: String, speakerId: String) = TalkSessionWithSpeakers(
    id = 0L,
    speaker_id = speakerId,
    talk_id = id,
    event_id = eventId
)

fun Session.Event.convertToDb(eventId: String): EventSession = EventSession(
    id = this.id,
    title = this.title,
    description = this.description,
    formatted_address = address?.formatted,
    address = address?.address,
    latitude = address?.lat,
    longitude = address?.lng,
    event_id = eventId
)
