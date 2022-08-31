package org.gdglille.devfest.android.theme.vitamin.ui.components.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decathlon.vitamin.compose.tags.VitaminTags
import org.gdglille.devfest.models.CategoryUi

@Composable
fun DecorativeTag(
    category: CategoryUi,
    modifier: Modifier = Modifier
) {
    when (category.color) {
        "cobalt" -> VitaminTags.DecorativeCobalt(
            label = ""
        )
    }
}
