package com.paligot.confily.quiz.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.quiz_action_start
import com.paligot.confily.resources.quiz_code_label
import com.paligot.confily.resources.quiz_enter_code_title
import com.paligot.confily.resources.quiz_name_label
import com.paligot.confily.resources.quiz_score_label
import com.paligot.confily.resources.screen_quiz
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.appbars.TopAppBar
import com.paligot.confily.style.theme.toDp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHomePane(
    username: String?,
    score: Int?,
    onStart: (code: String, name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var code by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = stringResource(Resource.string.screen_quiz))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(SpacingTokens.LargeSpacing.toDp()),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.LargeSpacing.toDp())
        ) {
            if (score != null) {
                Text(text = stringResource(Resource.string.quiz_score_label, score))
            }
            Text(text = stringResource(Resource.string.quiz_enter_code_title))
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(Resource.string.quiz_code_label)) }
            )
            if (username == null) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Resource.string.quiz_name_label)) }
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onStart(code, username ?: name) },
                enabled = code.isNotBlank() && (username != null || name.isNotBlank())
            ) {
                Text(stringResource(Resource.string.quiz_action_start))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun QuizHomePanePreview() {
    ConfilyTheme {
        QuizHomePane(
            username = null,
            score = null,
            onStart = { _, _ -> }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun QuizHomePaneWithScorePreview() {
    ConfilyTheme {
        QuizHomePane(
            username = "Alice",
            score = 3,
            onStart = { _, _ -> }
        )
    }
}
