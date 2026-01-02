package com.paligot.confily.backend.third.parties.openplanner.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoriesTable
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import com.paligot.confily.backend.integrations.infrastructure.exposed.IntegrationEntity
import com.paligot.confily.backend.integrations.infrastructure.exposed.OpenPlannerIntegrationsTable
import com.paligot.confily.backend.integrations.infrastructure.exposed.get
import com.paligot.confily.backend.internals.helpers.mimeType
import com.paligot.confily.backend.internals.helpers.slug
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndATable
import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.schedules.infrastructure.exposed.SchedulesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTrackEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTracksTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionsTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionsTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakersTable
import com.paligot.confily.backend.speakers.infrastructure.storage.SpeakerStorage
import com.paligot.confily.backend.team.infrastructure.exposed.TeamEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamTable
import com.paligot.confily.backend.team.infrastructure.storage.TeamStorage
import com.paligot.confily.backend.third.parties.openplanner.domain.OpenPlannerRepository
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed.groups
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed.toEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed.toEventSessionEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed.toEventSessionTrackEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed.toScheduleEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed.toSessionEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.CategoryOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FaqItemOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FaqSectionOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FormatOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.OpenPlanner
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.OpenPlannerApi
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SessionOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SpeakerOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.TeamOP
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.TrackOP
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class OpenPlannerRepositoryExposed(
    private val database: Database,
    private val openPlannerApi: OpenPlannerApi,
    private val commonApi: CommonApi,
    private val speakerStorage: SpeakerStorage,
    private val teamStorage: TeamStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : OpenPlannerRepository {
    override suspend fun update(eventId: String) {
        val eventUuid = UUID.fromString(eventId)
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val openPlannerIntegration = transaction(db = database) {
            val integration = IntegrationEntity.findIntegration(
                eventId = eventUuid,
                provider = IntegrationProvider.OPENPLANNER,
                usage = IntegrationUsage.AGENDA
            ) ?: throw NotAcceptableException("OpenPlanner integration not found for event $eventId")
            OpenPlannerIntegrationsTable[integration.id.value]
        }
        val openPlanner = openPlannerApi.fetchPrivateJson(
            eventId = openPlannerIntegration.eventId,
            privateId = openPlannerIntegration.apiKey
        )
        val remoteUrls = remoteUrls(openPlanner, event.slug)
        transaction(db = database) {
            upsertQAndA(openPlanner.faq, event)
            upsertTeams(openPlanner.team, remoteUrls, event)
            upsertTracks(openPlanner.event.tracks, event)
            upsertCategories(openPlanner.event.categories, event)
            upsertFormats(openPlanner.event.formats, event)
            upsertSpeakers(openPlanner.speakers, remoteUrls, event)
            upsertEventSessions(openPlanner.sessions, event)
            upsertSessions(openPlanner.sessions, event)
            upsertSchedules(openPlanner.sessions, event)
        }
    }

    private fun upsertQAndA(faq: List<FaqSectionOP>, event: EventEntity): List<QAndAEntity> {
        val qanda = faq
            .sortedBy { it.order }
            .fold(mutableListOf<FaqItemOP>()) { acc, qAndA ->
                acc.addAll(qAndA.items.sortedBy { it.order })
                acc
            }
            .mapIndexed { index, faqItemOP ->
                faqItemOP.toEntity(event = event, order = index, language = event.defaultLanguage)
            }
        QAndATable.deleteDiff(
            eventId = event.id.value,
            externalIds = qanda.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return qanda
    }

    private fun upsertTeams(
        teams: List<TeamOP>,
        remoteUrls: Map<String, String>,
        event: EventEntity
    ): List<TeamEntity> {
        teams.groups(event)
        val members = teams
            .groupBy { it.team }
            .map { (_, members) ->
                members.mapIndexed { index, member ->
                    member.toEntity(event, index, remoteUrls[member.id])
                }
            }
            .flatten()
        TeamTable.deleteDiff(
            eventId = event.id.value,
            externalIds = members.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return members
    }

    private fun upsertTracks(tracks: List<TrackOP>, event: EventEntity): List<EventSessionTrackEntity> {
        val entities =
            tracks.mapIndexed { index, trackOP -> trackOP.toEventSessionTrackEntity(order = index, event = event) }
        EventSessionTracksTable.deleteDiff(
            eventId = event.id.value,
            externalIds = entities.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return entities
    }

    private fun upsertCategories(categories: List<CategoryOP>, event: EventEntity): List<CategoryEntity> {
        val entities = categories.mapIndexed { index, categoryOP -> categoryOP.toEntity(event = event, order = index) }
        CategoriesTable.deleteDiff(
            eventId = event.id.value,
            externalIds = entities.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return entities
    }

    private fun upsertFormats(formats: List<FormatOP>, event: EventEntity): List<FormatEntity> {
        val entities = formats.map { formatOP -> formatOP.toEntity(event = event) }
        FormatsTable.deleteDiff(
            eventId = event.id.value,
            externalIds = entities.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return entities
    }

    private fun upsertSpeakers(
        speakers: List<SpeakerOP>,
        remoteUrls: Map<String, String>,
        event: EventEntity
    ): List<SpeakerEntity> {
        val entities = speakers.map { speakerOP -> speakerOP.toEntity(event, remoteUrls[speakerOP.id]) }
        SpeakersTable.deleteDiff(
            eventId = event.id.value,
            externalIds = entities.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return entities
    }

    private fun upsertEventSessions(sessions: List<SessionOP>, event: EventEntity): List<EventSessionEntity> {
        val entities = sessions
            .filter { it.speakerIds.isEmpty() }
            .map { sessionOP -> sessionOP.toEventSessionEntity(event) }
        EventSessionsTable.deleteDiff(
            eventId = event.id.value,
            externalIds = entities.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return entities
    }

    private fun upsertSessions(sessions: List<SessionOP>, event: EventEntity): List<SessionEntity> {
        val entities = sessions
            .filter { it.speakerIds.isNotEmpty() }
            .map { sessionOP -> sessionOP.toSessionEntity(event) }
        SessionsTable.deleteDiff(
            eventId = event.id.value,
            externalIds = entities.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return entities
    }

    private fun upsertSchedules(sessions: List<SessionOP>, event: EventEntity): List<ScheduleEntity> {
        val entities = sessions.map { sessionOP -> sessionOP.toScheduleEntity(event) }
        SchedulesTable.deleteDiff(
            eventId = event.id.value,
            externalIds = entities.mapNotNull { it.externalId },
            provider = IntegrationProvider.OPENPLANNER
        )
        return entities
    }

    private suspend fun remoteUrls(openPlanner: OpenPlanner, eventSlug: String): Map<String, String> = coroutineScope {
        val teams = openPlanner.team
            .filter { it.photoUrl != null }
            .map { member ->
                async(dispatcher) {
                    val url = try {
                        val buffer = commonApi.fetchByteArray(member.photoUrl!!)
                        val bucketItem = teamStorage.saveTeamPicture(
                            eventId = eventSlug,
                            id = member.name.slug(),
                            content = buffer,
                            mimeType = member.photoUrl.mimeType
                        )
                        bucketItem.url
                    } catch (_: Throwable) {
                        member.photoUrl!!
                    }
                    Pair(member.id, url)
                }
            }
            .awaitAll()
        val speakers = openPlanner.speakers
            .filter { it.photoUrl != null }
            .map { speaker ->
                async(dispatcher) {
                    val url = try {
                        val buffer = commonApi.fetchByteArray(speaker.photoUrl!!)
                        val bucketItem = speakerStorage.saveProfile(
                            eventId = eventSlug,
                            id = speaker.name.slug(),
                            content = buffer,
                            mimeType = speaker.photoUrl.mimeType
                        )
                        bucketItem.url
                    } catch (_: Throwable) {
                        speaker.photoUrl!!
                    }
                    Pair(speaker.id, url)
                }
            }
            .awaitAll()
        (teams + speakers).associate { it }
    }
}
