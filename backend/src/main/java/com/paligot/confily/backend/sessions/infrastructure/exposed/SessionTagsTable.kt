package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.tags.infrastructure.exposed.TagsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

// Junction table for Session-Tag many-to-many relationship
object SessionTagsTable : Table("session_tags") {
    val sessionId = reference("session_id", SessionsTable, onDelete = ReferenceOption.CASCADE)
    val tagId = reference("tag_id", TagsTable, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(sessionId, tagId)

    init {
        index(isUnique = false, sessionId)
        index(isUnique = false, tagId)
    }

    fun tagIds(sessionId: UUID): List<UUID> = SessionTagsTable
        .selectAll()
        .where { SessionTagsTable.sessionId eq sessionId }
        .map { it[tagId].value }
}
