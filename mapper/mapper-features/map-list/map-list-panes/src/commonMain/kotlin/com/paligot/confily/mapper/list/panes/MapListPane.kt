package com.paligot.confily.mapper.list.panes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.mapper.list.ui.models.MapItemUi
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MapListPane(
    mapItems: ImmutableList<MapItemUi>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Scaffold(modifier = modifier) {
        LazyColumn(contentPadding = it) {
            items(mapItems) {
                ListItem(
                    trailingContent = {
                        Image(
                            painter = rememberAsyncImagePainter(model = it.url),
                            contentDescription = null,
                            modifier = Modifier.width(40.dp).aspectRatio(1f)
                        )
                    },
                    headlineContent = { Text(text = it.name) },
                    modifier = Modifier.clickable { onClick(it.id) }
                )
            }
        }
    }
}
