package org.gdglille.devfest.theme.m3.style.schedules

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MoreTime
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

private const val ShortSchedule = 20
private const val MiddleSchedule = 60

fun Int.findTimeImageVector(): ImageVector {
    return when {
        this <= ShortSchedule -> Icons.Outlined.Bolt
        this <= MiddleSchedule -> Icons.Outlined.AccessTime
        else -> Icons.Outlined.MoreTime
    }
}

@Composable
fun String.findCategoryImageVector(): ImageVector? = when (this) {
    "database" -> Icons.Outlined.DataObject
    "computer" -> Icons.Outlined.Computer
    "public" -> Icons.Outlined.Public
    "cloud" -> Icons.Outlined.Cloud
    "smartphone" -> Icons.Outlined.Smartphone
    "lock" -> Icons.Outlined.Lock
    "autorenew" -> Icons.Outlined.Autorenew
    "psychology" -> Icons.Outlined.Psychology
    "draw" -> Icons.Outlined.Draw
    "language" -> Icons.Outlined.Language
    else -> null
}
