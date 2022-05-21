package org.gdglille.devfest.database

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.extensions.formatHoursMinutes
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.ScheduleItem
import org.gdglille.devfest.models.TalkItemUi

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
class ScheduleDao(
    private val db: Conferences4HallDatabase,
    private val settings: ObservableSettings,
    private val eventId: String
) {
    private val scheduleMapper = { id: String, _: String, startTime: String, endTime: String, room: String, title: String, speakers: List<String>, is_favorite: Boolean ->
        TalkItemUi(
            id = id,
            room = room,
            slotTime = startTime.toLocalDateTime().formatHoursMinutes(),
            startTime = startTime,
            endTime = endTime,
            title = title,
            speakers = speakers,
            isFavorite = is_favorite
        )
    }
    fun fetchSchedules(): Flow<AgendaUi> {
        return settings.getBooleanFlow("ONLY_FAVORITES", false)
            .flatMapMerge {
                return@flatMapMerge db.agendaQueries.selectScheduleItems(eventId, scheduleMapper)
                    .asFlow()
                    .mapToList()
                    .map {
                        val onlyFavorites = settings.getBoolean("ONLY_FAVORITES", false)
                        val talks = if (onlyFavorites) it.filter { it.isFavorite == onlyFavorites } else it
                        AgendaUi(
                            onlyFavorites = onlyFavorites,
                            talks = talks.groupBy { it.slotTime }.mapValues { entry -> entry.value.sortedBy { it.room } }
                        )
                    }
            }
    }

    fun toggleFavoriteFiltering() {
        val isFavorite = settings.getBoolean("ONLY_FAVORITES", false)
        settings.putBoolean("ONLY_FAVORITES", !isFavorite)
    }

    fun markAsFavorite(scheduleId: String, isFavorite: Boolean) = db.transaction {
        db.agendaQueries.markAsFavorite(isFavorite, scheduleId)
        if (isFavorite) return@transaction
        val onlyFavorites = settings.getBoolean("ONLY_FAVORITES", false)
        if (!onlyFavorites) return@transaction
        val countFavorites = db.agendaQueries.countScheduleItems(eventId, true).executeAsOneOrNull()
        if (countFavorites != null && countFavorites != 0L) return@transaction
        settings.putBoolean("ONLY_FAVORITES", false)
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
                    open_feedback = talk.open_feedback?.split("/")?.lastOrNull()
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
