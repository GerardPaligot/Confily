package org.gdglille.devfest.android.theme.vitamin.ui.components.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.decathlon.vitamin.compose.tags.VitaminTags
import org.gdglille.devfest.models.CategoryUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun DecorativeTag(
    category: CategoryUi,
    modifier: Modifier = Modifier
) {
    when (category.color) {
        "amethyst" -> VitaminTags.DecorativeAmethyst(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "cobalt" -> VitaminTags.DecorativeCobalt(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "brick" -> VitaminTags.DecorativeBrick(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "emerald" -> VitaminTags.DecorativeEmerald(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "jade" -> VitaminTags.DecorativeJade(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "saffron" -> VitaminTags.DecorativeSaffron(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "gold" -> VitaminTags.DecorativeGold(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "gravel" -> VitaminTags.DecorativeGravel(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )

        "default" -> VitaminTags.DecorativeGravel(
            label = category.name,
            iconPainter = category.icon?.iconPainter(),
            modifier = modifier
        )
    }
}

@Composable
private fun String.iconPainter(): Painter = painterResource(getResourceId(this))

@Composable
private fun getResourceId(icon: String): Int {
    if (icon == "default") {
        return RVitamin.drawable.ic_vtmn_function_line
    }
    val context = LocalContext.current
    val drawableName = "ic_vtmn_${icon.replace("-", "_")}_line"
    val identifier = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    return if (identifier > 0) identifier else RVitamin.drawable.ic_vtmn_function_line
}
