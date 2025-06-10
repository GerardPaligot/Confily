package com.paligot.confily.backend.qanda.application

import com.paligot.confily.backend.internals.infrastructure.firestore.AcronymEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAActionEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.QAndAEntity
import com.paligot.confily.models.Acronym
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.QuestionAndResponseAction
import com.paligot.confily.models.inputs.AcronymInput
import com.paligot.confily.models.inputs.QAndAActionInput
import com.paligot.confily.models.inputs.QAndAInput

fun AcronymInput.convertToEntity() = AcronymEntity(
    key = key,
    value = value
)

fun QAndAActionInput.convertToEntity(order: Int) = QAndAActionEntity(
    order = order,
    label = label,
    url = url
)

fun QAndAInput.convertToEntity(id: String? = null) = QAndAEntity(
    id = id,
    order = order,
    language = language,
    question = question,
    response = response,
    actions = actions.mapIndexed { index, it -> it.convertToEntity(index) },
    acronyms = acronyms.map(AcronymInput::convertToEntity)
)

fun AcronymEntity.convertToModel() = Acronym(
    key = key,
    value = value
)

fun QAndAActionEntity.convertToModel() = QuestionAndResponseAction(
    order = order,
    label = label,
    url = url
)

fun QAndAEntity.convertToModel() = QuestionAndResponse(
    id = id ?: "",
    order = order,
    question = question,
    response = response,
    actions = this.actions.map(QAndAActionEntity::convertToModel),
    acronyms = this.acronyms.map(AcronymEntity::convertToModel)
)
