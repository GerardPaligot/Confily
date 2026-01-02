package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.categories.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.categories.infrastructure.firestore.convertToModel
import com.paligot.confily.backend.events.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.events.infrastructure.storage.EventStorage
import com.paligot.confily.backend.export.domain.ExportAdminRepository
import com.paligot.confily.backend.formats.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.formats.infrastructure.firestore.convertToModel
import com.paligot.confily.backend.schedules.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.schedules.infrastructure.firestore.convertToEventSession
import com.paligot.confily.backend.schedules.infrastructure.firestore.convertToModelV4
import com.paligot.confily.backend.sessions.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.sessions.infrastructure.firestore.convertToModel
import com.paligot.confily.backend.speakers.application.convertToModel
import com.paligot.confily.backend.speakers.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.tags.infrastructure.firestore.TagFirestore
import com.paligot.confily.backend.tags.infrastructure.firestore.convertToModel
import com.paligot.confily.models.AgendaV4
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class ExportPlanningAdminRepositoryDefault(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage,
    private val speakerFirestore: SpeakerFirestore,
    private val sessionFirestore: SessionFirestore,
    private val categoryDao: CategoryFirestore,
    private val formatFirestore: FormatFirestore,
    private val tagFirestore: TagFirestore,
    private val scheduleItemFirestore: ScheduleItemFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExportAdminRepository<AgendaV4> {
    override suspend fun export(eventId: String): AgendaV4 {
        val eventDb = eventFirestore.get(eventId)
        val planning = buildPlanning(eventDb)
        eventStorage.uploadPlanningFile(eventId, eventDb.agendaUpdatedAt, planning)
        return planning
    }

    private suspend fun buildPlanning(eventDb: EventEntity) = coroutineScope {
        val schedules = async(context = dispatcher) {
            scheduleItemFirestore.getAll(eventDb.slugId).map { it.convertToModelV4() }
        }.await()
        // For older event, get their break sessions
        val breaks = schedules
            .filter { it.id.contains("-pause") }
            .map { it.convertToEventSession() }
        val sessions = async(context = dispatcher) {
            sessionFirestore.getAll(eventDb.slugId).map { it.convertToModel(eventDb) }
        }
        val formats = async(context = dispatcher) {
            formatFirestore.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val categories = async(context = dispatcher) {
            categoryDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val tags = async(context = dispatcher) {
            tagFirestore.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val speakers = async(context = dispatcher) {
            speakerFirestore.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        return@coroutineScope AgendaV4(
            schedules = schedules,
            sessions = sessions.await() + breaks,
            formats = formats.await(),
            categories = categories.await(),
            tags = tags.await(),
            speakers = speakers.await()
        )
    }
}
