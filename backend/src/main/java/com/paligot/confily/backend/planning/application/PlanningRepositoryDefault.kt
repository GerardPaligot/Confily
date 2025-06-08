package com.paligot.confily.backend.planning.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.date.FormatterPattern
import com.paligot.confily.backend.internals.helpers.date.format
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.openFeedbackUrl
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.planning.domain.PlanningRepository
import com.paligot.confily.models.Agenda
import com.paligot.confily.models.AgendaV3
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.Category
import com.paligot.confily.models.Format
import com.paligot.confily.models.Info
import com.paligot.confily.models.PlanningItem
import com.paligot.confily.models.ScheduleItem
import com.paligot.confily.models.ScheduleItemV3
import com.paligot.confily.models.ScheduleItemV4
import com.paligot.confily.models.Session
import com.paligot.confily.models.Speaker
import com.paligot.confily.models.Talk
import com.paligot.confily.models.TalkV3
import java.time.LocalDateTime

class PlanningRepositoryDefault(
    private val eventStorage: EventStorage
) : PlanningRepository {
    override suspend fun agenda(eventDb: EventEntity): Agenda {
        val planning = eventStorage.getPlanningFile(eventDb.slugId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning for event ${eventDb.slugId} not found")
        return Agenda(
            talks = planning.schedules
                .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.HoursMinutes) }
                .entries.map {
                    it.key to it.value
                        .map { schedule -> schedule.convertToModel(planning, eventDb) }
                        .sortedBy { it.room }
                }.associate { it }.toSortedMap()
        )
    }

    override suspend fun agendaMultiDays(eventDb: EventEntity): Map<String, Map<String, List<ScheduleItem>>> {
        val planning = eventStorage.getPlanningFile(eventDb.slugId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning for event ${eventDb.slugId} not found")
        return planning.schedules
            .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.YearMonthDay) }
            .entries.map { schedulesByDay ->
                schedulesByDay.key to schedulesByDay.value
                    .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.HoursMinutes) }
                    .entries.map { scheduleBySlot ->
                        scheduleBySlot.key to scheduleBySlot.value
                            .map { schedule -> schedule.convertToModel(planning, eventDb) }
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
        eventDb: EventEntity
    ): Map<String, Map<String, List<PlanningItem>>> {
        val planning = eventStorage.getPlanningFile(eventDb.slugId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning for event ${eventDb.slugId} not found")
        return planning.schedules
            .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.YearMonthDay) }
            .entries.map { schedulesByDay ->
                schedulesByDay.key to schedulesByDay.value
                    .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.HoursMinutes) }
                    .entries.map { scheduleBySlot ->
                        scheduleBySlot.key to scheduleBySlot.value
                            .map { schedule -> schedule.convertToPlanningModel(planning, eventDb) }
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

    override suspend fun planning(eventDb: EventEntity): AgendaV3 {
        val planning = eventStorage.getPlanningFile(eventDb.slugId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning for event ${eventDb.slugId} not found")
        return AgendaV3(
            sessions = planning.schedules.map { it.convertToModel() },
            talks = planning.sessions.filterIsInstance<Session.Talk>().map { it.convertToModel(eventDb) },
            formats = planning.formats,
            categories = planning.categories,
            speakers = planning.speakers
        )
    }

    override suspend fun planningBySchedules(eventDb: EventEntity): AgendaV4 {
        val planning = eventStorage.getPlanningFile(eventDb.slugId, eventDb.agendaUpdatedAt)
            ?: throw NotFoundException("Planning for event ${eventDb.slugId} not found")
        return AgendaV4(
            schedules = planning.schedules,
            sessions = planning.sessions,
            formats = planning.formats,
            categories = planning.categories,
            speakers = planning.speakers,
            tags = planning.tags
        )
    }
}

private fun ScheduleItemV4.convertToModel(): ScheduleItemV3 = ScheduleItemV3(
    id = this.id,
    order = order,
    date = LocalDateTime.parse(this.startTime).format(FormatterPattern.YearMonthDay),
    startTime = this.startTime,
    endTime = this.endTime,
    room = this.room,
    talkId = this.sessionId
)

fun Session.Talk.convertToModel(eventDb: EventEntity): TalkV3 = TalkV3(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    categoryId = this.categoryId,
    formatId = this.formatId,
    language = this.language,
    speakers = this.speakers,
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    openFeedback = eventDb.openFeedbackUrl()?.let { "$it/$id" } ?: run { null }
)

private fun ScheduleItemV4.convertToModel(
    planning: AgendaV4,
    eventDb: EventEntity
): ScheduleItem {
    val session = planning.sessions.find { it.id == this.id }
        ?: throw NotFoundException("Session with id ${this.id} not found in planning")
    return when (session) {
        is Session.Event -> this.convertToModel(session = session, talk = null)
        is Session.Talk -> this.convertToModel(
            session = session,
            talk = session.convertToModel(
                speakers = planning.speakers.filter { session.speakers.contains(it.id) },
                category = planning.categories.find { session.categoryId == it.id },
                format = planning.formats.find { session.formatId == it.id },
                eventDb = eventDb
            )
        )
    }
}

private fun ScheduleItemV4.convertToModel(session: Session, talk: Talk?) = ScheduleItem(
    id = session.id,
    order = this.order,
    time = LocalDateTime.parse(this.startTime).format(FormatterPattern.HoursMinutes),
    startTime = this.startTime,
    endTime = this.endTime,
    room = this.room,
    talk = talk
)

private fun ScheduleItemV4.convertToPlanningModel(
    planning: AgendaV4,
    eventDb: EventEntity
): PlanningItem {
    val session = planning.sessions.find { it.id == this.id }
        ?: throw NotFoundException("Session with id ${this.id} not found in planning")
    return when (session) {
        is Session.Event -> this.convertToPlanningEventModel(
            info = Info(id = session.id, title = session.title, description = session.description)
        )

        is Session.Talk -> this.convertToPlanningTalkModel(
            talk = session.convertToModel(
                speakers = planning.speakers.filter { session.speakers.contains(it.id) },
                category = planning.categories.find { session.categoryId == it.id },
                format = planning.formats.find { session.formatId == it.id },
                eventDb = eventDb
            )
        )
    }
}

fun ScheduleItemV4.convertToPlanningTalkModel(talk: Talk) = PlanningItem.TalkItem(
    id = this.id,
    order = order,
    startTime = this.startTime,
    endTime = this.endTime,
    room = this.room,
    talk = talk
)

fun ScheduleItemV4.convertToPlanningEventModel(info: Info) = PlanningItem.EventItem(
    id = this.id,
    order = order,
    startTime = this.startTime,
    endTime = this.endTime,
    room = this.room,
    info = info
)

private fun Session.Talk.convertToModel(
    speakers: List<Speaker>,
    category: Category?,
    format: Format?,
    eventDb: EventEntity
): Talk = Talk(
    id = this.id,
    title = this.title,
    level = this.level,
    abstract = this.abstract,
    category = category?.name ?: "",
    categoryStyle = category,
    format = format?.name ?: "",
    language = this.language,
    speakers = speakers,
    linkSlides = this.linkSlides,
    linkReplay = this.linkReplay,
    openFeedback = eventDb.openFeedbackUrl()?.let { "$it/$id" } ?: run { null }
)
