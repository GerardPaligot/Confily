package org.gdglille.devfest.database

import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.EventV3
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.db.Event
import org.gdglille.devfest.db.EventItem

fun EventV3.convertToModelDb(): Event = Event(
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
