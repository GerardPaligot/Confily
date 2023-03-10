package org.gdglille.devfest.android.components.structure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Dropdown(
    anchor: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: State<Boolean> = remember { mutableStateOf(false) },
    children: @Composable ColumnScope.() -> Unit
) {
    Box {
        anchor()
        DropdownMenu(
            modifier = modifier,
            expanded = expanded.value,
            onDismissRequest = onDismissRequest,
            content = children
        )
    }
}
