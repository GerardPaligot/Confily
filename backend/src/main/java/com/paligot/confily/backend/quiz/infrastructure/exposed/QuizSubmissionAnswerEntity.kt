package com.paligot.confily.backend.quiz.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class QuizSubmissionAnswerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QuizSubmissionAnswerEntity>(QuizSubmissionAnswersTable)

    var submissionId by QuizSubmissionAnswersTable.submissionId
    var submission by QuizSubmissionEntity referencedOn QuizSubmissionAnswersTable.submissionId
    var questionId by QuizSubmissionAnswersTable.questionId
    var chosenAnswerId by QuizSubmissionAnswersTable.chosenAnswerId
    var isCorrect by QuizSubmissionAnswersTable.isCorrect
}
