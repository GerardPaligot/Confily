package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.categories.CategoryDb
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.events.EventDb
import com.paligot.confily.backend.events.TeamGroupDb
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.formats.FormatDb
import com.paligot.confily.backend.internals.CommonApi
import com.paligot.confily.backend.internals.mimeType
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.qanda.QAndADb
import com.paligot.confily.backend.schedules.ScheduleDb
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.SessionDb
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.SpeakerDb
import com.paligot.confily.backend.team.TeamDao
import com.paligot.confily.backend.team.TeamDb
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class OpenPlannerRepository(
    private val openPlannerApi: OpenPlannerApi,
    private val commonApi: CommonApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val qAndADao: QAndADao,
    private val teamDao: TeamDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun update(eventId: String) = coroutineScope {
        val event = eventDao.get(eventId)
        val config = event.openPlannerConfig
            ?: throw NotAcceptableException("OpenPlanner config not initialized")
        val openPlanner = openPlannerApi.fetchPrivateJson(config.eventId, config.privateId)
        val qandas = openPlanner.faq
            .sortedBy { it.order }
            .fold(mutableListOf<FaqItemOP>()) { acc, qAndA ->
                acc.addAll(qAndA.items.sortedBy { it.order })
                acc
            }
            .mapIndexed { index, faqItemOP ->
                async(dispatcher) {
                    createOrMergeQAndA(eventId, index, event.defaultLanguage, faqItemOP)
                }
            }
            .awaitAll()
        val teamMembers = openPlanner.team
            .mapIndexed { index, it ->
                async(dispatcher) { createOrMergeTeamMember(eventId, it, index) }
            }
            .awaitAll()
        val categories = openPlanner.event.categories
            .map { async(dispatcher) { createOrMergeCategory(eventId, it) } }
            .awaitAll()
        val formats = openPlanner.event.formats
            .map { async(dispatcher) { createOrMergeFormat(eventId, it) } }
            .awaitAll()
        val allSpeakers = openPlanner.sessions
            .map { it.speakerIds }.flatten()
        val speakers = openPlanner.speakers
            .filter { allSpeakers.contains(it.id) }
            .map { async(dispatcher) { createOrMergeSpeaker(eventId, it) } }
            .awaitAll()
        val trackIds = openPlanner.event.tracks.map { it.id }
        openPlanner.sessions
            .map { async(dispatcher) { createOrMergeTalks(event, openPlanner.event.tracks, it) } }
            .awaitAll()
        val schedules = openPlanner.sessions
            .filter { it.trackId != null && it.dateStart != null && it.dateEnd != null }
            .groupBy { it.dateStart }
            .map {
                async(dispatcher) {
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
        clean(event, qandas, categories, formats, speakers, schedules, teamMembers)
        eventDao.updateAgendaUpdatedAt(
            event.copy(
                teamGroups = openPlanner.team
                    .map { TeamGroupDb(name = it.team ?: "", order = it.teamOrder ?: 0) }
                    .distinct()
            )
        )
    }

    @Suppress("LongParameterList")
    private suspend fun clean(
        event: EventDb,
        qandas: List<QAndADb>,
        categories: List<CategoryDb>,
        formats: List<FormatDb>,
        speakers: List<SpeakerDb>,
        schedules: List<ScheduleDb>,
        teamMembers: List<TeamDb>
    ) = coroutineScope {
        qAndADao.deleteDiff(event.slugId, qandas.map { it.id!! })
        teamDao.deleteDiff(event.slugId, teamMembers.map { it.id!! })
        categoryDao.deleteDiff(event.slugId, categories.map { it.id!! })
        formatDao.deleteDiff(event.slugId, formats.map { it.id!! })
        speakerDao.deleteDiff(event.slugId, speakers.map { it.id })
        scheduleItemDao.deleteDiff(event.slugId, schedules.map { it.id })
        val talkIds = schedules
            .filter { it.talkId != null && event.eventSessionTracks.contains(it.room).not() }
            .map { it.talkId!! }
        sessionDao.deleteDiffTalkSessions(event.slugId, talkIds)
        val eventSessionIds = schedules
            .filter { it.talkId != null && event.eventSessionTracks.contains(it.room) }
            .map { it.talkId!! }
        sessionDao.deleteDiffEventSessions(event.slugId, eventSessionIds)
    }

    private suspend fun createOrMergeQAndA(
        eventId: String,
        order: Int,
        language: String,
        faqItemOP: FaqItemOP
    ): QAndADb {
        val item = faqItemOP.convertToQAndADb(order = order, language = language)
        qAndADao.createOrUpdate(eventId, item)
        return item
    }

    private suspend fun createOrMergeTeamMember(
        eventId: String,
        team: TeamOP,
        index: Int
    ): TeamDb {
        val existing = teamDao.get(eventId, team.id)
        return if (existing == null) {
            val photoUrl = getAvatarUrl(eventId, team)
            val item = team.convertToTeamDb(index, photoUrl)
            teamDao.createOrUpdate(eventId, item)
            item
        } else {
            val photoUrl = getAvatarUrl(eventId, team)
            val item = existing.mergeWith(photoUrl, team)
            teamDao.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun getAvatarUrl(eventId: String, team: TeamOP) = try {
        if (team.photoUrl != null) {
            val avatar = commonApi.fetchByteArray(team.photoUrl)
            val bucketItem = teamDao.saveTeamPicture(
                eventId = eventId,
                id = team.id,
                content = avatar,
                mimeType = team.photoUrl.mimeType
            )
            bucketItem.url
        } else {
            null
        }
    } catch (_: Throwable) {
        team.photoUrl
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
            val photoUrl = getAvatarUrl(eventId, speaker)
            val item = speaker.convertToDb(photoUrl)
            speakerDao.createOrUpdate(eventId, item)
            item
        } else {
            val photoUrl = getAvatarUrl(eventId, speaker)
            val item = existing.mergeWith(photoUrl, speaker)
            speakerDao.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun getAvatarUrl(eventId: String, speaker: SpeakerOP) = try {
        if (speaker.photoUrl != null) {
            val avatar = commonApi.fetchByteArray(speaker.photoUrl)
            val bucketItem = speakerDao.saveProfile(
                eventId = eventId,
                id = speaker.id,
                content = avatar,
                mimeType = speaker.photoUrl.mimeType
            )
            bucketItem.url
        } else {
            null
        }
    } catch (_: Throwable) {
        speaker.photoUrl
    }

    private suspend fun createOrMergeTalks(
        event: EventDb,
        tracks: List<TrackOP>,
        session: SessionOP
    ): SessionDb {
        val track = tracks.find { it.id == session.trackId }
        return if (event.eventSessionTracks.contains(track?.name)) {
            val existing = sessionDao.getEventSession(event.slugId, session.id)
            val item = existing?.mergeWith(session) ?: session.convertToEventSessionDb()
            sessionDao.createOrUpdate(event.slugId, item)
            item
        } else {
            val existing = sessionDao.getTalkSession(event.slugId, session.id)
            val item = existing?.mergeWith(session) ?: session.convertToTalkDb()
            sessionDao.createOrUpdate(event.slugId, item)
            item
        }
    }

    private suspend fun createOrMergeSchedule(
        eventId: String,
        order: Int,
        session: SessionOP,
        tracks: List<TrackOP>
    ): ScheduleDb {
        val item = session.convertToScheduleDb(order, tracks)
        scheduleItemDao.createOrUpdate(eventId, item)
        return item
    }
}
