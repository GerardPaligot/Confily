package org.gdglille.devfest.theme.m3.style.schedules.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

@Immutable
data class ScheduleCardColors internal constructor(
    val titleColor: Color,
    val favoriteColor: Color,
    val unFavoriteColor: Color
) {
    @Composable
    internal fun favIconColor(isFavorite: Boolean): State<Color> {
        return rememberUpdatedState(if (isFavorite) favoriteColor else unFavoriteColor)
    }
}
