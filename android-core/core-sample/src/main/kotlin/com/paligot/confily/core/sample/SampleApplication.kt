package com.paligot.confily.core.sample

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder
import com.paligot.confily.core.events.EventRepository
import io.openfeedback.viewmodels.OpenFeedbackFirebaseConfig
import io.openfeedback.viewmodels.initializeOpenFeedback
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module

open class SampleApplication(
    private val koinModules: List<Module>
) : Application(), SingletonImageLoader.Factory {
    private val eventRepository: EventRepository by inject()

    override fun onCreate() {
        super.onCreate()
        initializeOpenFeedback(
            OpenFeedbackFirebaseConfig(
                context = this,
                projectId = BuildConfig.FIREBASE_PROJECT_ID,
                applicationId = "org.gdglille.devfest.android",
                apiKey = BuildConfig.FIREBASE_API_KEY,
                databaseUrl = "https://${BuildConfig.FIREBASE_PROJECT_ID}.firebaseio.com"
            )
        )

        startKoin {
            androidContext(this@SampleApplication)
            workManagerFactory()
            modules(koinModules)
        }

        eventRepository.isInitialized(BuildConfig.DEFAULT_EVENT)
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
                add(KtorNetworkFetcherFactory())
            }
            .memoryCache { MemoryCache.Builder().build() }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .build()
            }
            .build()
}
