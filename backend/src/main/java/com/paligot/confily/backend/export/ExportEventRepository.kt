package com.paligot.confily.backend.export

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.MapEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.MapFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.map.application.convertToModel
import com.paligot.confily.backend.qanda.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.TeamFirestore
import com.paligot.confily.backend.team.application.convertToModel
import com.paligot.confily.models.ExportEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class ExportEventRepository(
    private val eventDao: EventFirestore,
    private val eventStorage: EventStorage,
    private val qAndAFirestore: QAndAFirestore,
    private val teamFirestore: TeamFirestore,
    private val mapFirestore: MapFirestore,
    private val partnerFirestore: PartnerFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun get(eventId: String): ExportEvent {
        val eventDb = eventDao.get(eventId)
        return eventStorage.getEventFile(eventId, eventDb.eventUpdatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
    }

    suspend fun export(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId)
        val event = buildEvent(eventDb)
        eventStorage.uploadEventFile(eventId, eventDb.eventUpdatedAt, event)
        return@coroutineScope event
    }

    private suspend fun buildEvent(event: EventEntity): ExportEvent = coroutineScope {
        val qanda = async(dispatcher) {
            qAndAFirestore.getAll(event.slugId)
                .groupBy { it.language }
                .map { it.key to it.value.sortedBy { it.order }.map(QAndAEntity::convertToModel) }
                .toMap()
        }
        val maps = async(dispatcher) { mapFirestore.getAll(event.slugId).map(MapEntity::convertToModel) }
        return@coroutineScope event.convertToModelV5(
            qanda = qanda.await(),
            team = teamMembers(event),
            maps = maps.await(),
            hasPartners = partnerFirestore.hasPartners(event.slugId)
        )
    }

    private suspend fun teamMembers(eventDb: EventEntity) = coroutineScope {
        val orderMap = eventDb.teamGroups.associate { it.name to it.order }
        return@coroutineScope teamFirestore.getAll(eventDb.slugId)
            .asSequence()
            .sortedBy { orderMap[it.teamName] ?: 0 }
            .groupBy { it.teamName }
            .filter { it.key != "" }
            .map { entry -> entry.key to entry.value.map { it.convertToModel() } }
            .associate { it }
    }
}
