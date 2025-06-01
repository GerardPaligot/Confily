package com.paligot.confily.backend.export

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.categories.convertToModel
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.events.EventDb
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.formats.convertToModel
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.schedules.convertToEventSession
import com.paligot.confily.backend.schedules.convertToModelV4
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.convertToModel
import com.paligot.confily.backend.tags.TagDao
import com.paligot.confily.backend.tags.convertToModel
import com.paligot.confily.models.AgendaV4
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("LongParameterList")
class ExportPlanningRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val tagDao: TagDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun get(eventId: String): AgendaV4 {
        val eventDb = eventDao.get(eventId)
        return eventDao.getPlanningFile(eventId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning $eventId Not Found")
    }

    suspend fun export(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId)
        val planning = buildPlanning(eventDb)
        eventDao.uploadPlanningFile(eventId, eventDb.agendaUpdatedAt, planning)
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

    private suspend fun buildPlanning(eventDb: EventDb) = coroutineScope {
        val schedules = async(context = dispatcher) {
            scheduleItemDao.getAll(eventDb.slugId).map { it.convertToModelV4() }
        }.await()
        // For older event, get their break sessions
        val breaks = schedules
            .filter { it.id.contains("-pause") }
            .map { it.convertToEventSession() }
        val sessions = async(context = dispatcher) {
            sessionDao.getAll(eventDb.slugId).map { it.convertToModel(eventDb) }
        }
        val formats = async(context = dispatcher) {
            formatDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val categories = async(context = dispatcher) {
            categoryDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val tags = async(context = dispatcher) {
            tagDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val speakers = async(context = dispatcher) {
            speakerDao.getAll(eventDb.slugId).map { it.convertToModel() }
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
