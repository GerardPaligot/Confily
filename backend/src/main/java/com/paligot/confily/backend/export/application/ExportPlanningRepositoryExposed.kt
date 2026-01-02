package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.categories.infrastructure.exposed.CategoriesTable
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.categories.infrastructure.exposed.toModel
import com.paligot.confily.backend.export.domain.ExportPlanningRepository
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
import com.paligot.confily.backend.formats.infrastructure.exposed.toModel
import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.schedules.infrastructure.exposed.SchedulesTable
import com.paligot.confily.backend.schedules.infrastructure.exposed.toModel
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.toModel
import com.paligot.confily.backend.sessions.infrastructure.exposed.toSessionModel
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.toModel
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.Session
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class ExportPlanningRepositoryExposed(
    private val database: Database
) : ExportPlanningRepository {
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

    override suspend fun get(eventId: String): AgendaV4 = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val sessions = SessionEntity
            .findByEvent(eventUuid)
            .map { it.toSessionModel() }
        val eventSessions = EventSessionEntity
            .findByEvent(eventUuid)
            .map { it.toModel() }
        AgendaV4(
            schedules = ScheduleEntity
                .findByEvent(eventUuid)
                .orderBy(SchedulesTable.displayOrder to SortOrder.ASC)
                .map { it.toModel() },
            sessions = sessions + eventSessions,
            formats = FormatEntity.findByEvent(eventUuid)
                .orderBy(FormatsTable.createdAt to SortOrder.ASC)
                .map { it.toModel() },
            tags = emptyList(),
            categories = CategoryEntity
                .findByEvent(eventUuid)
                .orderBy(CategoriesTable.displayOrder to SortOrder.ASC)
                .map { it.toModel() },
            speakers = SpeakerEntity
                .findByEvent(eventUuid)
                .map { it.toModel() }
        )
    }

    private fun protect(value: String): String =
        if (value.contains(",") || value.contains('"')) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
}
