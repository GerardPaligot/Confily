package com.paligot.confily.core.agenda

import com.paligot.confily.core.events.EventDb
import com.paligot.confily.core.events.FeaturesActivatedDb
import com.paligot.confily.core.events.MenuDb
import com.paligot.confily.core.events.QAndAActionDb
import com.paligot.confily.core.events.QAndADb
import com.paligot.confily.core.extensions.format
import com.paligot.confily.core.partners.JobDb
import com.paligot.confily.core.partners.PartnerDb
import com.paligot.confily.core.partners.PartnerSocialDb
import com.paligot.confily.core.schedules.CategoryDb
import com.paligot.confily.core.schedules.EventSessionDb
import com.paligot.confily.core.schedules.FormatDb
import com.paligot.confily.core.schedules.SessionDb
import com.paligot.confily.core.schedules.TalkSessionDb
import com.paligot.confily.core.schedules.TalkSessionWithSpeakers
import com.paligot.confily.core.speakers.SpeakerDb
import com.paligot.confily.models.Category
import com.paligot.confily.models.EventLunchMenu
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.FeaturesActivated
import com.paligot.confily.models.Format
import com.paligot.confily.models.Job
import com.paligot.confily.models.PartnerV3
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.QuestionAndResponseAction
import com.paligot.confily.models.ScheduleItemV4
import com.paligot.confily.models.Session
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.Speaker
import kotlinx.datetime.toLocalDateTime
import kotlin.reflect.KClass

fun EventV3.convertToModelDb(): EventDb = EventDb(
    id = this.id,
    name = this.name,
    formattedAddress = this.address.formatted,
    address = this.address.address,
    latitude = this.address.lat,
    longitude = this.address.lng,
    date = this.startDate.dropLast(1).toLocalDateTime().format(),
    coc = this.coc,
    openfeedbackProjectId = this.openfeedbackProjectId,
    contactEmail = contactEmail,
    contactPhone = contactPhone,
    twitter = this.twitterUrl?.split("twitter.com/")?.get(1),
    twitterUrl = this.twitterUrl,
    linkedin = this.name,
    linkedinUrl = this.linkedinUrl,
    faqUrl = this.faqLink!!,
    cocUrl = this.codeOfConductLink!!,
    updatedAt = this.updatedAt
)

fun QuestionAndResponse.convertToModelDb(eventId: String) = QAndADb(
    order = order.toLong(),
    eventId = id,
    question = question,
    response = response
)

fun QuestionAndResponseAction.convertToModelDb(eventId: String, qandaId: String) = QAndAActionDb(
    id = "$qandaId-$order",
    order = order.toLong(),
    eventId = eventId,
    qandaId = order.toLong(),
    label = label,
    url = url
)

fun EventLunchMenu.convertToModelDb(eventId: String) = MenuDb(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert,
    eventId = eventId
)

fun FeaturesActivated.convertToModelDb(eventId: String) = FeaturesActivatedDb(
    eventId = eventId,
    hasNetworking = hasNetworking,
    speakerList = hasSpeakerList,
    hasPartnerList = hasPartnerList,
    hasMenus = hasMenus,
    hasQanda = hasQAndA,
    hasBilletWebTicket = hasBilletWebTicket
)

fun Category.convertToDb(eventId: String): CategoryDb = CategoryDb(
    id = id,
    name = name,
    color = color,
    icon = icon,
    selected = false,
    eventId = eventId
)

fun Format.convertToDb(eventId: String): FormatDb = FormatDb(
    id = id,
    name = name,
    time = time.toLong(),
    selected = false,
    eventId = eventId
)

fun <T : Session> ScheduleItemV4.convertToDb(eventId: String, type: KClass<T>): SessionDb =
    SessionDb(
        id = this.id,
        order = order.toLong(),
        room = this.room,
        date = this.date,
        startTime = this.startTime,
        endTime = this.endTime,
        sessionTalkId = if (type == Session.Talk::class) sessionId else null,
        sessionEventId = if (type == Session.Event::class) sessionId else null,
        eventId = eventId,
        isFavorite = false
    )

fun Session.Talk.convertToDb(eventId: String): TalkSessionDb = TalkSessionDb(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    language = this.language,
    slideUrl = this.linkSlides,
    replayUrl = this.linkReplay,
    categoryId = this.categoryId,
    formatId = this.formatId,
    openFeedbackUrl = this.openFeedback,
    eventId = eventId
)

fun Session.Talk.convertToDb(eventId: String, id: String, speakerId: String) = TalkSessionWithSpeakers(
    id = id,
    speakerId = speakerId,
    talkId = this.id,
    eventId = eventId
)

fun Session.Event.convertToDb(eventId: String): EventSessionDb = EventSessionDb(
    id = this.id,
    title = this.title,
    description = this.description,
    formattedAddress = address?.formatted,
    address = address?.address,
    latitude = address?.lat,
    longitude = address?.lng,
    eventId = eventId
)

fun Speaker.convertToDb(eventId: String): SpeakerDb = SpeakerDb(
    id = id,
    displayName = displayName,
    pronouns = pronouns,
    bio = bio,
    jobTitle = jobTitle,
    company = company,
    photoUrl = photoUrl,
    twitter = twitter,
    mastodon = mastodon,
    github = github,
    linkedin = linkedin,
    website = website,
    eventId = eventId
)

fun PartnerV3.convertToDb(
    eventId: String,
    type: String,
    hasSvgSupport: Boolean
): PartnerDb = PartnerDb(
    id = id,
    name = name,
    description = description,
    eventId = eventId,
    type = type,
    logoUrl = if (hasSvgSupport) {
        media.svg
    } else if (media.pngs != null) {
        media.pngs!!._250
    } else {
        media.svg
    },
    formattedAddress = address?.formatted,
    address = address?.address,
    latitude = address?.lat,
    longitude = address?.lng
)

fun SocialItem.convertToDb(eventId: String, partnerId: String): PartnerSocialDb = PartnerSocialDb(
    url = url,
    type = type.name,
    partnerId = partnerId,
    eventId = eventId
)

fun Job.convertToDb(eventId: String, partnerId: String): JobDb = JobDb(
    url = url,
    partnerId = partnerId,
    eventId = eventId,
    title = title,
    companyName = companyName,
    location = location,
    salaryMax = salary?.max?.toLong(),
    salaryMin = salary?.min?.toLong(),
    salaryRecurrence = salary?.recurrence,
    requirements = requirements,
    publishDate = publishDate,
    propulsed = propulsed
)
