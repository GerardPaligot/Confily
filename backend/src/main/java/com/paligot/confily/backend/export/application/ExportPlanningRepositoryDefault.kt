package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.events.infrastructure.storage.EventStorage
import com.paligot.confily.backend.export.domain.ExportPlanningRepository
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.Session
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("LongParameterList")
class ExportPlanningRepositoryDefault(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage
) : ExportPlanningRepository {
    override suspend fun get(eventId: String): AgendaV4 {
        val eventDb = eventFirestore.get(eventId)
        return eventStorage.getPlanningFile(eventId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning $eventId Not Found")
    }

    override suspend fun getCsv(eventId: String): String {
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
            if (session is Session.Talk) {
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
}
