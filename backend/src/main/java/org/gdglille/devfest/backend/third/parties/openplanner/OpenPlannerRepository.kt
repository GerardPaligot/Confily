package org.gdglille.devfest.backend.third.parties.openplanner

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.formats.FormatDb
import org.gdglille.devfest.backend.schedulers.ScheduleDb
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.SpeakerDb
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.talks.TalkDb

class OpenPlannerRepository(
    private val openPlannerApi: OpenPlannerApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val scheduleItemDao: ScheduleItemDao
) {
    suspend fun update(eventId: String, apiKey: String) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val config = event.openPlannerConfig
            ?: throw NotAcceptableException("OpenPlanner config not initialized")
        val openPlanner = openPlannerApi.fetchPrivateJson(config.eventId, config.privateId)
        val categories = openPlanner.event.categories
            .map { async { createOrMergeCategory(eventId, it) } }
            .awaitAll()
        val formats = openPlanner.event.formats
            .map { async { createOrMergeFormat(eventId, it) } }
            .awaitAll()
        val allSpeakers = openPlanner.sessions
            .map { it.speakerIds }.flatten()
        val speakers = openPlanner.speakers
            .filter { allSpeakers.contains(it.id) }
            .map { async { createOrMergeSpeaker(eventId, it) } }
            .awaitAll()
        val talks = openPlanner.sessions
            .map { async { createOrMergeTalks(eventId, it) } }
            .awaitAll()
        val trackIds = openPlanner.event.tracks.map { it.id }
        val schedules = openPlanner.sessions
            .filter { it.trackId != null && it.dateStart != null && it.dateEnd != null }
            .groupBy { it.dateStart }
            .map {
                async {
                    it.value
                        .sortedWith { sessionA, sessionB ->
                            trackIds.indexOf(sessionA.trackId)
                                .compareTo(trackIds.indexOf(sessionB.trackId))
                        }
                        .mapIndexed { index, sessionOP ->
                            createOrMergeSchedule(
                                eventId,
                                index,
                                sessionOP,
                                openPlanner.event.tracks
                            )
                        }
                }
            }
            .awaitAll()
            .flatten()
        clean(eventId, categories, formats, speakers, talks, schedules)
        eventDao.updateAgendaUpdatedAt(event)
    }

    private suspend fun clean(
        eventId: String,
        categories: List<CategoryDb>,
        formats: List<FormatDb>,
        speakers: List<SpeakerDb>,
        talks: List<TalkDb>,
        schedules: List<ScheduleDb>
    ) = coroutineScope {
        categoryDao.deleteDiff(eventId, categories.map { it.id!! })
        formatDao.deleteDiff(eventId, formats.map { it.id!! })
        speakerDao.deleteDiff(eventId, speakers.map { it.id })
        talkDao.deleteDiff(eventId, talks.map { it.id })
        scheduleItemDao.deleteDiff(eventId, schedules.map { it.id })
    }

    private suspend fun createOrMergeCategory(eventId: String, category: CategoryOP): CategoryDb {
        val existing = categoryDao.get(eventId, category.id)
        return if (existing == null) {
            val item = category.convertToDb()
            categoryDao.createOrUpdate(eventId, item)
            item
        } else {
            val item = existing.mergeWith(category)
            categoryDao.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun createOrMergeFormat(eventId: String, format: FormatOP): FormatDb {
        val existing = formatDao.get(eventId, format.id)
        return if (existing == null) {
            val item = format.convertToDb()
            formatDao.createOrUpdate(eventId, item)
            item
        } else {
            val item = existing.mergeWith(format)
            formatDao.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun createOrMergeSpeaker(eventId: String, speaker: SpeakerOP): SpeakerDb {
        val existing = speakerDao.get(eventId, speaker.id)
        return if (existing == null) {
            val item = speaker.convertToDb()
            speakerDao.createOrUpdate(eventId, item)
            item
        } else {
            val item = existing.mergeWith(speaker)
            speakerDao.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun createOrMergeTalks(eventId: String, session: SessionOP): TalkDb {
        val existing = talkDao.get(eventId, session.id)
        return if (existing == null) {
            val item = session.convertToTalkDb()
            talkDao.createOrUpdate(eventId, item)
            item
        } else {
            val item = existing.mergeWith(session)
            talkDao.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun createOrMergeSchedule(
        eventId: String, order: Int, session: SessionOP, tracks: List<TrackOP>
    ): ScheduleDb {
        val item = session.convertToScheduleDb(order, tracks)
        scheduleItemDao.createOrUpdate(eventId, item)
        return item
    }
}
