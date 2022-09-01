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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.extensions.formatHoursMinutes
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.CategoryUi
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
    val scheduleMapper =
        { id: String, _: String, startTime: String, endTime: String, room: String,
            title: String, abstract: String, category: String, categoryColor: String?, categoryIcon: String?,
            speakers: List<String>, speakersAvatar: List<String>, is_favorite: Boolean, level: String? ->
            val startDateTime = startTime.toLocalDateTime()
            val endDateTime = endTime.toLocalDateTime()
            val diff = endDateTime.toInstant(TimeZone.UTC).minus(startDateTime.toInstant(TimeZone.UTC))
            TalkItemUi(
                id = id,
                room = room,
                level = level,
                slotTime = startDateTime.formatHoursMinutes(),
                startTime = startTime,
                endTime = endTime,
                timeInMinutes = diff.inWholeMinutes.toInt(),
                title = title,
                abstract = abstract,
                category = CategoryUi(name = category, color = categoryColor, icon = categoryIcon),
                speakers = speakers,
                speakersAvatar = speakersAvatar,
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
                        val talks =
                            if (onlyFavorites) it.filter { it.isFavorite == onlyFavorites } else it
                        AgendaUi(
                            onlyFavorites = onlyFavorites,
                            talks = talks.groupBy { it.slotTime }
                                .mapValues { entry -> entry.value.sortedBy { it.room } }
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
                    category_color = talk.category_color,
                    category_icon = talk.category_icon,
                    format = talk.format,
                    open_feedback = talk.open_feedback?.split("/")?.lastOrNull(),
                    open_feedback_url = talk.open_feedback_url
                )
                val speakers = schedule.talk!!.speakers.map { it.convertToModelDb(eventId) }
                speakers.forEach {
                    db.speakerQueries.insertSpeaker(
                        id = it.id,
                        display_name = it.display_name,
                        bio = it.bio,
                        company = it.company,
                        photo_url = it.photo_url,
                        twitter = it.twitter,
                        github = it.github,
                        event_id = eventId
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
                    title = schedule.talk?.title ?: "Pause ☕️",
                    abstract_ = schedule.talk?.abstract ?: "",
                    category = schedule.talk?.category ?: "",
                    category_color = schedule.talk?.categoryStyle?.color,
                    category_icon = schedule.talk?.categoryStyle?.icon,
                    speakers = schedule.talk?.speakers?.map { it.displayName } ?: emptyList(),
                    speakers_avatar = schedule.talk?.speakers?.map { it.photoUrl } ?: emptyList(),
                    is_favorite = false,
                    level = schedule.talk?.level
                )
            } else {
                db.agendaQueries.updateSchedule(
                    id = schedule.id,
                    event_id = eventId,
                    start_time = schedule.startTime,
                    end_time = schedule.endTime,
                    room = schedule.room,
                    title = schedule.talk?.title ?: "Pause ☕️",
                    abstract_ = schedule.talk?.abstract ?: "",
                    category = schedule.talk?.category ?: "",
                    category_color = schedule.talk?.categoryStyle?.color,
                    category_icon = schedule.talk?.categoryStyle?.icon,
                    speakers = schedule.talk?.speakers?.map { it.displayName } ?: emptyList(),
                    speakers_avatar = schedule.talk?.speakers?.map { it.photoUrl } ?: emptyList(),
                    level = schedule.talk?.level
                )
            }
        }
    }
}
