package com.paligot.confily.core.models.factory

import com.paligot.confily.models.Session

fun Session.Talk.Companion.builder(): TalkBuilder = TalkBuilder()

class TalkBuilder {
    private var id: String = ""
    private var title: String = ""
    private var level: String? = null
    private var abstract: String = ""
    private var categoryId: String = ""
    private var formatId: String = ""
    private var language: String = ""
    private var speakers: List<String> = emptyList()

    fun id(id: String) = apply { this.id = id }
    fun title(title: String) = apply { this.title = title }
    fun level(level: String) = apply { this.level = level }
    fun abstract(abstract: String) = apply { this.abstract = abstract }
    fun categoryId(categoryId: String) = apply { this.categoryId = categoryId }
    fun formatId(formatId: String) = apply { this.formatId = formatId }
    fun language(language: String) = apply { this.language = language }
    fun speakers(speakers: List<String>) = apply { this.speakers = speakers }

    fun build(): Session.Talk = Session.Talk(
        id = id,
        title = title,
        abstract = abstract,
        level = level,
        categoryId = categoryId,
        formatId = formatId,
        language = language,
        speakers = speakers,
        linkSlides = null,
        linkReplay = null,
        openFeedback = null
    )
}
