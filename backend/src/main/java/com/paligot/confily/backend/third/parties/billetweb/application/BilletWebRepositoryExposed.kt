package com.paligot.confily.backend.third.parties.billetweb.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import com.paligot.confily.backend.integrations.infrastructure.exposed.BilletWebIntegrationsTable
import com.paligot.confily.backend.integrations.infrastructure.exposed.IntegrationEntity
import com.paligot.confily.backend.integrations.infrastructure.exposed.get
import com.paligot.confily.backend.third.parties.billetweb.domain.BilletWebRepository
import com.paligot.confily.backend.third.parties.billetweb.infrastructure.provider.BilletWebApi
import com.paligot.confily.models.Attendee
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class BilletWebRepositoryExposed(
    private val database: Database,
    private val api: BilletWebApi
) : BilletWebRepository {
    override suspend fun get(eventId: String, barcode: String): Attendee {
        val eventUuid = UUID.fromString(eventId)
        val integration = transaction(db = database) {
            val integration = IntegrationEntity.findIntegration(
                eventId = eventUuid,
                provider = IntegrationProvider.BILLETWEB,
                usage = IntegrationUsage.TICKETING
            ) ?: throw NotAcceptableException("BilletWeb integration not configured for this event $eventId")
            BilletWebIntegrationsTable[integration.id.value]
        }
        val attendees = api.fetchAttendee(integration.eventId, integration.basic, barcode)
        if (attendees.isEmpty()) throw NotFoundException("$barcode Not Found")
        return attendees.first()
    }
}
