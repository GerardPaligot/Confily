package org.gdglille.devfest.android.theme.m3.style.schedules.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.toDp

@Composable
fun FilterSection(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = FilterSectionDefaults.titleColor,
    style: TextStyle = FilterSectionDefaults.style,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FilterSectionTokens.BetweenSpacing.toDp())
    ) {
        Text(text = title, style = style, color = color)
        this.content()
    }
}

@Preview
@Composable
private fun FilterSectionPreview() {
    Conferences4HallTheme {
        FilterSection(title = "Categories") {
            Text(text = "Filter content")
        }
    }
}
