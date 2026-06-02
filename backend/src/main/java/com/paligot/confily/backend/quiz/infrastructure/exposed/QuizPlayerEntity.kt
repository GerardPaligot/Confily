package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import java.util.UUID

class QuizPlayerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QuizPlayerEntity>(QuizPlayersTable) {
        fun findByDevice(eventId: UUID, deviceId: String): QuizPlayerEntity? = this.find {
            (QuizPlayersTable.eventId eq eventId) and (QuizPlayersTable.deviceId eq deviceId)
        }.firstOrNull()
    }

    var eventId by QuizPlayersTable.eventId
    var event by EventEntity.Companion referencedOn QuizPlayersTable.eventId
    var deviceId by QuizPlayersTable.deviceId
    var username by QuizPlayersTable.username
    var createdAt by QuizPlayersTable.createdAt
    var updatedAt by QuizPlayersTable.updatedAt
}
