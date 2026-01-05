package com.paligot.confily.backend.planning.application

import com.paligot.confily.backend.categories.infrastructure.exposed.CategoriesTable
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.categories.infrastructure.exposed.toModel
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
import com.paligot.confily.backend.formats.infrastructure.exposed.toModel
import com.paligot.confily.backend.planning.domain.PlanningRepository
import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.schedules.infrastructure.exposed.SchedulesTable
import com.paligot.confily.backend.schedules.infrastructure.exposed.toModel
import com.paligot.confily.backend.schedules.infrastructure.exposed.toModelV3
import com.paligot.confily.backend.schedules.infrastructure.exposed.toModelV4
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.toModel
import com.paligot.confily.backend.sessions.infrastructure.exposed.toModelV3
import com.paligot.confily.backend.sessions.infrastructure.exposed.toSessionModel
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.toModel
import com.paligot.confily.models.Agenda
import com.paligot.confily.models.AgendaV3
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.ScheduleItem
import com.paligot.confily.models.Session
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.format.DateTimeFormatter
import java.util.UUID

class PlanningRepositoryExposed(
    private val database: Database
) : PlanningRepository {
    private val format = LocalDateTime.Format {
        hour(); char(':'); minute()
    }

    private fun protect(value: String): String =
        if (value.contains(",") || value.contains('"')) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }

    override suspend fun getCsv(eventId: String): String {
        val agenda = planningBySchedules(eventId)
        val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val schedules = agenda.schedules
            .sortedWith(
                compareBy(
                    { java.time.LocalDateTime.parse(it.startTime, dateTimeFormatter) },
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

    override suspend fun agenda(eventId: String): Agenda = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        Agenda(
            talks = ScheduleEntity
                .findByEvent(eventUuid)
                .groupBy { it.startTime.toLocalDateTime(TimeZone.UTC).format(format) }
                .entries.map {
                    it.key to it.value
                        .map { schedule -> schedule.toModel() }
                        .sortedBy { it.room }
                }.associate { it }.toSortedMap()
        )
    }

    override suspend fun agendaMultiDays(eventId: String): Map<String, Map<String, List<ScheduleItem>>> =
        transaction(db = database) {
            val eventUuid = UUID.fromString(eventId)
            ScheduleEntity
                .findByEvent(eventUuid)
                .groupBy { it.startTime.toLocalDateTime(TimeZone.UTC).date.toString() }
                .entries.map { schedulesByDay ->
                    schedulesByDay.key to schedulesByDay.value
                        .groupBy { it.startTime.toLocalDateTime(TimeZone.UTC).format(format) }
                        .entries.map { scheduleBySlot ->
                            scheduleBySlot.key to scheduleBySlot.value
                                .map { schedule -> schedule.toModel() }
                                .sortedBy { it.order }
                        }
                        .sortedBy { it.first }
                        .associate { it }
                        .toMap()
                }
                .sortedBy { it.first }
                .associate { it }
                .toMap()
        }

    override suspend fun planning(eventId: String): AgendaV3 = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val sessions = SessionEntity
            .findByEvent(eventUuid)
            .map { it.toModelV3() }
        AgendaV3(
            sessions = ScheduleEntity
                .findByEvent(eventUuid)
                .orderBy(SchedulesTable.displayOrder to SortOrder.ASC)
                .map { it.toModelV3() },
            talks = sessions,
            formats = FormatEntity.findByEvent(eventUuid)
                .orderBy(FormatsTable.createdAt to SortOrder.ASC)
                .map { it.toModel() },
            categories = CategoryEntity
                .findByEvent(eventUuid)
                .orderBy(CategoriesTable.displayOrder to SortOrder.ASC)
                .map { it.toModel() },
            speakers = SpeakerEntity
                .findByEvent(eventUuid)
                .map { it.toModel() }
        )
    }

    override suspend fun planningBySchedules(eventId: String): AgendaV4 = transaction(db = database) {
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
                .map { it.toModelV4() },
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
}
