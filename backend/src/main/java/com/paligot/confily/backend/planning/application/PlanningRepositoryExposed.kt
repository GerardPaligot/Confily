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
import com.paligot.confily.backend.schedules.infrastructure.exposed.toPlanningItemModel
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
import com.paligot.confily.models.PlanningItem
import com.paligot.confily.models.ScheduleItem
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import com.paligot.confily.backend.events.infrastructure.firestore.EventEntity as FEventEntity

class PlanningRepositoryExposed(
    private val database: Database
) : PlanningRepository {
    private val format = LocalDateTime.Format {
        hour(); char(':'); minute()
    }

    override suspend fun agenda(eventDb: FEventEntity): Agenda = transaction(db = database) {
        val eventUuid = UUID.fromString(eventDb.slugId)
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

    override suspend fun agendaMultiDays(eventDb: FEventEntity): Map<String, Map<String, List<ScheduleItem>>> =
        transaction(db = database) {
            val eventUuid = UUID.fromString(eventDb.slugId)
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

    override suspend fun agendaMultiDaysAndEventSessions(
        eventDb: FEventEntity
    ): Map<String, Map<String, List<PlanningItem>>> = transaction(db = database) {
        val eventUuid = UUID.fromString(eventDb.slugId)
        ScheduleEntity
            .findByEvent(eventUuid)
            .groupBy { it.startTime.toLocalDateTime(TimeZone.UTC).date.toString() }
            .entries.map { schedulesByDay ->
                schedulesByDay.key to schedulesByDay.value
                    .groupBy { it.startTime.toLocalDateTime(TimeZone.UTC).format(format) }
                    .entries.map { scheduleBySlot ->
                        scheduleBySlot.key to scheduleBySlot.value
                            .map { schedule -> schedule.toPlanningItemModel() }
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

    override suspend fun planning(eventDb: FEventEntity): AgendaV3 = transaction(db = database) {
        val eventUuid = UUID.fromString(eventDb.slugId)
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

    override suspend fun planningBySchedules(eventDb: FEventEntity): AgendaV4 = transaction(db = database) {
        val eventUuid = UUID.fromString(eventDb.slugId)
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
