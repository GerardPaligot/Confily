package org.gdglille.devfest.android.theme.vitamin.ui.components.qanda

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.buttons.IconSide
import com.decathlon.vitamin.compose.buttons.VitaminButtons
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.models.QuestionAndResponseUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

private const val ExpandedDegrees = 180f
private const val ClosedDegrees = 0f

@OptIn(ExperimentalMaterialApi::class)
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
        shape = RectangleShape,
        backgroundColor = VitaminTheme.colors.vtmnBackgroundPrimary
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
                    style = VitaminTheme.typography.body2,
                    color = VitaminTheme.colors.vtmnContentPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(RVitamin.drawable.ic_vtmn_arrow_down_line),
                    contentDescription = null,
                    modifier = Modifier.rotate(degrees = degrees)
                )
            }
            AnimatedVisibility(
                visible = qAndA.expanded,
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        RichTextThemeIntegration(
                            textStyle = { VitaminTheme.typography.body2 },
                            ProvideTextStyle = null,
                            contentColor = { VitaminTheme.colors.vtmnContentSecondary },
                            ProvideContentColor = null,
                        ) {
                            RichText {
                                Markdown(qAndA.response)
                            }
                        }
                        qAndA.actions.forEach {
                            VitaminButtons.Tertiary(
                                text = it.label,
                                icon = painterResource(RVitamin.drawable.ic_vtmn_link_line),
                                iconSide = IconSide.LEFT,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onLinkClicked(it.url)
                                }
                            )
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
