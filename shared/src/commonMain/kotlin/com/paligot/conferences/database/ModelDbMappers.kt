package com.paligot.conferences.database

import com.paligot.conferences.db.Event
import com.paligot.conferences.db.Speaker
import com.paligot.conferences.db.Talk
import com.paligot.conferences.models.ScheduleItem
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun com.paligot.conferences.models.Speaker.convertToModelDb(): Speaker = Speaker(
    id = this.id,
    display_name = this.displayName,
    bio = this.bio,
    company = this.company,
    photo_url = this.photoUrl,
    twitter = this.twitter,
    github = this.github
)

fun ScheduleItem.convertToModelDb(): Talk = Talk(
    id = this.id,
    title = this.talk!!.title,
    // date = "Talk from ${this.startTime.toLocalDateTime().formatHoursMinutes()} to ${this.endTime.toLocalDateTime().formatHoursMinutes()}",
    start_time = this.startTime,
    end_time = this.endTime,
    room = this.room,
    level = this.talk!!.level,
    abstract_ = this.talk!!.abstract,
    category = this.talk!!.category,
    format = this.talk!!.format,
    open_feedback = this.talk!!.openFeedback
)

fun com.paligot.conferences.models.Event.convertToModelDb(): Event = Event(
    id = this.id,
    name = this.name,
    address = this.address.address,
    date = getDate(this.startDate, this.endDate),
    twitter = this.twitterUrl?.split("twitter.com/")?.get(1),
    twitter_url = this.twitterUrl,
    linkedin = this.name,
    linkedin_url = this.linkedinUrl,
    faq_url = this.faqLink!!,
    coc_url = this.codeOfConductLink!!,
    updated_at = this.updatedAt
)

fun getDate(from: String, to: String): String {
    val fromDate = Instant.parse(from).toLocalDateTime(TimeZone.UTC)
    val toDate = Instant.parse(to).toLocalDateTime(TimeZone.UTC)
    if (from == to) {
        return fromDate.format()
    }
    return "From ${fromDate.format()} to ${toDate.format()}"
}

fun LocalDateTime.format(): String = "${this.dayOfWeek.name} ${this.dayOfMonth}, ${this.month.name} ${this.year}"
