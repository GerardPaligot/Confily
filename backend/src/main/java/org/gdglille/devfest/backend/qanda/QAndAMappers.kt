package org.gdglille.devfest.backend.qanda

import org.gdglille.devfest.models.Acronym
import org.gdglille.devfest.models.QuestionAndResponse
import org.gdglille.devfest.models.QuestionAndResponseAction
import org.gdglille.devfest.models.inputs.AcronymInput
import org.gdglille.devfest.models.inputs.QAndAActionInput
import org.gdglille.devfest.models.inputs.QAndAInput

fun AcronymInput.convertToDb() = AcronymDb(
    key = key,
    value = value
)

fun QAndAActionInput.convertToDb(order: Int) = QAndAActionDb(
    order = order,
    label = label,
    url = url
)

fun QAndAInput.convertToDb(id: String? = null) = QAndADb(
    id = id,
    order = order,
    language = language,
    question = question,
    response = response,
    actions = actions.mapIndexed { index, it -> it.convertToDb(index) },
    acronyms = acronyms.map { it.convertToDb() }
)

fun AcronymDb.convertToModel() = Acronym(
    key = key,
    value = value
)

fun QAndAActionDb.convertToModel() = QuestionAndResponseAction(
    order = order,
    label = label,
    url = url
)

fun QAndADb.convertToModel() = QuestionAndResponse(
    id = id ?: "",
    order = order,
    question = question,
    response = response,
    actions = this.actions.map { it.convertToModel() },
    acronyms = this.acronyms.map { it.convertToModel() }
)
