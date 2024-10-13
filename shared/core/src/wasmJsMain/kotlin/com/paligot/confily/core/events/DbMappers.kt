package com.paligot.confily.core.events

import com.paligot.confily.core.extensions.format
import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.EventV3
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

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

fun EventItemList.convertToModelDb(past: Boolean): EventItemDb = EventItemDb(
    id = this.id,
    name = this.name,
    date = this.startDate.dropLast(1).toLocalDateTime().format(),
    timestamp = this.startDate.toInstant().toEpochMilliseconds(),
    past = past
)
