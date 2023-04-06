package org.gdglille.devfest.database

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.db.Event
import org.gdglille.devfest.db.EventItem
import org.gdglille.devfest.db.Speaker
import org.gdglille.devfest.db.Talk
import org.gdglille.devfest.models.EventItemList
import org.gdglille.devfest.models.EventV2
import org.gdglille.devfest.models.ScheduleItem

fun org.gdglille.devfest.models.Speaker.convertToModelDb(eventId: String): Speaker = Speaker(
    id = this.id,
    display_name = this.displayName,
    pronouns = this.pronouns,
    bio = this.bio,
    job_title = this.jobTitle,
    company = this.company,
    photo_url = this.photoUrl,
    twitter = this.twitter,
    mastodon = this.mastodon,
    github = this.github,
    linkedin = this.linkedin,
    website = this.website,
    event_id = eventId
)

fun ScheduleItem.convertToModelDb(eventId: String): Talk = Talk(
    id = this.id,
    title = this.talk!!.title,
    start_time = this.startTime,
    end_time = this.endTime,
    room = this.room,
    level = this.talk!!.level,
    abstract_ = this.talk!!.abstract,
    category = this.talk!!.category,
    category_color = this.talk!!.categoryStyle?.color,
    category_icon = this.talk!!.categoryStyle?.icon,
    format = this.talk!!.format,
    open_feedback = this.talk!!.openFeedback,
    open_feedback_url = this.talk!!.openFeedback,
    event_id = eventId
)

fun EventV2.convertToModelDb(): Event = Event(
    id = this.id,
    name = this.name,
    formatted_address = this.address.formatted,
    address = this.address.address,
    latitude = this.address.lat,
    longitude = this.address.lng,
    date = this.startDate.dropLast(1).toLocalDateTime().format(),
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

private fun LocalDateTime.format(): String =
    "${this.dayOfWeek.name} ${this.dayOfMonth}, ${this.month.name} ${this.year}"

fun EventItemList.convertToModelDb(past: Boolean): EventItem = EventItem(
    id = this.id,
    name = this.name,
    date = this.startDate.dropLast(1).toLocalDateTime().format(),
    timestamp = this.startDate.toInstant().toEpochMilliseconds(),
    past = past
)
