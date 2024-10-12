package com.paligot.confily.core.networking

import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.models.ui.UserProfileUi

internal val profileMapper = { _: String, email: String, firstname: String, lastname: String,
    company: String?, qrcode: ByteArray ->
    UserProfileUi(
        email = email,
        firstName = firstname,
        lastName = lastname,
        company = company ?: "",
        qrCode = qrcode
    )
}

internal val tickerMapper = { _: String, _: String?, _: String?, _: String?, firstname: String?,
    lastname: String?, _: String, _: ByteArray ->
    UserProfileUi(
        email = "",
        firstName = firstname ?: "",
        lastName = lastname ?: "",
        company = "",
        qrCode = null
    )
}

internal val userItemMapper = { email: String, firstName: String, lastName: String,
    company: String?, _: String, _: Long ->
    UserNetworkingUi(
        email = email,
        firstName = firstName,
        lastName = lastName,
        company = company ?: ""
    )
}
