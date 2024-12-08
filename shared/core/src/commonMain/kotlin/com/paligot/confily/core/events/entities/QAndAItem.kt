package com.paligot.confily.core.events.entities

import com.paligot.confily.models.ui.QuestionAndResponseActionUi
import com.paligot.confily.models.ui.QuestionAndResponseUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("QAndAItemEntity")
class QAndAItem(val question: String, val answer: String, val actions: List<QAndAAction>)

@ObjCName("QAndAActionEntity")
class QAndAAction(val label: String, val url: String)

fun QAndAItem.mapToQuestionAndResponseUi(selected: QuestionAndResponseUi?): QuestionAndResponseUi =
    QuestionAndResponseUi(
        question = question,
        response = answer,
        expanded = question == selected?.question,
        actions = actions.map { it.mapToQuestionAndResponseActionUi() }.toImmutableList()
    )

fun QAndAAction.mapToQuestionAndResponseActionUi(): QuestionAndResponseActionUi =
    QuestionAndResponseActionUi(label = label, url = url)
