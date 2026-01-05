package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.categories.infrastructure.exposed.CategoriesTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object SessionCategoriesTable : Table("session_categories") {
    val sessionId = reference("session_id", SessionsTable, onDelete = ReferenceOption.CASCADE)
    val categoryId = reference("category_id", CategoriesTable, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(sessionId, categoryId)

    init {
        index(isUnique = false, sessionId)
        index(isUnique = false, categoryId)
    }

    fun categoryIds(sessionId: UUID): List<UUID> = this
        .selectAll()
        .where { SessionCategoriesTable.sessionId eq sessionId }
        .map { it[categoryId].value }

    fun firstCategoryId(sessionId: UUID) = this
        .innerJoin(CategoriesTable)
        .selectAll()
        .where { SessionCategoriesTable.sessionId eq sessionId }
        .firstOrNull()
        ?.let { row -> row[CategoriesTable.id] }
}
