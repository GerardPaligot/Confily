package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.toModel
import com.paligot.confily.models.Category
import com.paligot.confily.models.Session
import com.paligot.confily.models.Talk
import com.paligot.confily.models.TalkV3

fun SessionEntity.toModel(): Talk {
    val sessionUuid = this.id.value
    val categoryEntity = SessionCategoriesTable.firstCategoryId(sessionUuid)
        ?.let { CategoryEntity[it] }
    val speakers = SessionSpeakersTable.speakers(sessionUuid)
        .map { SpeakerEntity[it] }
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

fun SessionEntity.toModelV3(): TalkV3 {
    val sessionUuid = this.id.value
    val categoryEntity = SessionCategoriesTable.firstCategoryId(sessionUuid)
        ?.let { CategoryEntity[it] }
    val speakers = SessionSpeakersTable.speakers(sessionUuid)
        .map { SpeakerEntity[it] }
    return TalkV3(
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
        openFeedback = null
    )
}

fun SessionEntity.toSessionModel(): Session.Talk {
    val sessionUuid = this.id.value
    val categoryEntity = SessionCategoriesTable.firstCategoryId(sessionUuid)
        ?.let { CategoryEntity[it] }
    val speakers = SessionSpeakersTable.speakers(sessionUuid)
        .map { SpeakerEntity[it] }
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
