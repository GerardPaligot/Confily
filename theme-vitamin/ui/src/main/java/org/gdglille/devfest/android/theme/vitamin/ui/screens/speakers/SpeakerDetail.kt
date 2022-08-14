package org.gdglille.devfest.android.theme.vitamin.ui.screens.speakers

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakerHeader
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakerSocialSection
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SpeakerDetail(
    speaker: SpeakerUi,
    modifier: Modifier = Modifier,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        content = {
            LazyColumn {
                item {
                    SpeakerHeader(
                        url = speaker.url,
                        onBackClicked = onBackClicked
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    SpeakerSocialSection(
                        speaker = speaker,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onLinkClicked = onLinkClicked
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    RichTextThemeIntegration(
                        textStyle = { VitaminTheme.typography.body3 },
                        ProvideTextStyle = null,
                        contentColor = { VitaminTheme.colors.vtmnContentPrimary },
                        ProvideContentColor = null,
                    ) {
                        RichText(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Markdown(speaker.bio)
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun SpeakerListPreview() {
    Conferences4HallTheme {
        SpeakerDetail(
            speaker = SpeakerUi.fake,
            onLinkClicked = {}
        ) {}
    }
}
