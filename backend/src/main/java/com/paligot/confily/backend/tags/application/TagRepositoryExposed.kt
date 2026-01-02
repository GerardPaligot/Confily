package com.paligot.confily.backend.tags.application

import com.paligot.confily.backend.tags.domain.TagRepository
import com.paligot.confily.backend.tags.infrastructure.exposed.TagEntity
import com.paligot.confily.backend.tags.infrastructure.exposed.TagsTable
import com.paligot.confily.models.Tag
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TagRepositoryExposed(private val database: Database) : TagRepository {

    override suspend fun list(eventId: String): List<Tag> = transaction(db = database) {
        val uuid = UUID.fromString(eventId)
        TagEntity.findByEvent(uuid)
            .orderBy(TagsTable.name to SortOrder.ASC)
            .map { it.toModel() }
    }

    private fun TagEntity.toModel(): Tag = Tag(
        id = this.id.value.toString(),
        name = this.name
    )
}
