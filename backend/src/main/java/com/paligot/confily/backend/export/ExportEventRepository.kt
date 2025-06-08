package com.paligot.confily.backend.export

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.map.MapDao
import com.paligot.confily.backend.map.MapDb
import com.paligot.confily.backend.map.convertToModel
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.qanda.QAndADb
import com.paligot.confily.backend.qanda.convertToModel
import com.paligot.confily.backend.team.TeamDao
import com.paligot.confily.backend.team.convertToModel
import com.paligot.confily.models.ExportEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class ExportEventRepository(
    private val eventDao: EventFirestore,
    private val eventStorage: EventStorage,
    private val qAndADao: QAndADao,
    private val teamDao: TeamDao,
    private val mapDao: MapDao,
    private val partnerDao: PartnerDao,
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
            qAndADao.getAll(event.slugId)
                .groupBy { it.language }
                .map { it.key to it.value.sortedBy { it.order }.map(QAndADb::convertToModel) }
                .toMap()
        }
        val maps = async(dispatcher) { mapDao.getAll(event.slugId).map(MapDb::convertToModel) }
        return@coroutineScope event.convertToModelV5(
            qanda = qanda.await(),
            team = teamMembers(event),
            maps = maps.await(),
            hasPartners = partnerDao.hasPartners(event.slugId)
        )
    }

    private suspend fun teamMembers(eventDb: EventEntity) = coroutineScope {
        val orderMap = eventDb.teamGroups.associate { it.name to it.order }
        return@coroutineScope teamDao.getAll(eventDb.slugId)
            .asSequence()
            .sortedBy { orderMap[it.teamName] ?: 0 }
            .groupBy { it.teamName }
            .filter { it.key != "" }
            .map { entry -> entry.key to entry.value.map { it.convertToModel() } }
            .associate { it }
    }
}
