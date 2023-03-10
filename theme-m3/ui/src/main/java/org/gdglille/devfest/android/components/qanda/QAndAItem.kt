package org.gdglille.devfest.android.components.qanda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.models.QuestionAndResponseUi

private const val ExpandedDegrees = 180f
private const val ClosedDegrees = 0f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QAndAItem(
    qAndA: QuestionAndResponseUi,
    onExpandedClicked: (QuestionAndResponseUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val degrees by animateFloatAsState(if (qAndA.expanded) ExpandedDegrees else ClosedDegrees)
    Card(
        onClick = { onExpandedClicked(qAndA) },
        modifier = modifier.semantics {
            stateDescription = if (qAndA.expanded) {
                qAndA.response
            } else {
                ""
            }
        },
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.placeholder(visible = isLoading)
            ) {
                Text(
                    text = qAndA.question,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.rotate(degrees = degrees)
                )
            }
            AnimatedVisibility(
                visible = qAndA.expanded,
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        RichTextThemeIntegration(
                            textStyle = { MaterialTheme.typography.bodyMedium },
                            ProvideTextStyle = null,
                            contentColor = { MaterialTheme.colorScheme.onSurface },
                            ProvideContentColor = null,
                        ) {
                            RichText {
                                Markdown(qAndA.response)
                            }
                        }
                        qAndA.actions.forEach {
                            Button(
                                onClick = { onLinkClicked(it.url) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.filledTonalButtonColors()
                            ) {
                                Text(text = it.label)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun QAndAItemPreview() {
    Conferences4HallTheme {
        QAndAItem(
            qAndA = QuestionAndResponseUi.fake,
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}

@Preview
@Composable
fun QAndAItemExpandedPreview() {
    Conferences4HallTheme {
        QAndAItem(
            qAndA = QuestionAndResponseUi.fake.copy(expanded = true),
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}
