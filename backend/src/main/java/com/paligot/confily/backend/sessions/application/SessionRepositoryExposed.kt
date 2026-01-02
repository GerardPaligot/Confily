package com.paligot.confily.backend.sessions.application

import com.paligot.confily.backend.sessions.domain.SessionRepository
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionCategoriesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionSpeakersTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionTagsTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionsTable
import com.paligot.confily.models.Session
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class SessionRepositoryExposed(private val database: Database) : SessionRepository {

    override suspend fun list(eventId: String): List<Session> = transaction(db = database) {
        val uuid = UUID.fromString(eventId)
        SessionEntity
            .findByEvent(uuid)
            .orderBy(SessionsTable.createdAt to SortOrder.ASC)
            .map { session ->
                val sessionId = session.id.value
                session.toModel(
                    speakerIds = SessionSpeakersTable.speakerIds(sessionId).map { it.toString() },
                    categoryIds = SessionCategoriesTable.categoryIds(sessionId).map { it.toString() },
                    tagIds = SessionTagsTable.tagIds(sessionId).map { it.toString() }
                )
            }
    }

    private fun SessionEntity.toModel(
        speakerIds: List<String>,
        categoryIds: List<String>,
        tagIds: List<String>
    ): Session.Talk = Session.Talk(
        id = this.id.value.toString(),
        title = this.title,
        level = this.level,
        abstract = this.description ?: "",
        categoryId = categoryIds.firstOrNull() ?: "",
        tagIds = tagIds,
        formatId = this.formatId?.value?.toString() ?: "",
        language = this.language,
        speakers = speakerIds,
        linkSlides = this.linkSlides,
        linkReplay = this.linkReplay,
        openFeedback = null
    )
}
