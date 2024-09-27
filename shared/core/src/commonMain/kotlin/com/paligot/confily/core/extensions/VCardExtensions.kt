package com.paligot.confily.core.extensions

import com.paligot.confily.models.ui.UserNetworkingUi

inline fun UserNetworkingUi.encodeToString(): String = """BEGIN:VCARD
VERSION:4.0
N:$lastName;$firstName
EMAIL;INTERNET:$email
ORG:$company
END:VCARD
""".trimMargin()
