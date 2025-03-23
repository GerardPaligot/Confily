package com.paligot.confily.backend.team

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.internals.CommonApi
import com.paligot.confily.backend.internals.mimeType
import com.paligot.confily.models.inputs.TeamMemberInput
import kotlinx.coroutines.coroutineScope

class TeamRepository(
    private val commonApi: CommonApi,
    private val eventDao: EventDao,
    private val teamDao: TeamDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId)
            ?: throw NotFoundException("Event with $eventId is not found")
        val orderMap = eventDb.teamGroups.associate { it.name to it.order }
        return@coroutineScope teamDao.getAll(eventId)
            .sortedBy { orderMap[it.teamName] ?: 0 }
            .groupBy { it.teamName }
            .filter { it.key != "" }
            .map { entry -> entry.key to entry.value.map { it.convertToModel() } }
            .associate { it }
    }

    suspend fun create(eventId: String, apiKey: String, teamInput: TeamMemberInput) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            if (event.teamGroups.find { it.name == teamInput.teamName } == null) {
                throw NotAcceptableException("Team group ${teamInput.teamName} is not allowed")
            }
            val order = teamDao.last(eventId)?.order
            val teamDb = teamInput.convertToDb(
                lastOrder = order,
                photoUrl = teamInput.photoUrl
            )
            val id = teamDao.createOrUpdate(eventId, teamDb)
            if (teamInput.photoUrl != null) {
                val avatar = commonApi.fetchByteArray(teamInput.photoUrl!!)
                val bucketItem = teamDao.saveTeamPicture(
                    eventId = eventId,
                    id = id,
                    content = avatar,
                    mimeType = teamInput.photoUrl!!.mimeType
                )
                teamDao.createOrUpdate(eventId, teamDb.copy(photoUrl = bucketItem.url))
            }
            eventDao.updateAgendaUpdatedAt(event)
            return@coroutineScope id
        }

    suspend fun update(
        eventId: String,
        apiKey: String,
        teamMemberId: String,
        teamMemberInput: TeamMemberInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        if (event.teamGroups.find { it.name == teamMemberInput.teamName } == null) {
            throw NotAcceptableException("Team group ${teamMemberInput.teamName} is not allowed")
        }
        val photoUrl = if (teamMemberInput.photoUrl != null) {
            val avatar = commonApi.fetchByteArray(teamMemberInput.photoUrl!!)
            val bucketItem = teamDao.saveTeamPicture(
                eventId = eventId,
                id = teamMemberId,
                content = avatar,
                mimeType = teamMemberInput.photoUrl!!.mimeType
            )
            bucketItem.url
        } else {
            null
        }
        val lastOrder = teamDao.last(eventId)?.order
        val teamDb = teamMemberInput.convertToDb(
            lastOrder = lastOrder,
            photoUrl = photoUrl,
            id = teamMemberId
        )
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope teamDao.createOrUpdate(eventId, teamDb)
    }
}
