package org.gdglille.devfest.android.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Twitter
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.ui.R
import org.gdglille.devfest.models.PartnerItemUi

@Composable
fun SocialCard(
    title: String,
    text: String,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    shape: Shape = MaterialTheme.shapes.small
) {
    Card(
        modifier = modifier.wrapContentHeight().fillMaxWidth(),
        colors = CardDefaults.cardColors(contentColor = color),
        shape = shape,
    ) {
        CompositionLocalProvider(LocalTextStyle provides textStyle) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )
                }
                RichTextThemeIntegration(
                    textStyle = { textStyle },
                    ProvideTextStyle = null,
                    contentColor = { color },
                    ProvideContentColor = null,
                ) {
                    RichText {
                        Markdown(text)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SocialCardPreview() {
    Conferences4HallTheme {
        SocialCard(
            title = stringResource(R.string.title_communication_twitter),
            text = PartnerItemUi.fake.twitterMessage!!,
            imageVector = FontAwesomeIcons.Brands.Twitter
        )
    }
}
