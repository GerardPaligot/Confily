package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase
import com.paligot.conferences.extensions.formatHoursMinutes
import com.paligot.conferences.models.ScheduleItem
import com.paligot.conferences.models.TalkItemUi
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.toLocalDateTime

class ScheduleDao(private val db: Conferences4HallDatabase, private val eventId: String) {
    fun fetchSchedules(): Flow<List<TalkItemUi>> =
        db.agendaQueries.selectScheduleItems(eventId) { id, _, startTime, _, room, title, speakers, is_favorite ->
            TalkItemUi(
                id = id,
                room = room,
                time = startTime.toLocalDateTime().formatHoursMinutes(),
                title = title,
                speakers = speakers,
                isFavorite = is_favorite
            )
        }.asFlow().mapToList()

    fun markAsFavorite(scheduleId: String, isFavorite: Boolean) {
        db.agendaQueries.markAsFavorite(isFavorite, scheduleId)
    }

    fun insertOrUpdateSchedules(eventId: String, schedules: List<ScheduleItem>) = db.transaction {
        val schedulesCached = db.agendaQueries.selectScheduleItems(eventId).executeAsList()
        schedules.forEach { schedule ->
            if (schedule.talk != null) {
                val talk = schedule.convertToModelDb()
                db.talkQueries.insertTalk(
                    id = talk.id,
                    title = talk.title,
                    start_time = talk.start_time,
                    end_time = talk.end_time,
                    room = talk.room,
                    level = talk.level,
                    abstract_ = talk.abstract_,
                    category = talk.category,
                    format = talk.format,
                    open_feedback = talk.open_feedback
                )
                val speakers = schedule.talk!!.speakers.map { it.convertToModelDb() }
                speakers.forEach {
                    db.speakerQueries.insertSpeaker(
                        id = it.id,
                        display_name = it.display_name,
                        bio = it.bio,
                        company = it.company,
                        photo_url = it.photo_url,
                        twitter = it.twitter,
                        github = it.github
                    )
                    db.talkQueries.insertTalkWithSpeaker(speaker_id = it.id, talk_id = talk.id)
                }
            }
            val cached = schedulesCached.find { it.id == schedule.id }
            if (cached == null) {
                db.agendaQueries.insertSchedule(
                    id = schedule.id,
                    event_id = eventId,
                    start_time = schedule.startTime,
                    end_time = schedule.endTime,
                    room = schedule.room,
                    title = schedule.talk?.title ?: "Pause",
                    speakers = schedule.talk?.speakers?.map { it.displayName } ?: emptyList(),
                    is_favorite = false
                )
            } else {
                db.agendaQueries.updateSchedule(
                    event_id = eventId,
                    start_time = schedule.startTime,
                    end_time = schedule.endTime,
                    room = schedule.room,
                    title = schedule.talk?.title ?: "Pause",
                    speakers = schedule.talk?.speakers?.map { it.displayName } ?: emptyList(),
                    id = schedule.id
                )
            }
        }
    }
}
