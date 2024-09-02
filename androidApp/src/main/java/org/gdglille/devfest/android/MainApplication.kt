package org.gdglille.devfest.android

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder
import io.openfeedback.viewmodels.OpenFeedbackFirebaseConfig
import io.openfeedback.viewmodels.initializeOpenFeedback
import org.gdglille.devfest.android.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

private const val MemoryCacheSize = 0.25
private const val DiskCacheSize = 0.10

class MainApplication : Application(), SingletonImageLoader.Factory, KoinComponent {
    override fun onCreate() {
        super.onCreate()
        initializeOpenFeedback(
            OpenFeedbackFirebaseConfig(
                context = this,
                projectId = BuildConfig.FIREBASE_PROJECT_ID,
                applicationId = BuildConfig.APPLICATION_ID,
                apiKey = BuildConfig.FIREBASE_API_KEY,
                databaseUrl = "https://${BuildConfig.FIREBASE_PROJECT_ID}.firebaseio.com"
            )
        )

        startKoin {
            androidContext(this@MainApplication)
            workManagerFactory()
            modules(appModule)
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
                add(KtorNetworkFetcherFactory())
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, MemoryCacheSize)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(DiskCacheSize)
                    .build()
            }
            .build()
}
