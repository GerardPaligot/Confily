package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.internals.helpers.mimeType
import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.ScheduleEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.SessionEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.TeamGroupEntity
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.internals.infrastructure.storage.SpeakerStorage
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
    private val eventDao: EventFirestore,
    private val speakerFirestore: SpeakerFirestore,
    private val speakerStorage: SpeakerStorage,
    private val sessionFirestore: SessionFirestore,
    private val categoryDao: CategoryFirestore,
    private val formatFirestore: FormatFirestore,
    private val scheduleItemFirestore: ScheduleItemFirestore,
    private val qAndAFirestore: QAndAFirestore,
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
                    .map { TeamGroupEntity(name = it.team ?: "", order = it.teamOrder ?: 0) }
                    .distinct()
            )
        )
    }

    @Suppress("LongParameterList")
    private suspend fun clean(
        event: EventEntity,
        qandas: List<QAndAEntity>,
        categories: List<CategoryEntity>,
        formats: List<FormatEntity>,
        speakers: List<SpeakerEntity>,
        schedules: List<ScheduleEntity>,
        teamMembers: List<TeamDb>
    ) = coroutineScope {
        qAndAFirestore.deleteDiff(event.slugId, qandas.map { it.id!! })
        teamDao.deleteDiff(event.slugId, teamMembers.map { it.id!! })
        categoryDao.deleteDiff(event.slugId, categories.map { it.id!! })
        formatFirestore.deleteDiff(event.slugId, formats.map { it.id!! })
        speakerFirestore.deleteDiff(event.slugId, speakers.map { it.id })
        scheduleItemFirestore.deleteDiff(event.slugId, schedules.map { it.id })
        val talkIds = schedules
            .filter { it.talkId != null && event.eventSessionTracks.contains(it.room).not() }
            .map { it.talkId!! }
        sessionFirestore.deleteDiffTalkSessions(event.slugId, talkIds)
        val eventSessionIds = schedules
            .filter { it.talkId != null && event.eventSessionTracks.contains(it.room) }
            .map { it.talkId!! }
        sessionFirestore.deleteDiffEventSessions(event.slugId, eventSessionIds)
    }

    private suspend fun createOrMergeQAndA(
        eventId: String,
        order: Int,
        language: String,
        faqItemOP: FaqItemOP
    ): QAndAEntity {
        val item = faqItemOP.convertToQAndADb(order = order, language = language)
        qAndAFirestore.createOrUpdate(eventId, item)
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

    private suspend fun createOrMergeCategory(eventId: String, category: CategoryOP): CategoryEntity {
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

    private suspend fun createOrMergeFormat(eventId: String, format: FormatOP): FormatEntity {
        val existing = formatFirestore.get(eventId, format.id)
        return if (existing == null) {
            val item = format.convertToDb()
            formatFirestore.createOrUpdate(eventId, item)
            item
        } else {
            val item = existing.mergeWith(format)
            formatFirestore.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun createOrMergeSpeaker(eventId: String, speaker: SpeakerOP): SpeakerEntity {
        val existing = speakerFirestore.get(eventId, speaker.id)
        return if (existing == null) {
            val photoUrl = getAvatarUrl(eventId, speaker)
            val item = speaker.convertToDb(photoUrl)
            speakerFirestore.createOrUpdate(eventId, item)
            item
        } else {
            val photoUrl = getAvatarUrl(eventId, speaker)
            val item = existing.mergeWith(photoUrl, speaker)
            speakerFirestore.createOrUpdate(eventId, item)
            item
        }
    }

    private suspend fun getAvatarUrl(eventId: String, speaker: SpeakerOP) = try {
        if (speaker.photoUrl != null) {
            val avatar = commonApi.fetchByteArray(speaker.photoUrl)
            val bucketItem = speakerStorage.saveProfile(
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
        event: EventEntity,
        tracks: List<TrackOP>,
        session: SessionOP
    ): SessionEntity {
        val track = tracks.find { it.id == session.trackId }
        return if (event.eventSessionTracks.contains(track?.name)) {
            val existing = sessionFirestore.getEventSession(event.slugId, session.id)
            val item = existing?.mergeWith(session) ?: session.convertToEventSessionDb()
            sessionFirestore.createOrUpdate(event.slugId, item)
            item
        } else {
            val existing = sessionFirestore.getTalkSession(event.slugId, session.id)
            val item = existing?.mergeWith(session) ?: session.convertToTalkDb()
            sessionFirestore.createOrUpdate(event.slugId, item)
            item
        }
    }

    private suspend fun createOrMergeSchedule(
        eventId: String,
        order: Int,
        session: SessionOP,
        tracks: List<TrackOP>
    ): ScheduleEntity {
        val item = session.convertToScheduleDb(order, tracks)
        scheduleItemFirestore.createOrUpdate(eventId, item)
        return item
    }
}
