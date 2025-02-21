package com.paligot.confily.mapper.list.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun SurfacePickFile(
    label: String,
    onPickFiles: (List<String>) -> Unit,
    modifier: Modifier
) {
    var color by remember { mutableStateOf(Color.Transparent) }
    val dragTarget = remember {
        object : DragAndDropTarget {
            override fun onEntered(event: DragAndDropEvent) {
                color = Color.DarkGray
            }

            override fun onEnded(event: DragAndDropEvent) {
                color = Color.Transparent
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                val dragData = event.dragData()
                if (dragData is DragData.FilesList) {
                    onPickFiles(dragData.readFiles())
                    return true
                }
                return false
            }
        }
    }
    Surface(
        modifier = modifier
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragTarget
            ),
        border = BorderStroke(2.dp, color),
        color = Color.LightGray,
        contentColor = Color.Gray,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = label)
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
