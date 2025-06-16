package com.paligot.confily.backend.team.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.internals.helpers.mimeType
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.TeamFirestore
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.internals.infrastructure.storage.TeamStorage
import com.paligot.confily.backend.team.domain.TeamAdminRepository
import com.paligot.confily.models.inputs.TeamMemberInput

class TeamAdminRepositoryDefault(
    private val commonApi: CommonApi,
    private val eventDao: EventFirestore,
    private val teamFirestore: TeamFirestore,
    private val teamStorage: TeamStorage
) : TeamAdminRepository {
    override suspend fun create(eventId: String, teamInput: TeamMemberInput): String {
        val event = eventDao.get(eventId)
        if (event.teamGroups.find { it.name == teamInput.teamName } == null) {
            throw NotAcceptableException("Team group ${teamInput.teamName} is not allowed")
        }
        val order = teamFirestore.last(eventId)?.order
        val teamDb = teamInput.convertToEntity(
            lastOrder = order,
            photoUrl = teamInput.photoUrl
        )
        val id = teamFirestore.createOrUpdate(eventId, teamDb)
        if (teamInput.photoUrl != null) {
            val avatar = commonApi.fetchByteArray(teamInput.photoUrl!!)
            val bucketItem = teamStorage.saveTeamPicture(
                eventId = eventId,
                id = id,
                content = avatar,
                mimeType = teamInput.photoUrl!!.mimeType
            )
            teamFirestore.createOrUpdate(eventId, teamDb.copy(photoUrl = bucketItem.url))
        }
        eventDao.updateAgendaUpdatedAt(event)
        return id
    }

    override suspend fun update(eventId: String, teamMemberId: String, input: TeamMemberInput): String {
        val event = eventDao.get(eventId)
        if (event.teamGroups.find { it.name == input.teamName } == null) {
            throw NotAcceptableException("Team group ${input.teamName} is not allowed")
        }
        val photoUrl = if (input.photoUrl != null) {
            val avatar = commonApi.fetchByteArray(input.photoUrl!!)
            val bucketItem = teamStorage.saveTeamPicture(
                eventId = eventId,
                id = teamMemberId,
                content = avatar,
                mimeType = input.photoUrl!!.mimeType
            )
            bucketItem.url
        } else {
            null
        }
        val lastOrder = teamFirestore.last(eventId)?.order
        val teamDb = input.convertToEntity(
            lastOrder = lastOrder,
            photoUrl = photoUrl,
            id = teamMemberId
        )
        eventDao.updateAgendaUpdatedAt(event)
        return teamFirestore.createOrUpdate(eventId, teamDb)
    }
}
