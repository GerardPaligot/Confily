package com.paligot.confily.core.networking.entities

import com.paligot.confily.models.ui.TicketInfoUi
import com.paligot.confily.models.ui.TicketUi
import kotlin.native.ObjCName

@ObjCName("UserTicketEntity")
class UserTicket(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val barcode: String,
    val qrCode: ByteArray
)

fun UserTicket.mapToTicketUi(): TicketUi = TicketUi(
    info = if (id != null && firstName != null && lastName != null) {
        TicketInfoUi(id = id, firstName = firstName, lastName = lastName)
    } else {
        null
    },
    qrCode = qrCode
)
