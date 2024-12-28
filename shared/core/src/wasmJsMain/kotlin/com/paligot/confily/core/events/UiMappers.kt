package com.paligot.confily.core.events

import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.EventInfo
import com.paligot.confily.core.events.entities.EventItem
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.TeamMemberInfo
import com.paligot.confily.core.events.entities.TeamMemberItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun EventItemDb.convertToEntity(): EventItem = EventItem(
    id = id,
    name = name,
    date = Instant.parse(date).toLocalDateTime(TimeZone.UTC).date,
    past = past
)

fun EventDb.convertToEntity(): EventInfo = EventInfo(
    id = id,
    name = name,
    formattedAddress = formattedAddress.toImmutableList(),
    latitude = latitude,
    longitude = longitude,
    startTime = Instant.parse(date).toLocalDateTime(TimeZone.UTC),
    endTime = Instant.parse(date).toLocalDateTime(TimeZone.UTC),
    email = contactEmail,
    phone = contactPhone,
    faqUrl = faqUrl,
    cocUrl = cocUrl
)

fun MenuDb.convertToEntity() =
    MenuItem(name = name, dish = dish, accompaniment = accompaniment, dessert = dessert)

fun CocDb.convertToEntity() = CodeOfConduct(url = "", content = coc, phone = phone, email = email)

fun TeamMemberDb.convertToEntity() = TeamMemberItem(
    id = id,
    displayName = name,
    photoUrl = photoUrl,
    role = role
)

fun TeamMemberDb.convertToInfoEntity() = TeamMemberInfo(
    id = id,
    displayName = name,
    photoUrl = photoUrl,
    role = role,
    bio = bio
)
