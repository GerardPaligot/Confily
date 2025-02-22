// ktlint-disable filename
package com.paligot.confily

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
import com.paligot.confily.core.di.ApplicationIdNamed
import com.paligot.confily.core.di.ConfilyBaseUrlNamed
import com.paligot.confily.core.di.IsDebugNamed
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.main.Main
import com.paligot.confily.main.di.mainModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val platformModule: Module = module {
    single(named(IsDebugNamed)) { true }
    single(named(ApplicationIdNamed)) { "com.paligot.confily.jvm" }
    single(named(ConfilyBaseUrlNamed)) { BuildKonfig.BASE_URL }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalCoroutinesApi::class)
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
            modules(platformModule, mainModule)
        }) {
            val eventRepository = koinInject<EventRepository>()
            LaunchedEffect(Unit) {
                eventRepository.isInitialized(BuildKonfig.DEFAULT_EVENT)
            }
            Main(
                defaultEvent = BuildKonfig.DEFAULT_EVENT,
                isPortrait = false,
                launchUrl = { url -> println("launchUrl: $url") },
                onContactExportClicked = { exportNetworkingUi -> println("onContactExportClicked: $exportNetworkingUi") },
                onReportByPhoneClicked = { phone -> println("onReportByPhoneClicked: $phone") },
                onReportByEmailClicked = { email -> println("onReportByEmailClicked: $email") },
                onShareClicked = { text -> println("onShareClicked: $text") },
                onItineraryClicked = { lat, lng -> println("onItineraryClicked: $lat, $lng") },
                onScheduleStarted = {
                    scope.launch { eventRepository.fetchAndStoreAgenda() }
                },
                onProfileCreated = { println("onProfileCreated") }
            )
        }
    }
}
