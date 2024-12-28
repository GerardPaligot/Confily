package com.paligot.confily.backend.team

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
        return@coroutineScope teamDao.getAll(eventId).map { it.convertToModel() }
    }

    suspend fun create(eventId: String, apiKey: String, teamInput: TeamMemberInput) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            val teamDb = teamInput.convertToDb(photoUrl = teamInput.photoUrl)
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

    suspend fun get(eventId: String, teamMemberId: String) = coroutineScope {
        val teamMember = teamDao.get(eventId, teamMemberId)
            ?: throw NotFoundException("Team member with $teamMemberId is not found")
        return@coroutineScope teamMember.convertToModel()
    }

    suspend fun update(
        eventId: String,
        apiKey: String,
        teamMemberId: String,
        teamMemberInput: TeamMemberInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
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
        val speakerDb = teamMemberInput.convertToDb(photoUrl = photoUrl, teamMemberId)
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope teamDao.createOrUpdate(eventId, speakerDb)
    }
}
