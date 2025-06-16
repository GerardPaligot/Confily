package com.paligot.confily.backend.third.parties.billetweb.domain

import com.paligot.confily.models.Attendee

interface BilletWebRepository {
    suspend fun get(eventId: String, barcode: String): Attendee
}
