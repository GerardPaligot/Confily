package com.paligot.confily.core.events

import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.Event
import com.paligot.confily.core.events.entities.EventItem
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.Social
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal val eventMapper = { id: String, name: String, formattedAddress: List<String>,
    _: String, latitude: Double, longitude: Double, _: String,
    startDate: String, endDate: String, _: String?, contactEmail: String?,
    contactPhone: String?, _: String?, twitterUrl: String?, _: String?,
    linkedinUrl: String?, faqUrl: String, cocUrl: String, _: Long ->
    Event(
        id = id,
        name = name,
        formattedAddress = formattedAddress,
        latitude = latitude,
        longitude = longitude,
        startTime = Instant.parse(startDate).toLocalDateTime(TimeZone.currentSystemDefault()),
        endTime = Instant.parse(endDate).toLocalDateTime(TimeZone.currentSystemDefault()),
        email = contactEmail,
        phone = contactPhone,
        socials = listOfNotNull(
            twitterUrl?.let { Social(url = it, type = "twitter") },
            linkedinUrl?.let { Social(url = it, type = "linkedin") }
        ),
        faqUrl = faqUrl,
        cocUrl = cocUrl
    )
}

internal val eventItemMapper = { id: String, name: String, date: String, _: Long, past: Boolean ->
    EventItem(
        id = id,
        name = name,
        date = Instant.parse(date).toLocalDateTime(TimeZone.currentSystemDefault()).date,
        past = past
    )
}

internal val menuMapper = { name: String, dish: String, accompaniment: String, dessert: String ->
    MenuItem(name = name, dish = dish, accompaniment = accompaniment, dessert = dessert)
}

internal val cocMapper = { url: String, coc: String?, email: String?, phone: String? ->
    CodeOfConduct(url = url, content = coc, phone = phone, email = email)
}
