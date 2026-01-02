package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.categories.infrastructure.exposed.CategoriesTable
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.toModel
import com.paligot.confily.models.Category
import com.paligot.confily.models.Session
import com.paligot.confily.models.Talk
import org.jetbrains.exposed.sql.selectAll

fun SessionEntity.toModel(): Talk {
    val sessionUuid = this.id.value
    val categoryEntity = SessionCategoriesTable
        .selectAll()
        .where { SessionCategoriesTable.sessionId eq sessionUuid }
        .firstOrNull()
        ?.let { row -> CategoryEntity[row[CategoriesTable.id]] }
    val speakers = SessionSpeakersTable
        .selectAll()
        .where { SessionSpeakersTable.sessionId eq sessionUuid }
        .map { SpeakerEntity[it[SessionSpeakersTable.speakerId]] }
    return Talk(
        id = this.id.value.toString(),
        title = this.title,
        level = this.level,
        abstract = this.description ?: "",
        category = categoryEntity?.name ?: "",
        categoryStyle = categoryEntity?.let {
            Category(
                id = it.id.value.toString(),
                name = it.name,
                color = it.color ?: "",
                icon = it.icon ?: ""
            )
        },
        format = this.format?.name ?: "",
        language = this.language,
        speakers = speakers.map { it.toModel() },
        linkSlides = this.linkSlides,
        linkReplay = this.linkReplay,
        openFeedback = null
    )
}

fun SessionEntity.toSessionModel(): Session.Talk {
    val sessionUuid = this.id.value
    val categoryEntity = SessionCategoriesTable
        .selectAll()
        .where { SessionCategoriesTable.sessionId eq sessionUuid }
        .firstOrNull()
        ?.let { row -> CategoryEntity[row[SessionCategoriesTable.categoryId]] }
    val speakers = SessionSpeakersTable
        .selectAll()
        .where { SessionSpeakersTable.sessionId eq sessionUuid }
        .map { SpeakerEntity[it[SessionSpeakersTable.speakerId]] }
    return Session.Talk(
        id = this.id.value.toString(),
        title = this.title,
        level = this.level,
        abstract = this.description ?: "",
        categoryId = categoryEntity?.id?.value.toString(),
        formatId = this.format?.id?.value.toString(),
        language = this.language,
        speakers = speakers.map { it.id.value.toString() },
        linkSlides = this.linkSlides,
        linkReplay = this.linkReplay,
        openFeedback = null,
        tagIds = emptyList()
    )
}
