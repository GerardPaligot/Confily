package com.paligot.confily.quiz.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.quiz.ui.models.QuizAnsweredQuestionUi
import com.paligot.confily.quiz.ui.models.QuizResultUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.quiz_action_back_to_code
import com.paligot.confily.resources.quiz_result_score
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
fun QuizResultPane(
    result: QuizResultUi,
    onDone: () -> Unit,
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
                onClick = onDone
            ) {
                Text(stringResource(Resource.string.quiz_action_back_to_code))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.LargeSpacing.toDp()),
            modifier = Modifier.padding(horizontal = SpacingTokens.LargeSpacing.toDp())
        ) {
            item {
                Text(
                    text = stringResource(
                        Resource.string.quiz_result_score,
                        result.correctCount,
                        result.totalCount
                    ),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            items(result.perQuestion) { answered ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (answered.isCorrect) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        contentDescription = null,
                        tint = if (answered.isCorrect) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        }
                    )
                    Text(text = answered.question)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun QuizResultPanePreview() {
    ConfilyTheme {
        QuizResultPane(
            result = QuizResultUi(
                correctCount = 1,
                totalCount = 2,
                perQuestion = persistentListOf(
                    QuizAnsweredQuestionUi(
                        questionId = "q1",
                        question = "What is Compose?",
                        chosenAnswerId = "a1",
                        correctAnswerId = "a1",
                        isCorrect = true
                    ),
                    QuizAnsweredQuestionUi(
                        questionId = "q2",
                        question = "What language is used for KMP?",
                        chosenAnswerId = "b2",
                        correctAnswerId = "b1",
                        isCorrect = false
                    )
                )
            ),
            onDone = {}
        )
    }
}
