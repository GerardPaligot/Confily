package org.gdglille.devfest.android.theme.vitamin.ui.components.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decathlon.vitamin.compose.tags.VitaminTags
import org.gdglille.devfest.android.ui.resources.R

@Composable
fun LevelTag(
    level: String,
    modifier: Modifier = Modifier
) {
    val text = when (level) {
        "advanced" -> stringResource(R.string.text_level_advanced)
        "intermediate" -> stringResource(R.string.text_level_intermediate)
        "beginner" -> stringResource(R.string.text_level_beginner)
        else -> level
    }
    VitaminTags.DecorativeGravel(
        label = text,
        modifier = modifier
    )
}
