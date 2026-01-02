package com.paligot.confily.backend.qanda.infrastructure.exposed

import com.paligot.confily.models.Acronym
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.QuestionAndResponseAction

fun QAndAEntity.toModel(
    actions: List<QuestionAndResponseAction>,
    acronyms: List<Acronym>
): QuestionAndResponse = QuestionAndResponse(
    id = this.id.value.toString(),
    order = this.displayOrder,
    question = this.question,
    response = this.response,
    actions = actions,
    acronyms = acronyms
)

fun QAndAActionEntity.toModel(): QuestionAndResponseAction = QuestionAndResponseAction(
    order = this.displayOrder,
    label = this.label,
    url = this.url
)

fun QAndAAcronymEntity.toModel(): Acronym = Acronym(
    key = this.key,
    value = this.value
)
