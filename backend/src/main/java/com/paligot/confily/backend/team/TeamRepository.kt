package com.paligot.confily.backend.team

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.mimeType
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.models.inputs.TeamMemberInput
import kotlinx.coroutines.coroutineScope

class TeamRepository(
    private val commonApi: CommonApi,
    private val eventDao: EventFirestore,
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

    suspend fun create(eventId: String, teamInput: TeamMemberInput) = coroutineScope {
        val event = eventDao.get(eventId)
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
        teamMemberId: String,
        input: TeamMemberInput
    ) = coroutineScope {
        val event = eventDao.get(eventId)
        if (event.teamGroups.find { it.name == input.teamName } == null) {
            throw NotAcceptableException("Team group ${input.teamName} is not allowed")
        }
        val photoUrl = if (input.photoUrl != null) {
            val avatar = commonApi.fetchByteArray(input.photoUrl!!)
            val bucketItem = teamDao.saveTeamPicture(
                eventId = eventId,
                id = teamMemberId,
                content = avatar,
                mimeType = input.photoUrl!!.mimeType
            )
            bucketItem.url
        } else {
            null
        }
        val lastOrder = teamDao.last(eventId)?.order
        val teamDb = input.convertToDb(
            lastOrder = lastOrder,
            photoUrl = photoUrl,
            id = teamMemberId
        )
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope teamDao.createOrUpdate(eventId, teamDb)
    }
}
