package com.paligot.confily.core.agenda

import com.paligot.confily.db.Event
import com.paligot.confily.db.EventSession
import com.paligot.confily.db.TalkSession
import com.paligot.confily.db.TalkSessionWithSpeakers
import com.paligot.confily.models.Category
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.Format
import com.paligot.confily.models.ScheduleItemV4
import com.paligot.confily.models.Session
import com.paligot.confily.models.Speaker
import kotlin.reflect.KClass
import com.paligot.confily.db.Category as CategoryDb
import com.paligot.confily.db.Format as FormatDb
import com.paligot.confily.db.Session as SessionDb
import com.paligot.confily.db.Speaker as SpeakerDb

fun EventV3.convertToModelDb(): Event = Event(
    id = this.id,
    name = this.name,
    formatted_address = this.address.formatted,
    address = this.address.address,
    latitude = this.address.lat,
    longitude = this.address.lng,
    date = this.startDate,
    start_date = this.startDate,
    end_date = this.endDate,
    coc = this.coc,
    openfeedback_project_id = this.openfeedbackProjectId,
    contact_email = contactEmail,
    contact_phone = contactPhone,
    twitter = this.twitterUrl?.split("twitter.com/")?.get(1),
    twitter_url = this.twitterUrl,
    linkedin = this.name,
    linkedin_url = this.linkedinUrl,
    faq_url = this.faqLink!!,
    coc_url = this.codeOfConductLink!!,
    updated_at = this.updatedAt
)

fun Category.convertToDb(eventId: String): CategoryDb = CategoryDb(
    id = id,
    name = name,
    color = color,
    icon = icon,
    selected = false,
    event_id = eventId
)

fun Format.convertToDb(eventId: String): FormatDb = FormatDb(
    id = id,
    name = name,
    time = time.toLong(),
    selected = false,
    event_id = eventId
)

fun <T : Session> ScheduleItemV4.convertToDb(eventId: String, type: KClass<T>): SessionDb =
    SessionDb(
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

fun Speaker.convertToDb(eventId: String): SpeakerDb = SpeakerDb(
    id = id,
    display_name = displayName,
    pronouns = pronouns,
    bio = bio,
    job_title = jobTitle,
    company = company,
    photo_url = photoUrl,
    twitter = twitter,
    mastodon = mastodon,
    github = github,
    linkedin = linkedin,
    website = website,
    event_id = eventId
)
