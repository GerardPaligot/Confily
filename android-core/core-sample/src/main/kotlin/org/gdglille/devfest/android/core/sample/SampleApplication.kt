package org.gdglille.devfest.android.core.sample

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import io.openfeedback.viewmodels.OpenFeedbackFirebaseConfig
import io.openfeedback.viewmodels.initializeOpenFeedback
import org.gdglille.devfest.repositories.EventRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module

open class SampleApplication(
    private val koinModules: List<Module>
) : Application(), ImageLoaderFactory {
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

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .components {
            add(SvgDecoder.Factory())
        }
        .memoryCache {
            MemoryCache.Builder(this)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(this.cacheDir.resolve("image_cache"))
                .build()
        }
        .respectCacheHeaders(false)
        .build()
}
