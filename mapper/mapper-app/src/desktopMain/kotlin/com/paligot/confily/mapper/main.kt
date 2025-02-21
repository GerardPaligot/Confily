// ktlint-disable filename
package com.paligot.confily.mapper

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.paligot.confily.map.App

@OptIn(ExperimentalCoilApi::class)
fun main() = application {
    val windowState = rememberWindowState(size = DpSize.Unspecified)
    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "Confily"
    ) {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .memoryCachePolicy(CachePolicy.DISABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .logger(DebugLogger())
                .build()
        }
        App()
    }
}
