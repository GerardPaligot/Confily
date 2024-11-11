package com.paligot.confily.core.events

import com.paligot.confily.core.extensions.format
import com.paligot.confily.db.EventItem
import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.EventItemUi
import com.paligot.confily.models.ui.MenuItemUi
import com.paligot.confily.models.ui.TicketInfoUi
import com.paligot.confily.models.ui.TicketUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal val eventMapper = { _: String, name: String, formattedAddress: List<String>,
    address: String, latitude: Double, longitude: Double, date: String, _: String, _: String,
    _: String, _: String, _: String?, twitter: String?,
    twitterUrl: String?, linkedin: String?, linkedinUrl: String?,
    faqUrl: String, cocUrl: String, _: Long ->
    EventInfoUi(
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
}

internal val eventItemMapper = { id: String, name: String, date: String, _: Long, _: Boolean ->
    EventItemUi(id = id, name = name, date = date)
}

internal val menuMapper = { name: String, dish: String, accompaniment: String, dessert: String ->
    MenuItemUi(name = name, dish = dish, accompaniment = accompaniment, dessert = dessert)
}

internal val cocMapper = { coc: String, email: String, phone: String? ->
    CoCUi(text = coc, phone = phone, email = email)
}

internal val ticketMapper = { _: String, id: String?, _: String?, _: String?,
    firstname: String?, lastname: String?, _: String, qrcode: ByteArray ->
    TicketUi(
        info = if (id != null && firstname != null && lastname != null) {
            TicketInfoUi(
                id = id,
                firstName = firstname,
                lastName = lastname
            )
        } else {
            null
        },
        qrCode = qrcode
    )
}

fun EventItemList.convertToModelDb(past: Boolean): EventItem = EventItem(
    id = this.id,
    name = this.name,
    date = this.startDate.dropLast(1).toLocalDateTime().format(),
    timestamp = this.startDate.toInstant().toEpochMilliseconds(),
    past = past
)
