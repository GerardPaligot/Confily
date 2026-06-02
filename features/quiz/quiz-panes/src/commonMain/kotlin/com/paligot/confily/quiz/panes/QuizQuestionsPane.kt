package com.paligot.confily.quiz.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.quiz.ui.AnswerOptionItem
import com.paligot.confily.quiz.ui.models.QuizAnswerOptionUi
import com.paligot.confily.quiz.ui.models.QuizQuestionUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.quiz_action_submit
import com.paligot.confily.resources.screen_quiz
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.appbars.TopAppBar
import com.paligot.confily.style.theme.toDp
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizQuestionsPane(
    questions: List<QuizQuestionUi>,
    selections: Map<String, String>,
    onSelect: (questionId: String, answerId: String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = stringResource(Resource.string.screen_quiz))
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingTokens.LargeSpacing.toDp()),
                enabled = questions.all { selections.containsKey(it.id) },
                onClick = onSubmit
            ) {
                Text(stringResource(Resource.string.quiz_action_submit))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.LargeSpacing.toDp()),
            modifier = Modifier.padding(horizontal = SpacingTokens.LargeSpacing.toDp())
        ) {
            items(questions) { question ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp())
                ) {
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.titleMedium
                    )
                    question.options.forEach { option ->
                        AnswerOptionItem(
                            label = option.label,
                            selected = selections[question.id] == option.id,
                            onClick = { onSelect(question.id, option.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun QuizQuestionsPanePreview() {
    ConfilyTheme {
        QuizQuestionsPane(
            questions = persistentListOf(
                QuizQuestionUi(
                    id = "q1",
                    question = "What is Compose?",
                    options = persistentListOf(
                        QuizAnswerOptionUi(id = "a1", label = "A UI toolkit"),
                        QuizAnswerOptionUi(id = "a2", label = "A database"),
                        QuizAnswerOptionUi(id = "a3", label = "A build tool")
                    )
                ),
                QuizQuestionUi(
                    id = "q2",
                    question = "What language is used for KMP?",
                    options = persistentListOf(
                        QuizAnswerOptionUi(id = "b1", label = "Kotlin"),
                        QuizAnswerOptionUi(id = "b2", label = "Java"),
                        QuizAnswerOptionUi(id = "b3", label = "Swift")
                    )
                )
            ),
            selections = mapOf("q1" to "a1"),
            onSelect = { _, _ -> },
            onSubmit = {}
        )
    }
}
