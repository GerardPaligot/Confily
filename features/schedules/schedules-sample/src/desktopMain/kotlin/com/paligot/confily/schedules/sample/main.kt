// ktlint-disable filename
package com.paligot.confily.schedules.sample

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.paligot.confily.BuildKonfig
import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.IsDebugNamed
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.schedules.di.scheduleModule
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val platformModule: Module = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { BuildKonfig.APP_ID }
    single(named(ConfilyBaseUrlNamed)) { BuildKonfig.BASE_URL }
}

@OptIn(ExperimentalCoilApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Confily"
    ) {
        val scope = rememberCoroutineScope()
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .components {
                    add(SvgDecoder.Factory())
                    add(KtorNetworkFetcherFactory())
                }
                .logger(DebugLogger())
                .build()
        }
        KoinApplication(application = {
            modules(platformModule, scheduleModule)
        }) {
            val eventRepository = koinInject<EventRepository>()
            LaunchedEffect(Unit) {
                eventRepository.isInitialized(BuildKonfig.DEFAULT_EVENT)
            }
            App(
                isPortrait = false
            )
        }
    }
}
