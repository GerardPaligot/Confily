package com.paligot.confily.backend.tags.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.tags.domain.TagAdminRepository
import com.paligot.confily.backend.tags.infrastructure.exposed.TagEntity
import com.paligot.confily.models.inputs.TagInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TagAdminRepositoryExposed(private val database: Database) : TagAdminRepository {

    override suspend fun create(eventId: String, input: TagInput): String = transaction(db = database) {
        val event = EventEntity[UUID.fromString(eventId)]
        val tag = TagEntity.new {
            this.event = event
            this.name = input.name
        }
        tag.id.value.toString()
    }

    override suspend fun update(eventId: String, tagId: String, input: TagInput): String = transaction(db = database) {
        val event = EventEntity[UUID.fromString(eventId)]
        val uuid = UUID.fromString(tagId)
        val tag = TagEntity
            .findById(event.id.value, uuid)
            ?: throw NotFoundException("Tag not found: $tagId")
        tag.name = input.name
        tag.id.value.toString()
    }
}
