package com.paligot.confily.core.events.entities

import com.paligot.confily.core.extensions.formatLocalizedFull
import com.paligot.confily.models.ui.EventInfoUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.plus
import kotlinx.datetime.until
import kotlin.native.ObjCName

@ObjCName("EventEntity")
class Event(
    val id: String,
    val name: String,
    val formattedAddress: List<String>,
    val latitude: Double,
    val longitude: Double,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val email: String?,
    val phone: String?,
    val socials: List<Social>,
    val faqUrl: String,
    val cocUrl: String
)

fun Event.mapToUi(): EventInfoUi = EventInfoUi(
    name = name,
    formattedAddress = formattedAddress.toImmutableList(),
    address = formattedAddress.joinToString("\n"),
    latitude = latitude,
    longitude = longitude,
    date = startTime.formatLocalizedFull(),
    twitterUrl = socials.first { it.type == "twitter" }.url,
    linkedinUrl = socials.first { it.type == "linkedin" }.url,
    faqLink = faqUrl,
    codeOfConductLink = cocUrl
)

fun Event.mapToDays(): List<String> {
    val startDate = startTime.date
    val endDate = endTime.date
    val days = mutableListOf<String>()
    for (i in 0 until startDate.until(endDate, DateTimeUnit.DAY)) {
        val currentDate = startDate.plus(i, DateTimeUnit.DAY)
        days.add(currentDate.format(LocalDate.Format { byUnicodePattern("dd/MM") }))
    }
    return days
}
