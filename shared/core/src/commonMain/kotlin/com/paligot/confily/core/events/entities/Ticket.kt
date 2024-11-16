package com.paligot.confily.core.events.entities

import com.paligot.confily.models.ui.TicketInfoUi
import com.paligot.confily.models.ui.TicketUi
import kotlin.native.ObjCName

@ObjCName("TicketEntity")
class Ticket(
    val id: String?,
    val firstName: String?,
    val lastName: String?,
    val qrCode: ByteArray
)

fun Ticket.mapToUi(): TicketUi = TicketUi(
    info = if (id != null && firstName != null && lastName != null) {
        TicketInfoUi(id = id, firstName = firstName, lastName = lastName)
    } else {
        null
    },
    qrCode = qrCode
)
