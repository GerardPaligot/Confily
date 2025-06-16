package com.paligot.confily.backend.export

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.application.convertToModel
import com.paligot.confily.backend.formats.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.TagFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.schedules.application.convertToEventSession
import com.paligot.confily.backend.schedules.application.convertToModelV4
import com.paligot.confily.backend.sessions.application.convertToModel
import com.paligot.confily.backend.speakers.application.convertToModel
import com.paligot.confily.backend.tags.application.convertToModel
import com.paligot.confily.models.AgendaV4
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("LongParameterList")
class ExportPlanningRepository(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage,
    private val speakerFirestore: SpeakerFirestore,
    private val sessionFirestore: SessionFirestore,
    private val categoryDao: CategoryFirestore,
    private val formatFirestore: FormatFirestore,
    private val tagFirestore: TagFirestore,
    private val scheduleItemFirestore: ScheduleItemFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun get(eventId: String): AgendaV4 {
        val eventDb = eventFirestore.get(eventId)
        return eventStorage.getPlanningFile(eventId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning $eventId Not Found")
    }

    suspend fun export(eventId: String) = coroutineScope {
        val eventDb = eventFirestore.get(eventId)
        val planning = buildPlanning(eventDb)
        eventStorage.uploadPlanningFile(eventId, eventDb.agendaUpdatedAt, planning)
        return@coroutineScope planning
    }

    suspend fun getCsv(eventId: String): String {
        val agenda = get(eventId)
        val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val schedules = agenda.schedules
            .sortedWith(
                compareBy(
                    { LocalDateTime.parse(it.startTime, dateTimeFormatter) },
                    { it.order }
                )
            )
        val sessionsById = agenda.sessions.associateBy { it.id }
        val formatsById = agenda.formats.associateBy { it.id }
        val categoriesById = agenda.categories.associateBy { it.id }
        val speakersById = agenda.speakers.associateBy { it.id }
        val headers = listOf(
            "start_time",
            "end_time",
            "room",
            "session_title",
            "session_description",
            "session_format",
            "session_category",
            "session_level",
            "speakers"
        )
        val csvRows = mutableListOf(headers.joinToString(","))
        for (schedule in schedules) {
            val session = sessionsById[schedule.sessionId]
            if (session is com.paligot.confily.models.Session.Talk) {
                val format = formatsById[session.formatId]?.name ?: ""
                val category = categoriesById[session.categoryId]?.name ?: ""
                val speakers = session.speakers
                    .mapNotNull { speakersById[it]?.displayName }
                    .joinToString(", ")
                val row = listOf(
                    schedule.startTime,
                    schedule.endTime,
                    schedule.room,
                    session.title,
                    session.abstract.replace("\n", " "),
                    format,
                    category,
                    session.level ?: "",
                    speakers
                ).map { protect(it) }
                csvRows.add(row.joinToString(","))
            }
        }
        return csvRows.joinToString("\n")
    }

    private fun protect(value: String): String =
        if (value.contains(",") || value.contains('"')) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
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
