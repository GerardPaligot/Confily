package com.paligot.conferences.vcard

import com.paligot.conferences.models.UserNetworkingUi

inline fun UserNetworkingUi.encodeToString(): String = """BEGIN:VCARD
VERSION:4.0
N:${lastName};${firstName}
EMAIL;INTERNET:${email}
ORG:${company}
END:VCARD""".trimMargin()
