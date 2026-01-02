package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAAcronymsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionEntity
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAActionsTable
import com.paligot.confily.backend.qanda.infrastructure.exposed.QAndAEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FaqItemOP
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

fun FaqItemOP.toEntity(event: EventEntity, order: Int, language: String): QAndAEntity {
    val links = Regex("\\[(.*?)\\]\\((.*?)\\)").findAll(answer)
    var response = answer
    links.forEach {
        response = answer.replaceRange(it.range, it.groupValues[1])
    }
    val acronyms = Regex("\n\\*\\[(.*?)\\]: (.*)").findAll(answer)
    acronyms.forEach {
        response = answer.replaceRange(it.range, "")
    }
    val entity = QAndAEntity
        .findByExternalId(event.id.value, this.id)
        ?.let { entity ->
            entity.displayOrder = order
            entity.language = language
            entity.question = this.question
            entity.response = response.replace("\n$".toRegex(), "")
            entity.updatedAt = Clock.System.now()
            entity
        }
        ?: QAndAEntity.new {
            this.event = event
            this.displayOrder = order
            this.language = language
            this.question = this@toEntity.question
            this.response = response.replace("\n$".toRegex(), "")
            this.externalId = this@toEntity.id
        }

    // Delete old actions and acronyms
    QAndAActionsTable.deleteWhere { QAndAActionsTable.qandaId eq entity.id.value }
    QAndAAcronymsTable.deleteWhere { QAndAAcronymsTable.qandaId eq entity.id.value }

    links.forEachIndexed { index, matchResult ->
        val (text, url) = matchResult.destructured
        QAndAActionEntity.new {
            this.qanda = entity
            this.displayOrder = index
            this.label = text
            this.url = url
        }
    }

    acronyms.forEach { matchResult ->
        val (acronym, definition) = matchResult.destructured
        QAndAAcronymEntity.new {
            this.qanda = entity
            this.key = acronym
            this.value = definition
        }
    }

    return entity
}
