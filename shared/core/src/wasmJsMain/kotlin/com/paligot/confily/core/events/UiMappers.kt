package com.paligot.confily.core.events

import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.EventItemUi
import com.paligot.confily.models.ui.MenuItemUi
import kotlinx.collections.immutable.toImmutableList

fun EventItemDb.convertToUi(): EventItemUi = EventItemUi(id = id, name = name, date)

fun EventDb.convertToEventInfoUi() = EventInfoUi(
    name = name,
    formattedAddress = formattedAddress.toImmutableList(),
    address = address,
    latitude = latitude,
    longitude = longitude,
    date = date,
    twitter = twitter,
    twitterUrl = twitterUrl,
    linkedin = linkedin,
    linkedinUrl = linkedinUrl,
    faqLink = faqUrl,
    codeOfConductLink = cocUrl
)

fun MenuDb.convertToUi() = MenuItemUi(name = name, dish = dish, accompaniment = accompaniment, dessert = dessert)

fun CocDb.convertToUi() = CoCUi(text = coc, phone = phone, email = email)
