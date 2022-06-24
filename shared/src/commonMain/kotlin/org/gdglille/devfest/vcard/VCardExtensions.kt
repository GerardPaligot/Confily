package org.gdglille.devfest.vcard

import org.gdglille.devfest.models.UserNetworkingUi

inline fun UserNetworkingUi.encodeToString(): String = """BEGIN:VCARD
VERSION:4.0
N:$lastName;$firstName
EMAIL;INTERNET:$email
ORG:$company
END:VCARD""".trimMargin()
