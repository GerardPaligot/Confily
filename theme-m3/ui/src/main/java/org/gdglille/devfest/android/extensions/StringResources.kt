package org.gdglille.devfest.android.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Suppress("SpreadOperator")
@Composable
@ReadOnlyComposable
fun stringResource(@StringRes id: Int, formatArgs: List<String>): String {
    LocalConfiguration.current
    val resources = LocalContext.current.resources
    return resources.getString(id, *formatArgs.toTypedArray())
}
