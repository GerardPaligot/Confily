package com.paligot.confily.web

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.paligot.confily.events.di.eventListModule
import com.paligot.confily.navigation.Screen
import com.paligot.confily.schedules.di.scheduleModule
import com.paligot.confily.speakers.di.speakersModule
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.web.main.Main
import com.paligot.confily.web.main.mainModule
import org.koin.compose.KoinApplication

@OptIn(ExperimentalCoilApi::class)
@Composable
fun App() {
    val uriHandler = LocalUriHandler.current
    ConfilyTheme {
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }
        KoinApplication(application = {
            modules(
                buildConfigModule,
                eventListModule,
                scheduleModule,
                speakersModule,
                mainModule
            )
        }) {
            Main(
                startDestination = Screen.EventList.route,
                onLinkClicked = { uriHandler.openUri(it) },
                onShareClicked = {},
                onItineraryClicked = { _, _ -> }
            )
        }
    }
}

private fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory())
        }
        .crossfade(enable = true)
        .logger(DebugLogger())
        .networkCachePolicy(CachePolicy.DISABLED)
        .build()
