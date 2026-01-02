package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakersTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object SessionSpeakersTable : Table("session_speakers") {
    val sessionId = reference("session_id", SessionsTable, onDelete = ReferenceOption.CASCADE)
    val speakerId = reference("speaker_id", SpeakersTable, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(sessionId, speakerId)

    init {
        index(isUnique = false, sessionId)
        index(isUnique = false, speakerId)
    }

    fun speakerIds(sessionId: UUID): List<UUID> = this.selectAll()
        .where { SessionSpeakersTable.sessionId eq sessionId }
        .map { it[speakerId].value }
}
