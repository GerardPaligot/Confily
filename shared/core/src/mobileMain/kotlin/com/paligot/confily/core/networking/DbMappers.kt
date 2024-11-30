package com.paligot.confily.core.networking

import com.paligot.confily.core.networking.entities.UserInfo
import com.paligot.confily.core.networking.entities.UserItem
import com.paligot.confily.core.networking.entities.UserTicket

internal val profileMapper = { _: String, email: String, firstname: String, lastname: String,
    company: String?, qrcode: ByteArray ->
    UserInfo(
        email = email,
        firstName = firstname,
        lastName = lastname,
        company = company,
        qrCode = qrcode
    )
}

internal val tickerMapper = { id: String?, email: String?, firstname: String?, lastname: String?,
    barcode: String, qrcode: ByteArray ->
    UserTicket(
        id = id,
        email = email ?: "",
        firstName = firstname ?: "",
        lastName = lastname ?: "",
        barcode = barcode,
        qrCode = qrcode
    )
}

internal val userItemMapper = { email: String, firstName: String, lastName: String,
    company: String?, _: String, _: Long ->
    UserItem(
        email = email,
        firstName = firstName,
        lastName = lastName,
        company = company
    )
}
