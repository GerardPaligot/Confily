package com.paligot.confily.core.events.entities

import com.paligot.confily.core.extensions.formatLocalizedFull
import com.paligot.confily.models.ui.EventInfoUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
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
