package com.paligot.confily.style.schedules.card

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalLayoutApi::class)
@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SmallScheduleCardPreview() {
    ConfilyTheme {
        SmallScheduleCard(
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            speakersUrls = persistentListOf("", ""),
            speakersLabel = "John Doe and Jeanne Doe",
            contentDescription = null,
            onClick = {},
            onFavoriteClick = {}
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ScheduleCardPreview() {
    ConfilyTheme {
        MediumScheduleCard(
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            speakersUrls = persistentListOf("", ""),
            speakersLabel = "John Doe and Jeanne Doe",
            contentDescription = null,
            onClick = {},
            onFavoriteClick = {}
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun ScheduleCardFavoritePreview() {
    ConfilyTheme {
        MediumScheduleCard(
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            speakersUrls = persistentListOf("", ""),
            speakersLabel = "John Doe and Jeanne Doe",
            isFavorite = true,
            contentDescription = null,
            onClick = {},
            onFavoriteClick = {}
        )
    }
}
