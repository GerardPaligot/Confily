package com.paligot.confily.backend.quiz.infrastructure.exposed

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object QuizSubmissionAnswersTable : UUIDTable("quiz_submission_answers") {
    val submissionId = reference("submission_id", QuizSubmissionsTable, onDelete = ReferenceOption.CASCADE)
    val questionId = reference("question_id", QuizQuestionsTable, onDelete = ReferenceOption.CASCADE)
    val chosenAnswerId = reference("chosen_answer_id", QuizAnswersTable, onDelete = ReferenceOption.CASCADE)
    val isCorrect = bool("is_correct")

    init {
        index(isUnique = false, submissionId)
    }
}
