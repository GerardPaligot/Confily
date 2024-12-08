package com.paligot.confily.core.events.entities

import com.paligot.confily.core.extensions.formatLocalizedFull
import com.paligot.confily.core.socials.entities.Social
import com.paligot.confily.core.socials.entities.mapToSocialUi
import com.paligot.confily.models.ui.EventInfoUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("EventEntity")
class Event(
    val info: EventInfo,
    val socials: List<Social>
)

fun Event.mapToEventInfoUi(): EventInfoUi = EventInfoUi(
    name = info.name,
    formattedAddress = info.formattedAddress.toImmutableList(),
    address = info.formattedAddress.joinToString("\n"),
    latitude = info.latitude,
    longitude = info.longitude,
    date = info.startTime.formatLocalizedFull(),
    faqLink = info.faqUrl,
    codeOfConductLink = info.cocUrl,
    socials = socials.map { it.mapToSocialUi() }.toImmutableList()
)
