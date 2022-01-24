package com.paligot.conferences.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val imageVector: ImageVector) {
    object Agenda : Screen(route = "agenda", title = "Agenda", imageVector = Icons.Filled.Event)
    object Event : Screen(route = "event", title = "Event Info", imageVector = Icons.Filled.LocalActivity)
}
